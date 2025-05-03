package com.example.movies.API.controller;

import com.example.movies.API.traffic.Traffic;
import com.example.movies.API.service.TrafficService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/traffic")
public class TrafficController {
    private final TrafficService trafficService;

    public TrafficController(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    /**
     * Create a new traffic record (client IP, date, time)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Traffic create(@RequestBody Traffic traffic) {
        return trafficService.create(traffic);
    }

    /**
     * Retrieve all traffic records
     */
    @GetMapping
    public List<Traffic> getAll() {
        return trafficService.getAll();
    }

    /**
     * Retrieve a single traffic record by ID
     */
    @GetMapping("/{id}")
    public Traffic getById(@PathVariable Long id) {
        return trafficService.getById(id);
    }

    /**
     * Delete a traffic record by ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        trafficService.delete(id);
    }
}
