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

    @GetMapping("/add-movie")
    public String addMovie(Model model) {
        model.addAttribute("pageTitle", "Add Movie");
        model.addAttribute("actors", actorService.getAll());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("movie", new MovieDTO());

        return "add-movie";  
    }

    @PostMapping("/add-movie")
    public String processAddMovie(@ModelAttribute("movie") MovieDTO movieDTO) {
        movieService.create(
            movieDTO.getTitle(), 
            movieDTO.getReleaseYear(), 
            movieDTO.getDuration(),
            movieDTO.getGenres(),
            movieDTO.getActors()
        ); 
        return "redirect:/success"; 
    }
}
