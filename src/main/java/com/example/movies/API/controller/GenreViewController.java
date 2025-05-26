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
  public String addActor(@ModelAttribute("genre") GenreDTO genreDTO) {
      Genre genre = new Genre(genreDTO.getGenreName());
      genreService.create(genre);
      return "redirect:/success";
  }
}
