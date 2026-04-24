package com.example.graft;

public class SearchUserDTO {
    private Long id;
    private String username;
    private String name;
    private String bio;

    public SearchUserDTO(Long id, String username, String name, String bio) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.bio = bio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
