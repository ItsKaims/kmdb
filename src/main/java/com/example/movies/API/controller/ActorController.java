package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity; 
import java.util.Optional; 

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
  private final ActorService actorService;

  public ActorController(ActorService actorService) {
    this.actorService = actorService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Actor create(@Valid @RequestBody Actor actor) {
    return actorService.create(actor);
  }

  @GetMapping
  public List<Actor> getAll(
    @RequestParam(required = false) String name,
    @RequestParam(required = false) 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthdate
  ) {
    if (name != null && birthdate != null) {
      return actorService.findByNameAndBirthDate(name, birthdate);
    }
    if (name != null) {
      return actorService.findByName(name);
    }
    if (birthdate != null) {
      return actorService.findByBirthDate(birthdate);
    }
    return actorService.getAll();
  }
  

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    return actorService.getById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}")
  public Actor update(
      @PathVariable Long id,
      @RequestBody Actor partial  // name and/or birthDate
  ) {
    return actorService.update(
      id,
      partial.getName(),
      partial.getBirthDate()
    );
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(
      @PathVariable Long id,
      @RequestParam(defaultValue = "false") boolean force
  ) {
    actorService.delete(id, force);
  }

  /**
   * GET /api/actors/{id}/movies
   * Returns all movies this actor has appeared in.
   * Throws 404 if the actor doesn’t exist or (optionally) has no movies.
   */
  @GetMapping("/{id}/movies")
  public ResponseEntity<?> getMoviesForActor(@PathVariable Long id) {
    return actorService.getMoviesForActor(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.noContent().build());
  }

}
