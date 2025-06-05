package com.example.movies.API.controller;

import com.example.movies.API.service.ActorService;
import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.dto.GenreDTO;
import com.example.movies.API.dto.ActorDTO;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.entity.Movie;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.dao.DataIntegrityViolationException;
import jakarta.validation.Valid;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.movies.API.exception.ResourceNotFoundException;
import org.springframework.data.web.PageableDefault;


@Controller
public class GenreViewController {
        private final GenreService genreService;

    public GenreViewController(GenreService genreService) {
        this.genreService = genreService;   
    }
    
    @GetMapping("/addgenre")
    public String showAddGenreForm(Model model) {
        model.addAttribute("genre", new GenreDTO());
        return "addgenre";
    }

 @PostMapping("/addgenre")
    public String addGenre(
    @Valid @ModelAttribute("genre") GenreDTO genreDTO,
    BindingResult bindingResult,
    Model model
) {
    // 1) If JSR-303 validation failed (e.g. @NotBlank), re-show form immediately
    if (bindingResult.hasErrors()) {
        return "addgenre";  
    }

    try {
        // 2) Attempt to save
        Genre genre = new Genre(genreDTO.getGenreName().trim());
        genreService.create(genre);

    } catch (DataAccessException ex) {
        // 3) A UNIQUE constraint (or other DB error) happened — assume duplicate name
        bindingResult.rejectValue(
            "genreName",      // the field in your DTO
            "duplicate",      // a code you can use in your messages.properties
            "That genre name already exists."
        );
        return "addgenre";   // re-show the form, with the field error now attached
    }

    // 4) success!
    model.addAttribute("successMessage", "Genre added!");
    return "addgenre";     // or whatever view you want
    }

    @GetMapping("/genres/{id}")
    public String showGenreDetails(@PathVariable Long id, Model model) {
        // 1) Load the Genre or throw 404
        Genre genre = genreService.getById(id)
            .orElseThrow(() -> new RuntimeException("Genre not found with id " + id));

        // 2) Get all movies in that genre
        Set<Movie> movies = genre.getMovies();

        // 3) From those movies, collect all unique actors
        Set<Actor> actors = movies.stream()
                                .flatMap(m -> m.getActors().stream())
                                .collect(Collectors.toSet());

        // 4) Expose to Thymeleaf
        model.addAttribute("genre",  genre);
        model.addAttribute("movies", movies);
        model.addAttribute("actors", actors);
        return "genre-details";
    }

    @PostMapping("genres/edit/{id}")
    public String updateGenre(
        @PathVariable Long id,
        @RequestParam("name") String newName,
        RedirectAttributes attrs
    ) {
        // call your service to update the genre
        try {
        genreService.updateName(id, newName);
        attrs.addFlashAttribute("successMessage", "Genre updated!");
        } catch(ResourceNotFoundException ex) {
        attrs.addFlashAttribute("errorMessage", ex.getMessage());
        }
    return "redirect:/genres";  // back to the all-genres page
  }

    @PostMapping("genres/delete/{id}")
    public String deleteGenre(
        @PathVariable Long id,
        @RequestParam(name="force", defaultValue="false") boolean force,
        RedirectAttributes attrs
    ) {
    try {
        genreService.delete(id, force);
        attrs.addFlashAttribute("successMessage",
        force
          ? "Genre and its movie‐links have been removed."
          : "Genre deleted!");   
          
    } catch (IllegalStateException ex) {
        attrs.addFlashAttribute("errorMessage", ex.getMessage());
    }
    return "redirect:/genres";
}


}
