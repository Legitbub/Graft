package com.example.graft;

public class RecommendationUserDTO {
    private Long id;
    private String username;
    private String name;
    private Long mutualConnectionScore;

    public RecommendationUserDTO(Long id, String username, String name, Long mutualConnectionScore) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.mutualConnectionScore = mutualConnectionScore;
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

    public Long getMutualConnectionScore() {
        return mutualConnectionScore;
    }

    public void setMutualConnectionScore(Long mutualConnectionScore) {
        this.mutualConnectionScore = mutualConnectionScore;
    }
}
