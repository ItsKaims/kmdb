package com.example.movies.API;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "<B>🎬 Welcome to the Movies API! Try GET /movies next.<B>";
    }
}
