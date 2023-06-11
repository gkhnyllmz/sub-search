package com.musthavecaffeine.subsearchserver;

import model.MovieResponse;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Arrays;

public class SearchService {

    SearchService() {
        String path = "D:\\IR\\sub-search-server\\data\\movies_subtitles.csv";
        String meta = "D:\\IR\\sub-search-server\\data\\movies_meta.csv";
        try {
            System.out.print("Reading the datasets, and indexing the corpus\n");
            // Once the indexer run, the meta_data is assigned the meta_data field of Indexer class, and Corpus is indexed
            Indexer.indexer(path, meta);
            System.out.print("Indexing completed\n");
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public MovieResponse[] search(String subtitle, int genre, int rating) throws ParseException, IOException {

        System.out.print("Query for " + subtitle + " is beginning\n");
        MovieResponse[] results = Indexer.query(subtitle);

//        MovieResponse[] results = new MovieResponse[]{
//            new MovieResponse("Toy Story Collection", new int[]{16, 35, 10751}, 4, new Occurrence[]{new Occurrence(58, 62), new Occurrence(78, 84)}),
//            new MovieResponse("Jumanji", new int[]{12, 14, 10751}, 3, new Occurrence[]{new Occurrence(30, 34)})
//        };

        return Arrays.stream(results).filter(movie -> movie.rating() >= rating &&
                (genre < 0 || Arrays.stream(movie.genres()).anyMatch(g -> g == genre))).toArray(MovieResponse[]::new);
    }
}
