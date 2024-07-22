package io.csie.chris.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);
    private static final String STATUS = "status";
    private static final String UP = "UP";
    private static final String TIMESTAMP = "timestamp";
    private static final String APP_NAME = "appName";
    private static final String APP_VERSION = "appVersion";

    @Value("${spring.application.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping(value = "/health", produces = "application/json")
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put(STATUS, UP);
        response.put(TIMESTAMP, LocalDateTime.now());
        response.put(APP_NAME, appName);
        response.put(APP_VERSION, appVersion);

        logger.info("Health check performed, status: UP");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}