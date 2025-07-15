package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class VaccineRequest {
    private String name;
    private String type;
    private String version;
    private String releaseDate;
}
