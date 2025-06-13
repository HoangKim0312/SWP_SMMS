package com.example.swp_smms.service.implement;

import com.example.swp_smms.model.entity.AccessToken;
import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.entity.PasswordResetToken;
import com.example.swp_smms.model.entity.RefreshToken;
import com.example.swp_smms.model.exception.SmmsException;
import com.example.swp_smms.model.payload.request.LoginRequest;
import com.example.swp_smms.model.payload.response.AuthResponse;
import com.example.swp_smms.repository.AccessTokenRepository;
import com.example.swp_smms.repository.AccountRepository;
import com.example.swp_smms.repository.PasswordResetTokenRepository;
import com.example.swp_smms.repository.RefreshTokenRepository;
import com.example.swp_smms.security.JwtTokenProvider;
import com.example.swp_smms.service.AuthenticationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final static Logger logger = org.slf4j.LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AccountRepository accountRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final PasswordResetTokenRepository resetPasswordTokenRepository;
    
    @Value("${application.email.url:http://localhost:3000/reset-password}")
    private String forgotPasswordUrl;

    @Value("${application.email.secure.characters:ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789}")
    private String emailSecureChar;

    @Override
    public AuthResponse authenticate(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Account account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new SmmsException(HttpStatus.UNAUTHORIZED, "Authentication fails, your email is not exist"));

        if (account.isLocked()) {
            throw new SmmsException(HttpStatus.UNAUTHORIZED, "Authentication fails, your account is locked. Please contact admin");
        }

        //Create new token
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Terminate all existing tokens
        revokeRefreshTokens(accessToken);
        revokeAccessTokensFromUser(account);

        //Save new token
        saveAccountToken(account, accessToken, refreshToken);

        return AuthResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeRefreshTokens(String accessToken) {
        AccessToken token = accessTokenRepository.findByToken(accessToken);
        if (token != null) {
            //Terminate old refreshToken
            RefreshToken refreshToken = token.getRefreshToken();
            refreshToken.setRevoked(true);
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);
        }
    }

    private void saveAccountToken(Account account, String accessToken, String refreshToken) {
        RefreshToken newRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .revoked(false)
                .expired(false)
                .build();

        refreshTokenRepository.save(newRefreshToken);

        AccessToken newAccessToken = AccessToken.builder()
                .token(accessToken)
                .account(account)
                .refreshToken(newRefreshToken)
                .revoked(false)
                .expired(false)
                .build();

        accessTokenRepository.save(newAccessToken);
    }

    private void revokeAccessTokensFromUser(Account account) {
        List<AccessToken> existingToken = accessTokenRepository.findAllValidTokensByUser(account.getAccountId());
        if (!existingToken.isEmpty()) {
            existingToken.forEach(token -> {
                token.setRevoked(true);
                token.setExpired(true);
                accessTokenRepository.save(token);
            });
        }
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SmmsException(HttpStatus.UNAUTHORIZED, "No jwt Token found in the request header");
        }

        jwtToken = authHeader.substring(7);

        AccessToken token = accessTokenRepository.findByToken(jwtToken);
        if (token != null) {
            Account account = token.getAccount();
            revokeAccessTokensFromUser(account);
            revokeRefreshTokens(jwtToken);
        }
    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new SmmsException(HttpStatus.UNAUTHORIZED, "No jwt Token found in the request header");
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtTokenProvider.getUsernameFromJwt(refreshToken);

        //validate
        if (userEmail != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            RefreshToken curRefreshToken = refreshTokenRepository.findByToken(refreshToken)
                    .orElseThrow(() -> new SmmsException(HttpStatus.BAD_REQUEST, "Token is invalid or not exist"));
            if (!curRefreshToken.isRevoked() && !curRefreshToken.isExpired()) {
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                Account account = accountRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new SmmsException(HttpStatus.BAD_REQUEST, "Invalid user. User not found"));

                //Revoke old tokens
                revokeAccessTokensFromUser(account);

                curRefreshToken.setRevoked(true);
                curRefreshToken.setExpired(true);
                refreshTokenRepository.save(curRefreshToken);

                //generate new token
                String newAccessToken = jwtTokenProvider.generateAccessToken(authentication);
                String newRefreshToken = jwtTokenProvider.generateRefreshToken(authentication);

                saveAccountToken(account, newAccessToken, newRefreshToken);

                return AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();

            } else {
                throw new SmmsException(HttpStatus.BAD_REQUEST, "Token is invalid or not exist");
            }
        }

        return null;
    }

    @Override
    public void forgotPassword(String email) throws NoSuchAlgorithmException, MessagingException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found"));

        String token = generateResetPasswordToken(32);
        StringBuilder link = new StringBuilder();
        link.append(forgotPasswordUrl).append("/").append(token);
        
        // TODO: Implement email service
        // emailService.sendMimeMessageWithHtml(
        //         account.getFullName(),
        //         account.getEmail(),
        //         link.toString(),
        //         EmailTemplateName.FORGOT_PASSWORD.getName(),
        //         "Reset your password");

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .expired(LocalDateTime.now().plusMinutes(30))
                .revoked(false)
                .account(account)
                .build();

        resetPasswordTokenRepository.save(passwordResetToken);
    }

    @Override
    public void resetPassword(String newPassword, String token) {
        PasswordResetToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new SmmsException(HttpStatus.BAD_REQUEST, "Token not found"));

        if (resetPasswordToken.isRevoked()) {
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Token is invalid or revoked");
        }

        if (resetPasswordToken.getExpired().isBefore(LocalDateTime.now())) {
            resetPasswordToken.setRevoked(true);
            resetPasswordTokenRepository.save(resetPasswordToken);
            throw new SmmsException(HttpStatus.BAD_REQUEST, "Token is expired");
        }

        Account account = accountRepository.findById(resetPasswordToken.getAccount().getAccountId())
                .orElseThrow(() -> new SmmsException(HttpStatus.BAD_REQUEST, "Account not found"));

        account.setPassword(passwordEncoder.encode(newPassword));
        accountRepository.save(account);
        resetPasswordToken.setRevoked(true);
        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    private String generateResetPasswordToken(int codelength) throws NoSuchAlgorithmException {
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();
        //lay ngau nhien 1 ki tu trong chuoi emailSecurecHar
        for (int i = 0; i < codelength; i++) {
            int randomIndex = random.nextInt(emailSecureChar.length());
            codeBuilder.append(emailSecureChar.charAt(randomIndex));
        }

        //Hash token with SHA-256
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(codeBuilder.toString().getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    @Scheduled(cron = "1 * * * * ?")
    public void revokedExpiredPasswordResetTokens() {
        logger.info("Detecting mark expired password reset tokens task started");
        List<PasswordResetToken> expiredTokens = resetPasswordTokenRepository.findAllByExpiredBefore(LocalDateTime.now());
        logger.info("Found {} expired password reset tokens", expiredTokens.size());
        if (!expiredTokens.isEmpty()) {
            for (PasswordResetToken token : expiredTokens) {
                token.setRevoked(true);
            }
            resetPasswordTokenRepository.saveAll(expiredTokens);
            logger.info("Marked {} expired password reset tokens as revoked", expiredTokens.size());
        }
        logger.info("Detecting mark expired password reset tokens task finished");
    }

    @Scheduled(cron = "1 * * * * ?")
    @Transactional
    public void scheduledDeleteExpiredToken() {
        logger.info("Detecting deleting expired reset password tokens task started");
        resetPasswordTokenRepository.deleteAllByRevoked(true);
        logger.info("Detecting deleting expired reset password tokens task finished");
    }
} 