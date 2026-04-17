/*
package com.example.graft;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PostService {
    private final PostRepository repo;

    public PostService(PostRepository repo) {
        this.repo = repo;
    }

    public Post post(String text) {
        Post p = new Post();
        p.setText(text);
        p.setTimestamp(LocalDateTime.now());
        return repo.save(p);
    }
}
*/
