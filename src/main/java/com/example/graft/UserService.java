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
        String safePass = encoder.encode(pass);
        var user = new User();
        user.setName(name);
        user.setUsername(uName);
        user.setPassword(safePass);
        user.setEmail(email);
        repo.save(user);
    }

    /*public void post(Long userID, String text) {
        User user = repo.findById(userID)
                .orElseThrow(() -> new RuntimeException("user missing for userID"));
        Post p = ps.post(text);
        user.addPost(p);
    }*/
}
