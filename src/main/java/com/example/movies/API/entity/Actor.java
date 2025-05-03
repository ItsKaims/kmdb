package com.example.movies.API.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(name = "actors")
public class Actor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private LocalDate birthDate;

  @ManyToMany(mappedBy = "actors")
  @JsonIgnoreProperties("actors")
  private Set<Movie> movies = new HashSet<>();

  protected Actor() { /* JPA */ }

  public Actor(String name, LocalDate birthDate) {
    this.name = name;
    this.birthDate = birthDate;
  }

  public Long getId() { return id; }
  public String getName() { return name; }
  public void setName(String name) { this.name = name; }
  public LocalDate getBirthDate() { return birthDate; }
  public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
  public Set<Movie> getMovies() { return movies; }
}
