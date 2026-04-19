package com.example.graft.authentication;

import com.example.graft.User;
import com.example.graft.UserRepository;
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
        User user = repo.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("Invalid user"));
        System.out.println("DEBUG - Fetched password from Neo4j: " + user.getPassword());
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername()).password(user.getPassword())
                .roles("USER").build();
    }
}
