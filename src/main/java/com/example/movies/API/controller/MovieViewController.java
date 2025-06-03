package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.service.ActorService;
import com.example.movies.API.dto.MovieDTO;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.ActorService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Set;
import java.util.stream.Collectors;


@Controller
public class MovieViewController {
    private final GenreService genreService;
    private final MovieService movieService;
    private final ActorService actorService;

    public MovieViewController(
        MovieService movieService, 
        GenreService genreService, 
        ActorService actorService
        ) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.actorService = actorService;
    }

    @GetMapping("/addmovie")
    public String addMovie(Model model) {
        model.addAttribute("pageTitle", "Add Movie");
        model.addAttribute("actors", actorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("movie", new MovieDTO());

        return "addmovie";  
    }

    @PostMapping("/addmovie")
    public String processAddMovie(@Valid @ModelAttribute("movie") MovieDTO movieDTO,
        BindingResult bindingResult,
        Model model
    ) {
        populateFormLists(model);
        // 1) If JSR-303 validation failed (e.g. @NotBlank), re-show form immediately
        if (bindingResult.hasErrors()) {
            return "addmovie";  
        } try {
        movieService.create(
            movieDTO.getTitle(), 
            movieDTO.getReleaseYear(), 
            movieDTO.getDuration(),
            movieDTO.getGenres(),
            movieDTO.getActors()
        );
        } catch(DataAccessException ex) {
            // 3) A UNIQUE constraint (or other DB error) happened — assume duplicate name
            bindingResult.rejectValue(
                "title",      // the field in your DTO
                "duplicate",      // a code you can use in your messages.properties
                "That movie already exists."
            );
            return "addmovie";   // re-show the form, with the field error now attached
        } 
        // 4) success!
        model.addAttribute("successMessage", "Movie added!");
        return "addmovie"; 
    }

    private void populateFormLists(Model model) {
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("actors", actorService.getAll());
    }

    @GetMapping("/movies/{id}")
    public String showMovieDetails(@PathVariable Long id, Model model) {
        // 1) Load the Movie or throw 404
        Movie movie = movieService.getById(id);

        // 2) Get all movies in that genre
        Set<Genre> genres = movie.getGenres();
        Set<Actor> actors = movie.getActors();

        // 3) Expose to Thymeleaf
        model.addAttribute("movie",  movie);
        model.addAttribute("genres", genres);
        model.addAttribute("actors", actors);
        return "movie-details";
    }
}
