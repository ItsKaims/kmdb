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

  public Movie create(String title, int year, int duration,
                      Set<Long> genreIds, Set<Long> actorIds) {
    Movie m = new Movie(title, year, duration);
    genreIds.stream()
      .map(genreRepo::findById)
      .map(opt -> opt.orElseThrow(() ->
        new ResourceNotFoundException("Genre not found with id " + opt)))
      .forEach(m.getGenres()::add);
    actorIds.stream()
      .map(actorRepo::findById)
      .map(opt -> opt.orElseThrow(() ->
        new ResourceNotFoundException("Actor not found with id " + opt)))
      .forEach(m.getActors()::add);
    return movieRepo.save(m);
  }

  public List<Movie> getAll() {
    return movieRepo.findAll();
  }

  public Movie getById(Long id) {
    return movieRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Movie not found with id " + id));
  }

  public List<Movie> findByYear(int year) {
    return movieRepo.findByReleaseYear(year);
  }

  public List<Movie> findByGenre(Long genreId) {
    return movieRepo.findByGenreId(genreId);
  }

  public List<Movie> findByActor(Long actorId) {
    return movieRepo.findByActorId(actorId);
  }

  public Movie update(
      Long id,
      String title,
      Integer year,
      Integer duration,
      Set<Long> genreIds,
      Set<Long> actorIds
  ) {
    Movie m = getById(id);
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
    Movie m = getById(id);
    // break relationships
    m.getGenres().forEach(g -> g.getMovies().remove(m));
    m.getActors().forEach(a -> a.getMovies().remove(m));
    movieRepo.delete(m);
  }

  public List<Movie> getLatestThree() {
    // Option A: custom query method on repo
    return movieRepo.findTop10ByOrderByIdDesc();
    // — or Option B: use Pageable
    // Pageable topTen = PageRequest.of(0,10, Sort.by("id").descending());
    // return actorRepo.findAll(topTen).getContent();
  }
}
