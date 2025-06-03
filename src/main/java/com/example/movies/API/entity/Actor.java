package com.example.movies.API.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "actors",
uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
public class Actor {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String name;

  @Column(nullable = false)
  @NotNull(message = "Birth date cannot be empty")
  @PastOrPresent(message = "Birth date must be in the past or today")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate birthDate;

  @ManyToMany(mappedBy = "actors")
  @JsonIgnore 
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
