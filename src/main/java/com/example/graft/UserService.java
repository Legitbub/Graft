package com.example.graft;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordEncoder encoder;
    //PostService ps;

    public UserService(UserRepository repo, PasswordEncoder encoder/*, PostService ps*/) {
        this.repo = repo;
        this.encoder = encoder;
        //this.ps = ps;
    }

    public void createUser(String name, String uName, String pass, String email) {
        validateUniqueUser(uName, email);
        String safePass = encoder.encode(pass);
        var user = new User();
        user.setUserId(repo.findNextUserId());
        user.setName(name);
        user.setUsername(uName);
        user.setPassword(safePass);
        user.setEmail(email);
        repo.save(user);
    }

    public void createUser(String name, String uName, String pass, String email, 
                          Integer age, String bio, String country) {
        validateUniqueUser(uName, email);
        String safePass = encoder.encode(pass);
        var user = new User();
        user.setUserId(repo.findNextUserId());
        user.setName(name);
        user.setUsername(uName);
        user.setPassword(safePass);
        user.setEmail(email);
        user.setAge(age);
        if (bio != null && !bio.isEmpty()) {
            user.setBio(bio);
        }
        user.setCountry(country);
        repo.save(user);
    }

    private void validateUniqueUser(String username, String email) {
        String normalizedUsername = username != null ? username.trim() : "";
        String normalizedEmail = email != null ? email.trim() : "";

        if (repo.findByUsername(normalizedUsername).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        if (repo.findByEmail(normalizedEmail).isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }
    }

    /*public void post(Long userID, String text) {
        User user = repo.findById(userID)
                .orElseThrow(() -> new RuntimeException("user missing for userID"));
        Post p = ps.post(text);
        user.addPost(p);
    }*/
}
