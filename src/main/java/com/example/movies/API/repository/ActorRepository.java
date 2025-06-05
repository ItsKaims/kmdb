package com.example.movies.API.repository;

import com.example.movies.API.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
  Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
  Page<Actor> findTop10ByOrderByIdDesc(Pageable pageable);
  Page<Actor> findAllByOrderByNameAsc(Pageable pageable);
  Page<Actor> findByNameAndBirthDate(
    String name,
    LocalDate birthDate,
    Pageable pageable
  );
  // ⓵ Find all actors born exactly on a given date
  Page<Actor> findByBirthDate(LocalDate birthDate,Pageable pageable);

  // ⓶ Find all actors whose name contains “…” (case‐insensitive)
  //     AND whose birthDate matches exactly
  Page<Actor> findByNameContainingIgnoreCaseAndBirthDate(
      String name,
      LocalDate birthDate,
      Pageable pageable
  );

}
