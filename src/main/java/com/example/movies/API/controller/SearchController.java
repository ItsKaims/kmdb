package com.example.movies.API.controller;

import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.example.movies.API.service.SearchService;
import com.example.movies.API.repository.ActorRepository;
import com.example.movies.API.repository.MovieRepository;
import com.example.movies.API.repository.GenreRepository;
import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.service.ActorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;


@Controller
public class SearchController {
    private final SearchService searchService;
    private final GenreService genreService;
    private final MovieService movieService;
    private final ActorService actorService;

  public SearchController(
        SearchService s,
        MovieService movieService, 
        GenreService genreService, 
        ActorService actorService
    ) {
        this.searchService = s;
        this.movieService = movieService;
        this.genreService = genreService;
        this.actorService = actorService;
  }

  @GetMapping("/search")
  public String doSearch(@RequestParam(required = false) String query,
                         @RequestParam(defaultValue="all") String criteria,
                         Model model) {
    // always echo back so your form stays filled:
    model.addAttribute("query",    query);
    model.addAttribute("criteria", criteria);
    
    if (query == null || query.trim().isEmpty()) {
        model.addAttribute("queryError", "Please enter something to search for.");
        model.addAttribute("latestTenActors", actorService.getLatestTenActors());
        model.addAttribute("latestTenGenres", genreService.getLatestTenGenres());
        model.addAttribute("latestTenMovies", movieService.getLatestTenMovies());
        return "index";   // re‐render the same template with an error
    }

    if("actor".equals(criteria) || "all".equals(criteria)) {
      model.addAttribute("actorResults",
                         searchService.searchActors(query));
    }
    if("movie".equals(criteria) || "all".equals(criteria)) {
      model.addAttribute("movieResults",
                         searchService.searchMovies(query));
    }
    if("genre".equals(criteria) || "all".equals(criteria)) {
      model.addAttribute("genreResults",
                         searchService.searchGenres(query));
    }

    return "search-results";
  }
}
