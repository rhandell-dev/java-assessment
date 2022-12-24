package com.example.takehome.dtos.response;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountryItemResponseDto {

    private List<String> countries;

    private String name;

    private List<String> otherCountries;
}
