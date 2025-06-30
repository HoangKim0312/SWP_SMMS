package com.example.swp_smms.service.implement;

import com.example.swp_smms.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendAccountCredentials(String email, String firstName, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your School Medical Management System Account");
        message.setText(String.format(
            "Dear %s,\n\n" +
            "Your account has been created in the School Medical Management System.\n\n" +
            "Your login credentials are:\n" +
            "Email: %s\n" +
            "Password: %s\n\n" +
            "Please login and change your password for security reasons.\n\n" +
            "Best regards,\n" +
            "School Medical Management System Team",
            firstName, email, password
        ));

        mailSender.send(message);
    }

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
} 