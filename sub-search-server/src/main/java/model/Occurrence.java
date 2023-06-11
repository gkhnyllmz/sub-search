package model;

public record Occurrence(int start, int end) {

    public boolean equal(Occurrence occurrence) {
        return this.start == occurrence.start && this.end == occurrence.end;
    }

}
