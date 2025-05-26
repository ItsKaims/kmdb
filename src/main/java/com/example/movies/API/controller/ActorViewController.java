package com.example.movies.API.controller;

import com.example.movies.API.service.ActorService;
import com.example.movies.API.entity.Actor;
import com.example.movies.API.dto.ActorDTO;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;



@Controller
public class ActorViewController {
        private final ActorService actorService;

    public ActorViewController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping("/addactor")
    public String showAddActorForm(Model model) {
        model.addAttribute("actor", new ActorDTO());
        return "addactor";
    }

  @PostMapping("/addactor")
  public String addActor(@ModelAttribute("actor") ActorDTO actorDTO) {
      Actor actor = new Actor(actorDTO.getName(), actorDTO.getBirthDate());
      actorService.create(actor);
      return "redirect:/success";
  }
}
