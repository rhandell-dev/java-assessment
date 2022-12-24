package com.example.takehome.dtos.response;

import com.example.takehome.models.ContinentModel;
import com.fasterxml.jackson.annotation.JsonRootName;

import lombok.Data;

@Data
@JsonRootName("data")
public class ContinentDetailResponseDto {
    ContinentModel continent;
}
