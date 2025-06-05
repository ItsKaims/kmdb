package com.example.movies.API.controller;

import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.service.ActorService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;


@Controller
public class HomeController {

    private final GenreService genreService;
    private final MovieService movieService;
    private final ActorService actorService;

    public HomeController(
        MovieService movieService, 
        GenreService genreService, 
        ActorService actorService
        ) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.actorService = actorService;
    }

    @GetMapping("/")
    public String home(Model model, @PageableDefault(size = 10) Pageable pageable) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("actors", actorService.getAll(pageable).getContent());
        model.addAttribute("movies", movieService.getAll(pageable).getContent());
        model.addAttribute("genres", genreService.getAll(pageable).getContent());
        model.addAttribute("latestTenActors", actorService.getLatestTenActors(PageRequest.of(0, 10, Sort.by("id").descending())).getContent());
        model.addAttribute("latestTenGenres", genreService.getLatestTenGenres());
        model.addAttribute("latestTenMovies", movieService.getLatestTenMovies(pageable).getContent());
        return "index";    
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());
        model.addAttribute("pageTitle", "Movies");
        model.addAttribute("movies", movieService.getAllMoviesSortedByName(pageable).getContent());
        return "movies";
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        Pageable pageable = PageRequest.of(0, 50, Sort.by("name").ascending());
        model.addAttribute("pageTitle", "Genres");
        model.addAttribute("genres", genreService.getAllGenresSortedByName());
        return "genres";
    }

    @GetMapping("/actors")
    public String actors(Model model) {
        Pageable pageable = PageRequest.of(0, 50, Sort.by("name").ascending());
        model.addAttribute("pageTitle", "Actors");
        model.addAttribute("actors", actorService.getAllActorsSortedByName(PageRequest.of(0, 50, Sort.by("name").ascending())).getContent());
        return "actors";
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        model.addAttribute("pageTitle", "Success");
        return "success"; 
    }
}
