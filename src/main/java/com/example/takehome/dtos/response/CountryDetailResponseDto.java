package com.example.takehome.dtos.response;

import com.example.takehome.models.CountryModel;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName("data")
public class CountryDetailResponseDto {
    private CountryModel country;
}
