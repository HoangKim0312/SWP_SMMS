package com.example.swp_smms.security;

import com.example.swp_smms.model.entity.Account;
import com.example.swp_smms.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email: " + email));

        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole().getRoleName());

        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(authority);

        return new org.springframework.security.core.userdetails.User(account.getEmail(), account.getPassword(), authorities);
    }
} 