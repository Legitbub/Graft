package com.example.graft;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends Neo4jRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

    @Query("OPTIONAL MATCH (u:User) RETURN coalesce(max(toInteger(u.user_id)), -1) + 1")
    Long findNextUserId();

    @Query("MATCH (u:User) WHERE toString(u.user_id) = toString($userId) RETURN u")
    Optional<User> findByUserId(Long userId);

    @Query("MATCH (u:User) WHERE toString(u.user_id) = toString($userId) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio, u.email AS email, u.age AS age, u.country AS country")
    Optional<UserProfileDTO> findUserProfileByUserId(Long userId);

    @Query("MATCH (u:User {username: $username}) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio, u.email AS email, u.age AS age, u.country AS country")
    Optional<UserProfileDTO> findUserProfileByUsername(String username);

    @Query("MATCH (u:User) " +
           "OPTIONAL MATCH (u)<-[:FOLLOWS]-(f:User) " +
           "WITH u, COUNT(f) AS followerCount " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, followerCount " +
           "ORDER BY followerCount DESC LIMIT $limit")
    List<PopularUserDTO> findPopularUsersByLimit(int limit);

    @Query("MATCH (u:User {username: $username}) RETURN toInteger(u.user_id) AS id")
    Optional<Long> findUserIdByUsername(String username);

    @Query("MATCH (a:User {user_id: $followerUserId})-[:FOLLOWS]->(b:User {user_id: $followeeUserId}) RETURN COUNT(*) > 0")
    boolean isFollowing(Long followerUserId, Long followeeUserId);

    @Query("MATCH (a:User {user_id: $followerUserId}) " +
           "MATCH (b:User {user_id: $followeeUserId}) " +
           "MERGE (a)-[:FOLLOWS]->(b) " +
           "RETURN count(*)")
    Long followUser(Long followerUserId, Long followeeUserId);

    @Query("MATCH (a:User {user_id: $followerUserId})-[r:FOLLOWS]->(b:User {user_id: $followeeUserId}) " +
           "WITH r " +
           "DELETE r " +
           "RETURN count(r)")
    Long unfollowUser(Long followerUserId, Long followeeUserId);

    @Query("MATCH (u:User {user_id: $userId})-[:FOLLOWS]->(:User)-[:FOLLOWS]->(rec:User) " +
           "WHERE NOT (u)-[:FOLLOWS]->(rec) AND u <> rec " +
           "RETURN toInteger(rec.user_id) AS id, rec.username AS username, rec.name AS name, COUNT(*) AS mutualConnectionScore " +
           "ORDER BY mutualConnectionScore DESC LIMIT $limit")
    List<RecommendationUserDTO> findFriendRecommendationsByUserId(Long userId, int limit);

    @Query("MATCH (u:User) " +
           "WHERE toLower(u.username) CONTAINS toLower($searchTerm) " +
           "OR toLower(u.name) CONTAINS toLower($searchTerm) " +
           "OR replace(toLower(u.username), ' ', '') CONTAINS toLower($searchTermNoSpace) " +
           "OR replace(toLower(u.name), ' ', '') CONTAINS toLower($searchTermNoSpace) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio")
    List<SearchUserDTO> searchUsersComprehensive(String searchTerm, String searchTermNoSpace);

    @Query("MATCH (:User {username: $username})-[:FOLLOWS]->(u:User) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio " +
           "ORDER BY toLower(u.username)")
    List<SearchUserDTO> findFollowingUsers(String username);

    @Query("MATCH (:User {username: $username})<-[:FOLLOWS]-(u:User) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio " +
           "ORDER BY toLower(u.username)")
    List<SearchUserDTO> findFollowerUsers(String username);

    @Query("MATCH (a:User)-[:FOLLOWS]->(m:User)<-[:FOLLOWS]-(b:User) WHERE toString(a.user_id) = toString($userId1) AND toString(b.user_id) = toString($userId2) " +
           "RETURN toInteger(m.user_id) AS id, m.username AS username, m.name AS name, m.bio AS bio " +
           "ORDER BY toLower(m.username)")
    List<SearchUserDTO> findMutualFriends(Long userId1, Long userId2);

    @Query("MATCH (u:User) WHERE toString(u.user_id) = toString($userId) OPTIONAL MATCH (u)<-[:FOLLOWS]-(f:User) RETURN COUNT(f)")
    Long countFollowers(Long userId);
}
