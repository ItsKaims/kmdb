package com.example.movies.API.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "gps_traffic")
public class GpsTraffic {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = true)
  private String imei;

  @Column(nullable = false)
  private LocalDateTime timestamp;

  @Column(nullable = false)
  private double latitude;

  @Column(nullable = false)
  private double longitude;

  @Column
  private Double speed;    // km/h, optional

  @Column
  private Double course;   // degrees, optional

  protected GpsTraffic() { /* for JPA */ }

  public GpsTraffic(String imei,
                    LocalDateTime timestamp,
                    double latitude,
                    double longitude,
                    Double speed,
                    Double course) {
    this.imei      = imei;
    this.timestamp = timestamp;
    this.latitude  = latitude;
    this.longitude = longitude;
    this.speed     = speed;
    this.course    = course;
  }

  // getters (no setters for id)
  public Long getId()                  { return id; }
  public String getImei()              { return imei; }
  public LocalDateTime getTimestamp()  { return timestamp; }
  public double getLatitude()          { return latitude; }
  public double getLongitude()         { return longitude; }
  public Double getSpeed()             { return speed; }
  public Double getCourse()            { return course; }
}
