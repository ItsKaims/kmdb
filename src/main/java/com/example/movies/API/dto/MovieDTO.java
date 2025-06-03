package com.example.movies.API.dto;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PastOrPresent;
import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.*;

public class MovieDTO {
    @NotBlank(message = "Movie name cannot be empty")
    @Size(min = 2, max=100, message= "Movie name has to be between 2 and 100 characters long")
    private String title;
    @NotNull(message = "Release date can not be blank")
    @Min(value = 1, message = "Release Year has to be number")
    private Integer releaseYear;
    @NotNull(message = "duration date can not be blank")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;
    @NotNull(message ="Please select at least one genre")
    @Size(min = 1, message = "Select at least one genre from list")
    private Set<Long> genres = new HashSet<>();
    @NotNull(message = "Actor IDs cannot be null")
    @NotEmpty(message = "Please select at least one actor")
    @Size(min = 1, message = "Select at least one actor from list")
    private Set<Long> actors = new HashSet<>();
    


    // Constructors (no-args and all-args if needed)
    public MovieDTO() {}

    // Getters and setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) { this.releaseYear = releaseYear; }

    public Integer getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public Set<Long> getGenres() { return genres; }
    public void setGenres(Set<Long> genres) { this.genres = genres; }

    public Set<Long> getActors() { return actors; }
    public void setActors(Set<Long> actors) { this.actors = actors; }

}
