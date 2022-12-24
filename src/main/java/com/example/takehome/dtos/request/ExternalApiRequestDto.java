package com.example.takehome.dtos.request;

import lombok.Data;

@Data
public class ExternalApiRequestDto {

    private String query;

    private String variables;
}
