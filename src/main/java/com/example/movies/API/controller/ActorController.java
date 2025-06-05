package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import com.example.movies.API.exception.ResourceNotFoundException;

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
  public Page<Actor> getAll(
    @PageableDefault(size = 20) Pageable pageable,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) 
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate birthdate
  ) {
    if (name != null && birthdate != null) {
      return actorService.findByNameAndBirthDate(name, birthdate, pageable);
    }
    if (name != null) {
      return actorService.findByName(name, pageable);
    }
    if (birthdate != null) {
      return actorService.findByBirthDate(birthdate, pageable);
    }
    return actorService.getAll(pageable);
  }
  

  @GetMapping("/{id}")
  public ResponseEntity<?> getById(@PathVariable Long id) {
    return actorService.getById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Actor> update(
      @PathVariable Long id,
      @RequestBody Actor partial  // name and/or birthDate
  ) {
    Actor existingActor = actorService.getById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    
    String name = partial.getName();
    LocalDate birthDate = partial.getBirthDate();
    
    if (name == null && birthDate == null) {
        return ResponseEntity.badRequest().build();
    }
    
    Actor updatedActor = actorService.update(
        id,
        name != null ? name : existingActor.getName(),
        birthDate != null ? birthDate : existingActor.getBirthDate()
    );
    
    return ResponseEntity.ok(updatedActor);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @PathVariable Long id,
      @RequestParam(defaultValue = "false") boolean force
  ) {
    actorService.getById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    actorService.delete(id, force);
    return ResponseEntity.noContent().build();
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
