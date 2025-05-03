package com.example.movies.API.service;

import com.example.movies.API.traffic.Traffic;
import com.example.movies.API.repository.TrafficRepository;
import com.example.movies.API.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;

@Service
@Transactional
public class TrafficService {
    private final TrafficRepository trafficRepo;

    public TrafficService(TrafficRepository trafficRepo) {
        this.trafficRepo = trafficRepo;
    }

    public Traffic create(Traffic traffic) {
        return trafficRepo.save(traffic);
    }

    public List<Traffic> getAll() {
        return trafficRepo.findAll();
    }

    public Traffic getById(Long id) {
        return trafficRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Traffic entry not found with id " + id));
    }

    public void delete(Long id) {
        Traffic t = getById(id);
        trafficRepo.delete(t);
    }

    public Page<Traffic> getPage(Pageable pageable) {
        return trafficRepo.findAll(pageable);
      }
}
