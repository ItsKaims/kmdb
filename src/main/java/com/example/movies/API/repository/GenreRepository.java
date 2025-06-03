package com.example.movies.API.repository;

import com.example.movies.API.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findTop10ByOrderByIdDesc();
    List<Genre> findAllByOrderByNameAsc();
    List<Genre> findByNameContainingIgnoreCase(String name);
}
