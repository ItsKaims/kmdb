package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
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
        model.addAttribute("actors", actorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        // 1) If JSR-303 validation failed (e.g. @NotBlank), re-show form immediately
        if (bindingResult.hasErrors()) {
            populateFormLists(model);
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
                "That genre name already exists."
            );
            populateFormLists(model);
            return "addmovie";   // re-show the form, with the field error now attached
        } 
        // 4) success!
        model.addAttribute("successMessage", "Genre added!");
        return "addmovie"; 
    }

    private void populateFormLists(Model model) {
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("actors", actorService.getAll());
    }
}
