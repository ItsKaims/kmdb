package com.example.movies.API;

import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.service.ActorService;
import com.example.movies.API.dto.MovieDTO;
import com.example.movies.API.dto.ActorDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


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
    public String home(Model model) {
        model.addAttribute("pageTitle", "Home");
        model.addAttribute("actors", actorService.getAll());
        model.addAttribute("movies", movieService.getAll());
        model.addAttribute("genres", genreService.getAll());
        model.addAttribute("lastTen", actorService.getLatestTen());
        model.addAttribute("latestThree", genreService.getLatestThree());
        model.addAttribute("latestThreeMovies", movieService.getLatestThree());
        return "index";    
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("pageTitle", "Movies");
        model.addAttribute("movies", movieService.getAll());
        return "movies";   
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("pageTitle", "Genres");
        model.addAttribute("genres", genreService.getAll());
        return "genres";  
    }

    @GetMapping("/actors")
    public String actors(Model model) {
        model.addAttribute("pageTitle", "Actors");
        model.addAttribute("actors", actorService.getAll());
        return "actors";
    }

    @GetMapping("/success")
    public String successPage(Model model) {
        model.addAttribute("pageTitle", "Success");
        return "success"; 
    }
}
