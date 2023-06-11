package com.musthavecaffeine.subsearchserver;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import model.Genre;
import model.MovieResponse;
import model.Occurrence;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Indexer {

    private static SimpleAnalyzer analyzer;
    private static Directory index;
    private static List<String[]> meta_data;

    public static List<String[]> readDataset(String path) {
        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            return reader.readAll();
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addDoc(IndexWriter w, String start_time, String end_time, String text, String imdb_id) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("text", text, Field.Store.NO));

        // use a string field for start_time because we don't want it tokenized
        doc.add(new StringField("meta", start_time + "|" + end_time + "|" + imdb_id, Field.Store.YES));
        w.addDocument(doc);

    }

    public static void indexer(String path, String meta) throws IOException, ParseException {
        // Read the dataset
//        List<String[]> dataset = readDataset(path);

        // Read the metadata to use in Queries
        meta_data = readDataset(meta);

        // Specify the analyzer for tokenizing text.
        // The same analyzer should be used for indexing and searching
        analyzer = new SimpleAnalyzer();

        // Create the index
        index = FSDirectory.open(Paths.get("D:\\IR\\sub-search-server\\data\\index"));

//        IndexWriterConfig config = new IndexWriterConfig(analyzer);
//        config.setRAMBufferSizeMB(156);
//        config.setUseCompoundFile(false);
//
//        IndexWriter w = new IndexWriter(index, config);
//
//        int size = 500000;
//        for (int i = 1; i < size; i++) { // First row is header #TODO change
//            System.out.println((float)i/size);
//            String[] line = dataset.get(i);
//            addDoc(w, line[0], line[1], line[2], line[3]);
//        }
//
//        w.close();
    }

    public static MovieResponse[] query(String querystr) throws ParseException, IOException {
        // the "subtitle" arg specifies the default field to use
        // when no field is explicitly specified in the query.
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        List<String> termList = Arrays.stream(querystr.split(" ")).toList();
        termList.forEach(term -> {
            builder.add(new TermQuery(new Term("text",term)), BooleanClause.Occur.SHOULD);
        });
        builder.setMinimumNumberShouldMatch(Math.round(termList.size() * 0.75f));

        // search
        int hitsPerPage = 500000;
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(builder.build(), hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;

        Map<String, MovieResponse> response = new HashMap<>();

        // display results
        System.out.println("Found " + hits.length + " hits.");
        for (ScoreDoc hit : hits) {
            int docId = hit.doc;
            Document d = searcher.doc(docId);
            String[] list = d.get("meta").split("\\|");
            String imdbId = list[2];
            int start = Math.round(Float.parseFloat(list[0]));
            int end = Math.round(Float.parseFloat(list[1]));
            String film_name = null;
            Genre[] genres = new Genre[]{};
            int rating = 0;
            for (int j = 1; j < meta_data.size(); j++) {
                if (meta_data.get(j)[1].equals(list[2])) {
                    film_name = meta_data.get(j)[2];
                    genres = new Gson().fromJson(meta_data.get(j)[0], Genre[].class);
                    rating = Math.round(Float.parseFloat(meta_data.get(j)[3]));
                    break;
                }
            }
            ArrayList<Integer> genreIds = new ArrayList<>();
            for (Genre genre : genres) {
                genreIds.add(genre.id);
            }

            if (response.containsKey(imdbId)) {
                response.put(imdbId, response.get(imdbId).addOccurrence(new Occurrence(start, end)));
            } else {
                response.put(imdbId, new MovieResponse(film_name, genreIds.toArray(Integer[]::new), rating, new Occurrence[]{new Occurrence(start, end)}));
            }
//            System.out.println((i + 1) + ". " + "Film name:" + film_name +" Genres:" + genres +
//                    " Starts at: " + d.get("start_time") +
//                    " Ends at: " + d.get("end_time") + " Movie id= " + d.get("imdb_id") + "\t");
        }
        // reader can only be closed when there
        // is no need to access the documents any more.
        reader.close();
        return response.values().toArray(MovieResponse[]::new);
    }

}
