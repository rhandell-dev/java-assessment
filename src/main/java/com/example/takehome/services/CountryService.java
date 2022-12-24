package com.example.takehome.services;

import java.util.List;

import com.example.takehome.dtos.response.CountryResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface CountryService {

    /**
     * Get Countries and group by Continent
     * 
     * @param countries request from client
     * @return {@link CountryResponseDto}
     * @throws JsonProcessingException
     */
    public CountryResponseDto getCountriesByContinent(List<String> countries)
            throws JsonProcessingException;
}
