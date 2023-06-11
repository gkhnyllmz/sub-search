import {Occurrence} from "./occurrance";

export interface Movie {
  title: string;
  genres: number[];
  rating: number;
  occurrences: Occurrence[];
  moreOccurrences: number;
}
