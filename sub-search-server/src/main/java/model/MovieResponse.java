package model;

import java.util.Arrays;
import java.util.List;

public record MovieResponse(String title, Integer[] genres, int rating, Occurrence[] occurrences) {

    public MovieResponse addOccurrence(Occurrence occurrence) {
        if (Arrays.stream(occurrences).anyMatch(o -> o.equal(occurrence))) {
            return this;
        }
        List<Occurrence> list = new java.util.ArrayList<>(Arrays.stream(occurrences).toList());
        list.add(occurrence);
        return new MovieResponse(title, genres, rating, list.toArray(Occurrence[]::new));
    }

}
