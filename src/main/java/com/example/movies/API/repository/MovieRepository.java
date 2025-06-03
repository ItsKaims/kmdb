package com.example.movies.API.repository;

import com.example.movies.API.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {

  List<Movie> findByReleaseYear(int year);
  List<Movie> findTop10ByOrderByIdDesc();
  List<Movie> findAllByOrderByTitleAsc();
  List<Movie> findByTitleContainingIgnoreCase(String title);

  @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
  List<Movie> findByGenreId(@Param("genreId") Long genreId);

  @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
  List<Movie> findByActorId(@Param("actorId") Long actorId);

}
