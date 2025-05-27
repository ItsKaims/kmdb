package com.example.movies.API.service;

import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.exception.ResourceNotFoundException;
import com.example.movies.API.repository.GenreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.orm.jpa.JpaSystemException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;


import java.util.List;
import java.util.Set;

@Service
@Transactional
public class GenreService {
  private final GenreRepository genreRepo;

  public GenreService(GenreRepository genreRepo) {
    this.genreRepo = genreRepo;
  }

  /** 1) Create a new genre */
  public Genre create(Genre genre) {
    try {
      return genreRepo.saveAndFlush(genre);
    } catch (JpaSystemException | ConstraintViolationException ex) {
      // wrap it
      throw new DataIntegrityViolationException("duplicate-genre", ex);
    }
  }

  /** 2) Retrieve all genres */
  public List<Genre> getAll() {
    return genreRepo.findAll();
  }

  /** 3) Retrieve one genre by ID, or throw 404 */
  public Genre getById(Long id) {
    return genreRepo.findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id " + id));
  }

  /** 4) Fetch all movies in a given genre */
  public Set<Movie> getMoviesByGenre(Long genreId) {
    Genre genre = getById(genreId);
    return genre.getMovies();
  }

  /** 5) Update only the name of an existing genre */
  public Genre updateName(Long id, String newName) {
    Genre genre = getById(id);
    genre.setName(newName);
    return genreRepo.save(genre);
  }

  /** 6) Delete a genre; if force=false, prevent deletion when movies exist */
  public void delete(Long id, boolean force) {
    Genre genre = getById(id);

    if (!force && !genre.getMovies().isEmpty()) {
      throw new IllegalStateException(
        "Cannot delete genre '" + genre.getName() +
        "' because it has " + genre.getMovies().size() + " associated movies."
      );
    }

    // If forced, break the bidirectional relationship:
    if (force) {
      genre.getMovies().forEach(m -> m.getGenres().remove(genre));
    }

    genreRepo.delete(genre);
  }

  public List<Genre> getLatestThree() {
    // Option A: custom query method on repo
    return genreRepo.findTop10ByOrderByIdDesc();
    // — or Option B: use Pageable
    // Pageable topTen = PageRequest.of(0,10, Sort.by("id").descending());
    // return actorRepo.findAll(topTen).getContent();
  }
}
