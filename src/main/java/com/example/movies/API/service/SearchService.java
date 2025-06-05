package com.example.movies.API.service;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import org.springframework.stereotype.Service;
import com.example.movies.API.repository.ActorRepository;
import com.example.movies.API.repository.MovieRepository;
import com.example.movies.API.repository.GenreRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

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

  public Page<Actor> searchActors(String q, Pageable pageable) {
    return actorRepo.findByNameContainingIgnoreCase(q, pageable);
  }
  public Page<Movie> searchMovies(String q, Pageable pageable) {
    return movieRepo.findByTitleContainingIgnoreCase(q, pageable);
  }
  public Page<Genre> searchGenres(String q, Pageable pageable) {
    return genreRepo.findByNameContainingIgnoreCase(q, pageable);
  }
}
