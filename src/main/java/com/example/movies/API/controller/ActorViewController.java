package com.example.movies.API.controller;

import com.example.movies.API.service.ActorService;
import com.example.movies.API.entity.Actor;
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
}
