package com.example.takehome.models;

import java.util.List;

import lombok.Data;

@Data
public class ContinentModel {
    private String code;

    private List<CountryModel> countries;

    private String name;
}
