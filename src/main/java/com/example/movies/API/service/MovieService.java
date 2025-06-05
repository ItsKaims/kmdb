package com.example.movies.API.service;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.exception.ResourceNotFoundException;
import com.example.movies.API.repository.ActorRepository;
import com.example.movies.API.repository.GenreRepository;
import com.example.movies.API.repository.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.JpaSystemException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.HashSet;
import java.util.Optional;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class MovieService {
  private final MovieRepository movieRepo;
  private final GenreRepository genreRepo;
  private final ActorRepository actorRepo;

  public MovieService(
      MovieRepository movieRepo,
      GenreRepository genreRepo,
      ActorRepository actorRepo
  ) {
    this.movieRepo = movieRepo;
    this.genreRepo = genreRepo;
    this.actorRepo = actorRepo;
  }

  public Movie create(String title,
                      int year,
                      int duration,
                      Set<Long> genreIds,
                      Set<Long> actorIds) {

    Movie m = new Movie(title, year, duration);

    // attach genres
    genreIds.stream()
      .map(genreRepo::findById)
      .map(opt -> opt.orElseThrow(() ->
        new ResourceNotFoundException("Genre not found with id " + opt)))
      .forEach(m.getGenres()::add);

    // attach actors
    actorIds.stream()
      .map(actorRepo::findById)
      .map(opt -> opt.orElseThrow(() ->
        new ResourceNotFoundException("Actor not found with id " + opt)))
      .forEach(m.getActors()::add);

    try {
      // fires the INSERT immediately, including join‐table writes
      return movieRepo.saveAndFlush(m);
    } catch (JpaSystemException jse) {
      // unwrap to see if it was a UNIQUE‐constraint on title
      Throwable root = jse.getCause();
      if (root instanceof org.hibernate.exception.ConstraintViolationException ||
          root.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
        throw new DataIntegrityViolationException("duplicate-movie", jse);
      }
      throw jse;
    }
  }
  public Page<Movie> getAll(Pageable pageable) {
    return movieRepo.findAll(pageable);
  }

  public Optional<Movie> getById(Long id) {
    return movieRepo.findById(id);
  }

  public Page<Movie> findByYear(int year, Pageable pageable) {
    return movieRepo.findByReleaseYear(year, pageable);
  }

  public Page<Movie> findByGenre(Long genreId, Pageable pageable) {
    return movieRepo.findByGenreId(genreId, pageable);
  }

  public Page<Movie> findByActor(Long actorId, Pageable pageable) {
    return movieRepo.findByActorId(actorId, pageable);
  }

  public Movie update(
      Long id,
      String title,
      Integer year,
      Integer duration,
      Set<Long> genreIds,
      Set<Long> actorIds
  ) {
    Movie m = getById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
    if (title != null) m.setTitle(title);
    if (year != null) m.setReleaseYear(year);
    if (duration != null) m.setDuration(duration);
    if (genreIds != null) {
      m.getGenres().clear();
      genreIds.forEach(gid ->
        m.getGenres().add(genreRepo.findById(gid)
          .orElseThrow(() -> new ResourceNotFoundException("Genre not found " + gid)))
      );
    }
    if (actorIds != null) {
      m.getActors().clear();
      actorIds.forEach(aid ->
        m.getActors().add(actorRepo.findById(aid)
          .orElseThrow(() -> new ResourceNotFoundException("Actor not found " + aid)))
      );
    }
    return movieRepo.save(m);
  }

  public void delete(Long id) {
    Movie m = getById(id)
      .orElseThrow(() -> new RuntimeException("Movie not found with id " + id));
    // break relationships
    m.getGenres().forEach(g -> g.getMovies().remove(m));
    m.getActors().forEach(a -> a.getMovies().remove(m));
    movieRepo.delete(m);
  }

  public Page<Movie> getLatestTenMovies(Pageable pageable) {
    // Option A: custom query method on repo
    return movieRepo.findTop10ByOrderByIdDesc(pageable);
    // — or Option B: use Pageable
    // Pageable topTen = PageRequest.of(0,10, Sort.by("id").descending());
    // return actorRepo.findAll(topTen).getContent();
  }

   public Page<Movie> getAllMoviesSortedByName(Pageable pageable) {
    return movieRepo.findAllByOrderByTitleAsc(pageable);
  }

  // In MovieService:
  public Set<Actor> getActorsInMovie(Long movieId) {
      Movie movie = movieRepo.findById(movieId)
          .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));
      Set<Actor> actors = movie.getActors();
      if (actors.isEmpty()) {
          throw new ResourceNotFoundException("No actors found for movie with id " + movieId);
      }
      return actors;
  }

  /**
   * Replace this movie’s actors with exactly the given actor IDs.
   * Any previously attached actors not in actorIds will be removed;
   * any new IDs will be added. Throws 404 if movie or any actorId is not found.
   */
  public Movie updateMovieActors(Long movieId, Set<Long> actorIds) {
    // 1) Load the existing movie or throw 404
    System.out.println("Looking for movie ID: " + movieId);
    Movie movie = movieRepo.findById(movieId)
        .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + movieId));

    // 2) Build a fresh Set<Actor> from actorIds
    Set<Actor> newActors = new HashSet<>();
    for (Long actorId : actorIds) {
      Actor actor = actorRepo.findById(actorId)
          .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + actorId));
      newActors.add(actor);
    }

    // 3) Replace the movie’s actor set
    movie.setActors(newActors);

    // 4) Save. JPA will handle the join‐table updates (remove missing, add new).
    return movieRepo.save(movie);
  }
}
