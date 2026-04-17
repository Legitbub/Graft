package com.example.graft;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountDetailsService implements UserDetailsService {
    private final UserRepository repo;

    public AccountDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByName(username).orElseThrow(() ->
                new UsernameNotFoundException("Invalid user"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getName()).password(user.getPassword())
                .roles("USER").build();
    }
}
