package com.example.graft;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    PostService ps;

    public UserService(UserRepository repo, PostService ps) {
        this.repo = repo;
        this.ps = ps;
    }

    public void createUser(String name) {
        var user = new User();
        user.setName(name);
        repo.save(user);
    }

    public void post(Long userID, String text) {
        User user = repo.findById(userID)
                .orElseThrow(() -> new RuntimeException("user missing for userID"));
        Post p = ps.post(text);
        user.addPost(p);
    }
}
