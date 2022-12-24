package com.example.takehome.utils;

import com.example.takehome.codes.ExternalApiRequestType;
import com.example.takehome.dtos.request.ExternalApiRequestDto;

public class ExternalApiRequestBuilder {
    private static final String GET_COUNTRY_QUERY = "query ($code: ID!) { country(code: $code) {continent { code name }}}";

    private static final String GET_CONTINENT_QUERY = "query ($code: ID!) { continent(code: $code) { countries { code }}}";

    private static final String QUERY_VAR = "{\"code\": \"%s\"}";

    public static ExternalApiRequestDto createRequestDto(
            ExternalApiRequestType type, String parameter) {
        ExternalApiRequestDto request = new ExternalApiRequestDto();
        if (type.equals(ExternalApiRequestType.CONTINENT_REQUEST))
            request.setQuery(GET_CONTINENT_QUERY);
        else {
            request.setQuery(GET_COUNTRY_QUERY);
        }
        request.setVariables(String.format(QUERY_VAR, parameter));

        return request;
    }
}
