package com.example.graft;

public class UserProfileDTO {
    private Long id;
    private String username;
    private String name;
    private String bio;
    private String email;
    private Integer age;
    private String country;

    public UserProfileDTO(Long id, String username, String name, String bio, String email, Integer age, String country) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.bio = bio;
        this.email = email;
        this.age = age;
        this.country = country;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }
}
