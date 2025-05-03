package com.example.movies.API.repository;

import com.example.movies.API.traffic.Traffic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

// 1) Mark as a Spring bean (optional since Spring Boot will pick up any JpaRepository)
@Repository
public interface TrafficRepository extends JpaRepository<Traffic, Long> {
  // 2) No methods needed to start—JpaRepository gives you:
//     save(), findAll(), findById(), delete(), etc.

// 3) Add custom queries if needed, for example:
    List<Traffic> findByIp(String ip);
    List<Traffic> findByDate(LocalDate date);
}
