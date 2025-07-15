// src/main/java/com/example/swp_smms/model/payload/request/AcceptMedicationSentRequest.java
package com.example.swp_smms.model.payload.request;

import lombok.Data;

@Data
public class AcceptMedicationSentRequest {
    private Boolean isAccepted; // true or false
}
