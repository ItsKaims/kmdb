package com.example.movies.API.repository;

import com.example.movies.API.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    List<Genre> findTop10ByOrderByIdDesc();
    List<Genre> findAllByOrderByNameAsc();
    Page<Genre> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
