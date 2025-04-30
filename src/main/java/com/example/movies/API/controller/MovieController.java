package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Movie create(
      @RequestParam String title,
      @RequestParam int releaseYear,
      @RequestParam int duration,
      @RequestParam Set<Long> genres,
      @RequestParam Set<Long> actors
  ) {
    return movieService.create(title, releaseYear, duration, genres, actors);
  }

  @GetMapping
  public List<Movie> getAll(
      @RequestParam(required = false) Long genre,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Long actor
  ) {
    if (genre != null)      return movieService.findByGenre(genre);
    else if (year != null)  return movieService.findByYear(year);
    else if (actor != null) return movieService.findByActor(actor);
    else                    return movieService.getAll();
  }

  @GetMapping("/{id}")
  public Movie getById(@PathVariable Long id) {
    return movieService.getById(id);
  }

  // Get all actors in a movie
  @GetMapping("/{id}/actors")
  public Set<Actor> getActors(@PathVariable Long id) {
    return movieService.getById(id).getActors();
  }

  @PatchMapping("/{id}")
  public Movie update(
      @PathVariable Long id,
      @RequestParam(required = false) String title,
      @RequestParam(required = false) Integer releaseYear,
      @RequestParam(required = false) Integer duration,
      @RequestParam(required = false) Set<Long> genres,
      @RequestParam(required = false) Set<Long> actors
  ) {
    return movieService.update(id, title, releaseYear, duration, genres, actors);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    movieService.delete(id);
  }
}
