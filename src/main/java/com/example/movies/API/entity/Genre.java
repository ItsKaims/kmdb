package com.example.movies.API.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "genres")
public class Genre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @ManyToMany(mappedBy = "genres")
  @JsonIgnoreProperties("genres")
  private Set<Movie> movies = new HashSet<>();

  protected Genre() { /* JPA */ }

  public Genre(String name) {
    this.name = name;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }

  public Set<Movie> getMovies() { return movies; }
}
