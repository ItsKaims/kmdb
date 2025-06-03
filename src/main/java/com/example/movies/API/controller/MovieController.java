package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.dto.MoviePatchDTO;
import com.example.movies.API.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.movies.API.dto.MovieActorsPatchDto;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;

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

   /**
   * Fetches a single Movie (or throws 404 if not found), then returns its actors set.
   */
  // Get all actors in a movie
  @GetMapping("/{id}/actors")
  public Set<Actor> getActors(@PathVariable Long id) {
      return movieService.getActorsInMovie(id);
  }

      @PatchMapping("/{id}")
    public Movie update(
        @PathVariable Long id,
        @Valid @RequestBody MoviePatchDTO dto
    ) {
        return movieService.update(
            id,
            dto.getTitle(),      // Null if not provided
            dto.getReleaseYear(), // Null if not provided
            dto.getDuration(),    // Null if not provided
            dto.getGenres(),      // Null if not provided
            dto.getActors()       // Null if not provided
        );
    }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    movieService.delete(id);
  }

  /**
   * PATCH /api/movies/{id}/actors
   * Body: { "actorIds": [3, 7, 12] }
   * Replaces this movie’s actors with exactly those IDs.
   */
  @PatchMapping("/{id}/actors")
  public Movie patchMovieActors(
      @PathVariable Long id,
      @Valid @RequestBody MovieActorsPatchDto dto,
      BindingResult bindingResult
  ) {
    // 1) If validation on dto fails (e.g. actorIds is null or empty), throw 400
    if (bindingResult.hasErrors()) {
      String errorMsg = bindingResult.getFieldError()
          .getField() + ": " + bindingResult.getFieldError().getDefaultMessage();
      throw new IllegalArgumentException(errorMsg);
    }

    // 2) Delegate to service
    return movieService.updateMovieActors(id, dto.getActorIds());
  }
}
