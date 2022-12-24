package com.example.takehome.dtos.response;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CountryResponseDto {
    List<CountryItemResponseDto> continents;
}
