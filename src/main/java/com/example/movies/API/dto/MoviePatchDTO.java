// File: src/main/java/com/example/movies/API/dto/MovieUpdateDTO.java
package com.example.movies.API.dto;

import java.util.Set;
import jakarta.validation.constraints.*;

public class MoviePatchDTO {
    @Size(min = 2, max = 100, message = "Title must be 2-100 characters")
    private String title;

    @Min(value = 1888, message = "Release year must be ≥ 1888")
    private Integer releaseYear;

    @Min(value = 1, message = "Duration must be ≥ 1 minute")
    private Integer duration;

    @Size(min = 1, message = "Must provide at least one genre")
    private Set<Long> genres;

    @Size(min = 1, message = "Must provide at least one actor")
    private Set<Long> actors;

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    public Set<Long> getGenres() { return genres; }
    public void setGenres(Set<Long> genres) { this.genres = genres; }
    public Set<Long> getActors() { return actors; }
    public void setActors(Set<Long> actors) { this.actors = actors; }
}