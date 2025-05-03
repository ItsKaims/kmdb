package com.example.movies.API;

import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.service.ActorService;
import com.example.movies.API.service.TrafficService;
import com.example.movies.API.traffic.Traffic;
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



@Controller
public class HomeController {

    private final GenreService genreService;
    private final MovieService movieService;
    private final ActorService actorService;
    private final TrafficService trafficService;

    // ① Inject the MovieService bean
    public HomeController(
        MovieService movieService, 
        GenreService genreService, 
        ActorService actorService,
        TrafficService trafficService
        ) {
        this.movieService = movieService;
        this.genreService = genreService;
        this.actorService = actorService;
        this.trafficService = trafficService;
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
        return "index";    // renders index.html
    }

    @GetMapping("/movies")
    public String movies(Model model) {
        model.addAttribute("pageTitle", "Movies");
        // ② Actually fetch and hand off the list of movies
        model.addAttribute("movies", movieService.getAll());
        return "movies";   // renders movies.html
    }

    @GetMapping("/genres")
    public String genres(Model model) {
        model.addAttribute("pageTitle", "Genres");
        // ② Actually fetch and hand off the list of genres
        model.addAttribute("genres", genreService.getAll());
        return "genres";   // renders movies.html
    }

    @GetMapping("/actors")
    public String actors(Model model) {
        model.addAttribute("pageTitle", "Actors");
        model.addAttribute("actors", actorService.getAll());
        return "actors";
    }

    @GetMapping("/traffic")
    public String traffic(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        Model model
    ) {
    // Sort by date desc, then time desc:
    Sort sort = Sort.by("date").descending()
    .and(Sort.by("time").descending());
    Pageable pageable = PageRequest.of(page, size, sort);

    Page<Traffic> trafficPage = trafficService.getPage(pageable);

        model.addAttribute("pageTitle", "Traffic");
        model.addAttribute("trafficPage", trafficPage);
        return "traffic";
    }
    

    // You can add similar methods for /actors, /genres, /traffic:
    // @GetMapping("/actors")   → model.addAttribute("actors", actorService.getAll());
    // @GetMapping("/genres")   → model.addAttribute("genres", genreService.getAll());
    // @GetMapping("/traffic")  → model.addAttribute("trafficList", trafficService.getAll());
}
