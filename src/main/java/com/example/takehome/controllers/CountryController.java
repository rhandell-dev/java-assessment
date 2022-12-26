package com.example.takehome.controllers;

import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.takehome.dtos.response.CountryResponseDto;
import com.example.takehome.services.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/continent")
public class CountryController {

    @Autowired
    private CountryService countryService;

    private final Bucket bucket;

    public CountryController() {
        Bandwidth limit = Bandwidth.classic(5,
                Refill.greedy(5, Duration.ofMinutes(1)));
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    @GetMapping(path = "/get")
    public ResponseEntity<CountryResponseDto> getCountriesByContinent(
            @RequestParam(name = "countries") List<String> countries)
            throws JsonProcessingException {
        if (bucket.tryConsume(1)) {
            return ResponseEntity
                    .ok(countryService.getCountriesByContinent(countries));
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }

}
