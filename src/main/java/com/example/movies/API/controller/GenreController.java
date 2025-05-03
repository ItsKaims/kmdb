package com.example.movies.API.controller;

import com.example.movies.API.entity.Genre;
import com.example.movies.API.service.GenreService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.movies.API.entity.Movie;

import java.util.List;
import java.util.Set;
import java.util.Map;


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
  public List<Genre> all() {
    return genreService.getAll();
  }

  @GetMapping("/{id}")
  public Genre one(@PathVariable Long id) {
    return genreService.getById(id);
  }

  @GetMapping("/{id}/movies")
  public Set<Movie> movies(@PathVariable Long id) {
    return genreService.getMoviesByGenre(id);
  }

  @PatchMapping("/{id}")
  public Genre rename(@PathVariable Long id, @RequestBody Map<String,String> body) {
    String newName = body.get("name");
    return genreService.updateName(id, newName);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long id,
      @RequestParam(defaultValue="false") boolean force
  ) {
    genreService.delete(id, force);
  }

}
