package com.example.swp_smms.config;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.model.payload.request.AccountRequest;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Create a PropertyMap to skip accountId and manually map roleId
        modelMapper.addMappings(new PropertyMap<AccountRequest, Account>() {
            @Override
            protected void configure() {
                skip(destination.getAccountId());  // Skip mapping accountId
                // Optionally, map other fields manually here if needed
            }
        });

        return modelMapper;
    }
}
