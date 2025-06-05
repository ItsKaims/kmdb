package com.example.movies.API.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Title is required")
  @Column(unique = true)
  private String title;

  @NotNull(message = "Release year is required")
  @Positive(message = "Release year must be a positive number")
  private Integer releaseYear;

  @NotNull(message = "Duration is required")
  @Positive(message = "Duration must be a positive number")
  private Integer duration;  // minutes

  @ManyToMany
  @JoinTable(
    name = "movie_genre",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "genre_id")
  )
  @JsonIgnoreProperties("movies")
  private Set<Genre> genres = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "movie_actor",
    joinColumns = @JoinColumn(name = "movie_id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  @JsonIgnoreProperties("movies") 
  private Set<Actor> actors = new HashSet<>();

  protected Movie() { /* JPA */ }

  public Movie(String title, int releaseYear, int duration) {
    this.title = title;
    this.releaseYear = releaseYear;
    this.duration = duration;
  }

  public Long getId() { return id; }
  public String getTitle() { return title; }
  public void setTitle(String title) { this.title = title; }
  public Integer getReleaseYear() { return releaseYear; }
  public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }
  public Integer getDuration() { return duration; }
  public void setDuration(int duration) { this.duration = duration; }
  public Set<Genre> getGenres() { return genres; }
  public Set<Actor> getActors() { return actors; }
  public void setActors(Set<Actor> actors) { this.actors = actors; }
}
