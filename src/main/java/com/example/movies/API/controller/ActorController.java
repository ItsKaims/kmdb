package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.service.ActorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/actors")
public class ActorController {
  private final ActorService actorService;

  public ActorController(ActorService actorService) {
    this.actorService = actorService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Actor create(@RequestBody Actor actor) {
    return actorService.create(actor);
  }

  @GetMapping
  public List<Actor> getAll(@RequestParam(required = false) String name) {
    if (name != null) {
      return actorService.findByName(name);
    }
    return actorService.getAll();
  }

  @GetMapping("/{id}")
  public Actor getById(@PathVariable Long id) {
    return actorService.getById(id);
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
}
