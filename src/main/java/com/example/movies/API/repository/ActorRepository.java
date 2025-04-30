package com.example.movies.API.repository;

import com.example.movies.API.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActorRepository extends JpaRepository<Actor, Long> {
  List<Actor> findByNameContainingIgnoreCase(String name);
}
