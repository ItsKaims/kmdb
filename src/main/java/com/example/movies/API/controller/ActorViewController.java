package com.example.movies.API.controller;

import com.example.movies.API.service.ActorService;
import com.example.movies.API.service.MovieService;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Movie;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.dto.ActorDTO;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.movies.API.exception.ResourceNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ActorViewController {
        private final ActorService actorService;
        private final MovieService movieService;
        private final GenreService genreService;
        
        public ActorViewController(
          ActorService actorService, 
          MovieService movieService, 
          GenreService genreService
        ) {
          this.actorService = actorService;
          this.movieService = movieService;
          this.genreService = genreService;
        }
        
    @GetMapping("/addactor")
    public String showAddActorForm(Model model) {
        model.addAttribute("actor", new ActorDTO());
        return "addactor";
    }

  @PostMapping("/addactor")
  public String addActor(@Valid @ModelAttribute("actor") ActorDTO actorDTO,
    BindingResult bindingResult,
    Model model
  ) {
    // 1) If JSR-303 validation failed (e.g. @NotBlank), re-show form immediately
     if (bindingResult.hasErrors()) {
    return "addactor";
  }
  try {
    Actor actor = new Actor(actorDTO.getName(), actorDTO.getBirthDate());
    actorService.create(actor);
  } catch (DataIntegrityViolationException | JpaSystemException ex) {
    bindingResult.rejectValue("name",
                              "duplicate",
                              "That actor name already exists.");
    return "addactor";
  }
  model.addAttribute("successMessage", "Actor added!");
  return "addactor";
  }

  @GetMapping("/actors/{id}")
    public String showActorDetails(@PathVariable Long id, Model model) {
        // 1) Load the Actor or throw 404
        Actor actor = actorService.getById(id)
           .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id " + id));

        // 2) Get all movies where the actor is and genres
        Set<Movie> movies = actor.getMovies();
        Set<Genre> genres = movies.stream()
                                  .flatMap(m -> m.getGenres().stream())
                                  .collect(Collectors.toSet());

        // 3) Expose to Thymeleaf
        model.addAttribute("movies",  movies);
        model.addAttribute("genres", genres);
        model.addAttribute("actor", actor);
        return "actor-details";
    }
}
