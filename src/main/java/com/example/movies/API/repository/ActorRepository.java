package com.example.movies.API.repository;

import com.example.movies.API.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
  List<Actor> findByNameContainingIgnoreCase(String name);
  List<Actor> findTop10ByOrderByIdDesc();
  List<Actor> findAllByOrderByNameAsc();
  List<Actor> findByNameAndBirthDate(
    String name,
    LocalDate birthDate
  );
  // ⓵ Find all actors born exactly on a given date
  List<Actor> findByBirthDate(LocalDate birthDate);

  // ⓶ Find all actors whose name contains “…” (case‐insensitive)
  //     AND whose birthDate matches exactly
  List<Actor> findByNameContainingIgnoreCaseAndBirthDate(
      String name,
      LocalDate birthDate
  );

}
