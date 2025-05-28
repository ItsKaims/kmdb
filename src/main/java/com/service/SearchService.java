package com.example.movies.API.service;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import org.springframework.stereotype.Service;
import com.example.movies.API.repository.ActorRepository;
import com.example.movies.API.repository.MovieRepository;
import com.example.movies.API.repository.GenreRepository;
import java.util.List;


@Service
public class SearchService {
  private final ActorRepository actorRepo;
  private final MovieRepository movieRepo;
  private final GenreRepository genreRepo;

  public SearchService(ActorRepository a, MovieRepository m, GenreRepository g) {
    this.actorRepo = a;
    this.movieRepo = m;
    this.genreRepo = g;
  }

  public List<Actor> searchActors(String q) {
    return actorRepo.findByNameContainingIgnoreCase(q);
  }
  public List<Movie> searchMovies(String q) {
    return movieRepo.findByTitleContainingIgnoreCase(q);
  }
  public List<Genre> searchGenres(String q) {
    return genreRepo.findByNameContainingIgnoreCase(q);
  }
}
