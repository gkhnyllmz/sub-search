package com.musthavecaffeine.subsearchserver;

import jakarta.annotation.Resource;
import model.MovieResponse;
import model.Occurrence;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@SpringBootApplication
@RestController
public class SubSearchServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SubSearchServerApplication.class, args);
	}

	private final SearchService searchService = new SearchService();

	@CrossOrigin
	@GetMapping("/search")
	public MovieResponse[] search(@RequestParam(value = "subtitle") String subtitle, @RequestParam(value = "genre", defaultValue = "-1") int genre, @RequestParam(value = "rating", defaultValue = "0") int rating) throws ParseException, IOException {
		return searchService.search(subtitle, genre, rating);
	}

}
