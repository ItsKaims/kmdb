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
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import com.example.movies.API.annotation.ValidPagination;

import java.util.Map;
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
  public Page<Movie> getAll(
      @RequestParam(required = false) Long genre,
      @RequestParam(required = false) Integer year,
      @RequestParam(required = false) Long actor,
      @ValidPagination Pageable pageable
  ) {
    if (genre != null)      return movieService.findByGenre(genre, pageable);
    else if (year != null)  return movieService.findByYear(year, pageable);
    else if (actor != null) return movieService.findByActor(actor, pageable);
    else                    return movieService.getAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    return movieService.getById(id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", "Movie not found with id " + id)));
}

   /**
   * Fetches a single Movie (or throws 404 if not found), then returns its actors set.
   */
  // Get all actors in a movie with pagination
  @GetMapping("/{id}/actors")
  public Page<Actor> getActors(
      @PathVariable Long id,
      Pageable pageable
  ) {
      return movieService.getActorsInMovie(id, pageable);
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
   * Search movies by title containing the given search term (case-insensitive)
   */
  @GetMapping("/search")
  public Page<Movie> searchByTitle(
      @RequestParam String query,
      @ValidPagination Pageable pageable
  ) {
      return movieService.searchByTitle(query, pageable);
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
