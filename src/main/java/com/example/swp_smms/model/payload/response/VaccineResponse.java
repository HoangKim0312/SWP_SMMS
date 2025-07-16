package com.example.swp_smms.model.payload.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineResponse {
    private Long vaccineId;
    private String name;
    private String type;
    private String version;
    private LocalDate releaseDate;
}
