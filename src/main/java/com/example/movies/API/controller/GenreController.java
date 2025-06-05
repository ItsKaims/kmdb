package com.example.movies.API.controller;

import com.example.movies.API.entity.Genre;
import com.example.movies.API.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.movies.API.entity.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.example.movies.API.exception.ResourceNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.lang.String;


@RestController
@RequestMapping("/api/genres")
public class GenreController {
  private final GenreService genreService;
  public GenreController(GenreService genreService) { this.genreService = genreService; }

  @PostMapping @ResponseStatus(HttpStatus.CREATED)
  public Genre create(@RequestBody Genre g) {
    return genreService.create(g);
  }

  @GetMapping
  public Page<Genre> all(Pageable pageable) {
    return genreService.getAll(pageable);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> one(@PathVariable Long id) {
    return genreService.getById(id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Genre with ID " + id + " not found."));
}

  @GetMapping("/{id}/movies")
  public ResponseEntity<?> movies(@PathVariable Long id) {
    return genreService.getMoviesByGenre(id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Genre with ID " + id + " not found."));
  }

  @PatchMapping("/{id}")
  public Genre rename(@PathVariable Long id, @RequestBody Map<String,String> body) {
    String newName = body.get("name");
    return genreService.updateName(id, newName);
  }

  @DeleteMapping("/{id}")
public ResponseEntity<?> delete(
    @PathVariable Long id,
    @RequestParam(defaultValue="false") boolean force
) {
    try {
        genreService.delete(id, force);
        return ResponseEntity.noContent().build(); // ✅ 204 No Content
    } catch (ResourceNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            Map.of(
                "status", "error",
                "message", "Genre with ID " + id + " not found",
                "errorCode", "GENRE_NOT_FOUND"
            )
        );
    } catch (IllegalStateException e) {
        return ResponseEntity.badRequest().body(
            Map.of(
                "status", "error",
                "message", e.getMessage(),
                "errorCode", "GENRE_HAS_MOVIES",
                "solution", "Add ?force=true parameter to force deletion"
            )
        );
    }
}

}
