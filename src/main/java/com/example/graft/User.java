package com.example.graft;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Data
@Node("User")
public class User {
    @Id
    @GeneratedValue
    private Long ID;
    private String email;
    private String name;
    private String bio;
    private String password;

    // Users have a list of friends
    @Relationship
    private List<User> friends = new ArrayList<>();

    //May add post functionality in the future
    /*@Relationship(type = "POSTED", direction = Relationship.Direction.OUTGOING)
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post p) {
        posts.add(p);
    }*/

    public void addFriend(User f) {
        friends.add(f);
    }
}
