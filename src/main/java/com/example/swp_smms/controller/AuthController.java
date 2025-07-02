package com.example.swp_smms.controller;

import com.example.swp_smms.model.payload.request.LoginRequest;
import com.example.swp_smms.model.payload.request.ChangePasswordRequest;
import com.example.swp_smms.model.exception.ResponseBuilder;
import com.example.swp_smms.service.AuthenticationService;
import com.example.swp_smms.service.AccountService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.response.AccountResponse;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@RestController
@OpenAPIDefinition(info = @Info(
        title = "SMMS REST API", version = "1.0", description = "API documentation for School Medical Management System",
        contact = @Contact(name = "Github", url = "https://github.com/your-repo")),
        security = {@SecurityRequirement(name = "bearerToken")}
)
@SecuritySchemes({
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
})
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication required to use other resources.")
public class AuthController {

    private final AuthenticationService authService;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Login in to the system", description = "Login into the system requires all information to be provided, " + "and validations will be performed. The response will include an access token and a refresh token")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Successfully Login", content = @Content(examples = @ExampleObject(value = """
                {
                   "http_status": 200,
                   "time_stamp": "10/29/2024 11:20:03",
                   "message": "Successfully SignIn",
                   "data": {
                     "access_token": "xxxx.yyyy.zzzz",
                     "refresh_token": "xxxx.yyyy.zzzz"
                }
            """))),})
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Object> SignIn(@RequestBody @Valid LoginRequest request) {
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Successfully Sign in", authService.authenticate(request));
    }

    @Operation(summary = "Refresh token if expired", description = "If the current JWT Refresh Token has expired or been revoked, you can refresh it using this method")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Generate new Refresh Token and Access Token successfully", content = @Content(examples = @ExampleObject(value = """
                {
                 "access_token": "xxxx.yyyy.zzzz",
                 "refresh_token": "xxxx.yyyy.zzzz"
               }
            """))), @ApiResponse(responseCode = "401", description = "No JWT token found in the request header"), @ApiResponse(responseCode = "401", description = "JWT token has expired and revoked")})
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Generate new Refresh Token and Access Token successfully", authService.refreshToken(request, response));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestParam String email) throws MessagingException, NoSuchAlgorithmException {
        authService.forgotPassword(email);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Please check your email for password reset link");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Object> resetPassword(@RequestParam(value = "Password") String password, @RequestParam String token) {
        authService.resetPassword(password, token);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Password reset successfully");
    }

    @Operation(summary = "Logout of the system", description = "Logout of the system, bearer token (refresh token) is required")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Logged out successfully"), @ApiResponse(responseCode = "401", description = "No JWT token found in the request header")})
    @PostMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        authService.logout(request);
        return ResponseBuilder.responseBuilder(HttpStatus.OK, "Logged out successfully");
    }

    @PutMapping("/change-password/{accountId}")
    public ResponseEntity<Object> changePassword(
            @PathVariable UUID accountId,
            @Valid @RequestBody ChangePasswordRequest request) {
        try {
            accountService.changePassword(accountId, request);
            return ResponseBuilder.responseBuilder(HttpStatus.OK, "Password changed successfully");
        } catch (Exception e) {
            return ResponseBuilder.responseBuilder(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/me")
    public Object getCurrentUser(@RequestBody TokenRequest tokenRequest) {
        if (tokenRequest == null || !StringUtils.hasText(tokenRequest.getToken())) {
            return ResponseBuilder.responseBuilder(HttpStatus.BAD_REQUEST, "No token provided");
        }
        String token = tokenRequest.getToken();
        String email = jwtTokenProvider.getUsernameFromJwt(token);
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Account not found for token"));
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setUsername(account.getUsername());
        response.setFullName(account.getFullName());
        response.setDob(account.getDob());
        response.setGender(account.getGender());
        response.setPhone(account.getPhone());
        response.setRoleId(account.getRole() != null ? account.getRole().getRoleId() : null);
        response.setEmail(account.getEmail());
        return ResponseBuilder.responseBuilderWithData(HttpStatus.OK, "Current user fetched successfully", response);
    }

    static class TokenRequest {
        private String token;
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
    }
} 