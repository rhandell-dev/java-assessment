package com.example.takehome.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.takehome.dtos.response.CountryResponseDto;
import com.example.takehome.services.CountryService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping(path = "/country")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public CountryResponseDto getCountriesByContinent(
            @RequestParam(name = "countries") List<String> countries)
            throws JsonProcessingException {
        return countryService.getCountriesByContinent(countries);
    }

}
