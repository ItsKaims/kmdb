package com.example.movies.API.repository;

import com.example.movies.API.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  Page<Movie> findByReleaseYear(int year, Pageable pageable);
  Page<Movie> findTop10ByOrderByIdDesc(Pageable pageable);
  Page<Movie> findAllByOrderByTitleAsc(Pageable pageable);
  Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

  @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
  Page<Movie> findByGenreId(@Param("genreId") Long genreId, Pageable pageable);

  @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
  Page<Movie> findByActorId(@Param("actorId") Long actorId, Pageable pageable);

}
