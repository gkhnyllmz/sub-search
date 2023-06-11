import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Movie} from "../model/movie";

interface Genre {
  name: string;
  id: number;
}

@Component({
  selector: 'app-movie',
  templateUrl: './movie.component.html',
  styleUrls: ['./movie.component.css'],
  host: {class: 'flex-grow-1'}
})
export class MovieComponent {

  genreOptions: Genre[] = [
    { name: 'All', id: -1 },
    { name: 'Action', id: 28 },
    { name: 'Adventure', id: 12 },
    { name: 'Animation', id: 16 },
    { name: 'Comedy', id: 35 },
    { name: 'Crime', id: 80 },
    { name: 'Documentary', id: 99 },
    { name: 'Drama', id: 18 },
    { name: 'Family', id: 10751 },
    { name: 'Fantasy', id: 14},
    { name: 'History', id: 36 },
    { name: 'Horror', id: 27 },
    { name: 'Music', id: 10402 },
    { name: 'Mystery', id: 9648 },
    { name: 'Romance', id: 10749 },
    { name: 'Science Fiction', id: 878 },
    { name: 'TV Movie', id: 10770 },
    { name: 'Thriller', id: 53 },
    { name: 'War', id: 10752 },
    { name: 'Western', id: 37 },
  ];

  subtitle: string = '';
  genre: Genre = this.genreOptions[0];
  rating: number = 3;

  results: Movie[] = [
    // {title: 'Toy Story Collection', genres: [16, 35, 10751], rating: 4, start: 58, end: 62},
    // {title: 'Jumanji', genres: [12, 14, 10751], rating: 3, start: 30, end: 34}
  ];

  constructor(private httpClient: HttpClient) {}

  getGenreTitle(id: number): string {
    for (const genre of this.genreOptions) {
      if (genre.id === id) {
        return genre.name;
      }
    }
    return '-';
  }

  search() {
      this.httpClient.get<Movie[]>(`http://localhost:8080/search?subtitle=${this.subtitle}&rating=${this.rating || 0}&genre=${this.genre?.id || -1}`).subscribe(res => {
        res.forEach((movie: Movie) => {
          movie.moreOccurrences = movie.occurrences.splice(5)?.length || 0;
        })
        this.results = res;
      })
  }

}
