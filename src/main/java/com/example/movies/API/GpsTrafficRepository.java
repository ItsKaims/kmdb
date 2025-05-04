// src/main/java/com/example/movies/API/repository/GpsTrafficRepository.java
package com.example.movies.API.repository;

import com.example.movies.API.entity.GpsTraffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GpsTrafficRepository extends JpaRepository<GpsTraffic, Long> {
}
