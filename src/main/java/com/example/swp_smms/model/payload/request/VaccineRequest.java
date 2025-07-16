package com.example.swp_smms.model.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VaccineRequest {
    private String name;
    private String type;
    private String version;
    private LocalDate releaseDate;
}
