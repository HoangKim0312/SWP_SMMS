package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class ClassRequest {
    private String className;
    private String description;
    private int schoolYear;
    private int grade;
}
