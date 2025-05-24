package com.example.movies.API.dto;

import java.time.LocalDate;
import java.util.Set;

public class MovieDTO {
    private String title;
    private int releaseYear;
    private int duration;
    private Set<Long> genres;
    private Set<Long> actors;


    // Constructors (no-args and all-args if needed)
    public MovieDTO() {}

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public Set<Long> getGenres() { return genres; }
    public void setGenres(Set<Long> genres) { this.genres = genres; }

    public Set<Long> getActors() { return actors; }
    public void setActors(Set<Long> actors) { this.actors = actors; }

}
