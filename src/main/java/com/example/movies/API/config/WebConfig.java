package com.example.movies.API.config;

import com.example.movies.API.interceptor.TrafficLoggingInterceptor;
import com.example.movies.API.service.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TrafficService trafficService;

    @Autowired
    public WebConfig(TrafficService trafficService) {
        this.trafficService = trafficService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Only intercept calls under /api/traffic/**
        registry.addInterceptor(new TrafficLoggingInterceptor(trafficService))
                .addPathPatterns("/");
    }
}
