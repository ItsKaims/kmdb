package com.example.movies.API.interceptor;

import com.example.movies.API.traffic.Traffic;
import com.example.movies.API.service.TrafficService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDate;
import java.time.LocalTime;

public class TrafficLoggingInterceptor implements HandlerInterceptor {

    private final TrafficService trafficService;

    public TrafficLoggingInterceptor(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1) Determine client IP, falling back if behind a proxy
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }

        // 2) Create and save a Traffic record
        Traffic t = new Traffic(
            ip,
            LocalDate.now(),
            LocalTime.now()
        );
        trafficService.create(t);

        // 3) Continue processing the request
        return true;
    }
}
