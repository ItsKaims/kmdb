package com.example.movies.API.controller;

import com.example.movies.API.service.ActorService;
import com.example.movies.API.entity.Actor;
import com.example.movies.API.entity.Genre;
import com.example.movies.API.service.GenreService;
import com.example.movies.API.dto.GenreDTO;
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
}
