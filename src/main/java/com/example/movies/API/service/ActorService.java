package com.example.movies.API.service;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.exception.ResourceNotFoundException;
import com.example.movies.API.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.JpaSystemException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity; 
import java.util.Optional; 
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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

  public Page<Actor> getAll(Pageable pageable) {
    return actorRepo.findAll(pageable);
  }

  public Optional<Actor> getById(Long id) {
    return actorRepo.findById(id);
      //.orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
  }

  public Page<Actor> findByName(String name, Pageable pageable) {
    return actorRepo.findByNameContainingIgnoreCase(name, pageable);
  }

  public Actor update(Long id, String newName, LocalDate newBirthDate) {
    Actor actor = actorRepo.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
    actor.setName(newName);
    actor.setBirthDate(newBirthDate);
    return actorRepo.save(actor);
  }

  public void delete(Long id, boolean force) {
    Actor actor = actorRepo.findById(id)
    .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));
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

  public Page<Actor> getLatestTenActors(Pageable pageable) {
    // Option A: custom query method on repo
    return actorRepo.findTop10ByOrderByIdDesc(pageable);
    // — or Option B: use Pageable
    // Pageable topTen = PageRequest.of(0,10, Sort.by("id").descending());
    // return actorRepo.findAll(topTen).getContent();
  }

   public Page<Actor> getAllActorsSortedByName(Pageable pageable) {
    return actorRepo.findAllByOrderByNameAsc(pageable);
  }

   public Page<Actor> findByBirthDate(LocalDate birthDate, Pageable pageable) {
    return actorRepo.findByBirthDate(birthDate, pageable);
  }

  public Page<Actor> findByNameAndBirthDate(String name, LocalDate birthDate, Pageable pageable) {
    return actorRepo.findByNameContainingIgnoreCaseAndBirthDate(name, birthDate, pageable);
  }

  /**
   * 1) Load the Actor (or throw ResourceNotFoundException if not found)
   * 2) Get their movies set
   * 3) If you want “no movies” to be an error, check isEmpty() and throw
   */
  /**
     * Returns Optional<Set<Movie>>: Empty Optional if actor not found OR no movies found.
     */
  public Optional<Set<Movie>> getMoviesForActor(Long actorId) {
    return actorRepo.findById(actorId)
        .map(Actor::getMovies)
        .filter(movies -> movies != null && !movies.isEmpty());
  }
}
