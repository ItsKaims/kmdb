package com.example.movies.API.dto;

import java.time.LocalDate;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;




public class GenreDTO {
    @NotBlank(message = "Genre cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 2 and 50 characters")
    private String genreName;


    // Constructors (no-args and all-args if needed)
    public GenreDTO() {}

    public GenreDTO(String genreName) {
        this.genreName = genreName;
    }

    // Getters and Setters
    public String getGenreName() {
        return genreName;
    }
    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

}
