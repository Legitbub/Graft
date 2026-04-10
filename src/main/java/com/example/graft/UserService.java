package com.example.graft;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public void createUser(String name) {
        var user = new User();
        user.setName(name);
        repo.save(user);
    }
}
