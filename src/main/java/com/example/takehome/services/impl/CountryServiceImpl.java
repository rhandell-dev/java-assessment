package com.example.takehome.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.takehome.codes.ExternalApiRequestType;
import com.example.takehome.config.ExternalApiConfig;
import com.example.takehome.dtos.request.ExternalApiRequestDto;
import com.example.takehome.dtos.response.ContinentDetailResponseDto;
import com.example.takehome.dtos.response.CountryDetailResponseDto;
import com.example.takehome.dtos.response.CountryItemResponseDto;
import com.example.takehome.dtos.response.CountryResponseDto;
import com.example.takehome.services.CountryService;
import com.example.takehome.utils.ExternalApiRequestBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CountryServiceImpl implements CountryService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private ExternalApiConfig externalApiConf;

    @Override
    public CountryResponseDto getCountriesByContinent(List<String> countries)
            throws JsonProcessingException {

        Map<String, List<String>> countriesMap = new HashMap<>();
        Map<String, List<String>> otherCountriesMap = new HashMap<>();
        for (String country : countries) {
            ExternalApiRequestDto requestDto = ExternalApiRequestBuilder
                    .createRequestDto(ExternalApiRequestType.COUNTRY_REQUEST,
                            country);

            CountryDetailResponseDto response = callExternalApi(requestDto,
                    CountryDetailResponseDto.class);

            String continentName = response.getCountry().getContinent()
                    .getName();

            countriesMap.computeIfAbsent(continentName,
                    item -> new ArrayList<String>()).add(country);

            otherCountriesMap
                    .computeIfAbsent(continentName, e -> getCountryList(country,
                            response.getCountry().getContinent().getCode()))
                    .remove(country);
        }
        return mapResultToResponse(countriesMap, otherCountriesMap);
    }

    /**
     * Map External API Results to Response
     * 
     * @param continentMap contains list of countries from request grouped by
     *                     continent
     * @param countryMap   contains list of other countries other than from
     *                     request grouped by continent
     * @return {@link CountryResponseDto}
     */
    private CountryResponseDto mapResultToResponse(
            Map<String, List<String>> continentMap,
            Map<String, List<String>> countryMap) {
        CountryResponseDto response = new CountryResponseDto();
        List<CountryItemResponseDto> responseList = new ArrayList<>();
        for (String key : continentMap.keySet()) {
            CountryItemResponseDto responseItem = new CountryItemResponseDto();
            responseItem.setCountries(continentMap.get(key));
            responseItem.setName(key);
            responseItem.setOtherCountries(countryMap.get(key));
            responseList.add(responseItem);
        }

        response.setContinents(responseList);
        return response;
    }

    /**
     * Get List of countries of a continent from External API
     * 
     * @param country code from request, to be removed from list
     * @param code    continent code as External API request parameter
     * @return list of countries of a specific continent
     */
    private List<String> getCountryList(String country, String code) {
        ExternalApiRequestDto request = ExternalApiRequestBuilder
                .createRequestDto(ExternalApiRequestType.CONTINENT_REQUEST,
                        code);

        ContinentDetailResponseDto continentDetail = new ContinentDetailResponseDto();
        try {
            continentDetail = callExternalApi(request,
                    ContinentDetailResponseDto.class);
        } catch (JsonProcessingException e1) {
            e1.printStackTrace();
        }
        List<String> countryList = continentDetail.getContinent().getCountries()
                .stream().map(e -> e.getCode()).collect(Collectors.toList());
        countryList.remove(country);

        return countryList;
    }

    /**
     * Call External API and convert result to the specified Type T
     * 
     * @param <T>        type class for result conversion
     * @param requestDto external api request parameters
     * @param clazz      conversion class
     * @return {@link T}
     * @throws JsonProcessingException
     */
    private <T> T callExternalApi(ExternalApiRequestDto requestDto,
            Class<T> clazz) throws JsonProcessingException {
        String response = webClient.post().uri(externalApiConf.getUrl())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(jsonMapper.writeValueAsString(requestDto)).retrieve()
                .bodyToMono(String.class).block();
        return jsonMapper.readValue(response, clazz);
    }

}
