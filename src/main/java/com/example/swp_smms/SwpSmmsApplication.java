package com.example.swp_smms;

import com.example.swp_smms.service.DataInitializationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SwpSmmsApplication {
    //test by hiep
    public static void main(String[] args) {
        SpringApplication.run(SwpSmmsApplication.class, args);
    }

    @Bean
    public CommandLineRunner initializeData(DataInitializationService dataInitializationService) {
        return args -> {
            dataInitializationService.initializeRoles();
        };
    }
}
