package com.example.swp_smms.model.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request object for changing password")
public class ChangePasswordRequest {

    @Schema(description = "User's current password", example = "Password1")
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @Schema(description = "User's new password", example = "Password1")
    @NotBlank(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    @Schema(description = "User's confirm password", example = "Password1")
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
} 