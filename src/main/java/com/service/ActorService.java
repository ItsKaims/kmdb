package com.example.movies.API.service;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.exception.ResourceNotFoundException;
import com.example.movies.API.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.JpaSystemException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ActorService {
  private final ActorRepository actorRepo;

  public ActorService(ActorRepository actorRepo) {
    this.actorRepo = actorRepo;
  }

  public Actor create(Actor actor) {
      return actorRepo.saveAndFlush(actor);
  }

  public List<Actor> getAll() {
    return actorRepo.findAll();
  }

  public Actor getById(Long id) {
    return actorRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
  }

  public List<Actor> findByName(String name) {
    return actorRepo.findByNameContainingIgnoreCase(name);
  }

  public Actor update(Long id, String newName, LocalDate newBirthDate) {
    Actor actor = getById(id);
    actor.setName(newName);
    actor.setBirthDate(newBirthDate);
    return actorRepo.save(actor);
  }

  public void delete(Long id, boolean force) {
    Actor actor = getById(id);
    if (!force && !actor.getMovies().isEmpty()) {
      throw new IllegalStateException(
        "Unable to delete actor '" + actor.getName() +
        "' as they are associated with " + actor.getMovies().size() + " movies."
      );
    }
    if (force) {
      actor.getMovies().forEach(m -> m.getActors().remove(actor));
    }
    actorRepo.delete(actor);
  }

  public List<Actor> getLatestTenActors() {
    // Option A: custom query method on repo
    return actorRepo.findTop10ByOrderByIdDesc();
    // — or Option B: use Pageable
    // Pageable topTen = PageRequest.of(0,10, Sort.by("id").descending());
    // return actorRepo.findAll(topTen).getContent();
  }

   public List<Actor> getAllActorsSortedByName() {
    return actorRepo.findAllByOrderByNameAsc();
  }
}
