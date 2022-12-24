package com.example.takehome.models;

import lombok.Data;

@Data
public class CountryModel {

    private String code;

    private ContinentModel continent;
}
