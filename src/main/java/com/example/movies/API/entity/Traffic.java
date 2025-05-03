package com.example.movies.API.traffic;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "traffic")

public class Traffic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ip;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    protected Traffic() { }

public Traffic (String ip, LocalDate date, LocalTime time) { 
    this.ip = ip;
    this.date = date;
    this.time = time;
}

public Long getId() { return id; }
public String getIp() { return ip; }
public LocalDate getDate() { return date; }
public LocalTime getTime() { return time; }

}