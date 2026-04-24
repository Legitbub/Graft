import re

with open('src/main/java/com/example/graft/UserRepository.java', 'r') as f:
    content = f.read()

old_query = """    @Query("MATCH (:User {username: $username})-[:FOLLOWS]->(u:User)-[:FOLLOWS]->(:User {username: $username}) " +
           "RETURN toInteger(u.user_id) AS id, u.username AS username, u.name AS name, u.bio AS bio " +
           "ORDER BY toLower(u.username)")
    List<SearchUserDTO> findMutualUsers(String username);"""

new_query = """    @Query("MATCH (a:User {user_id: $userId1})-[:FOLLOWS]->(m:User)<-[:FOLLOWS]-(b:User {user_id: $userId2}) " +
           "RETURN toInteger(m.user_id) AS id, m.username AS username, m.name AS name, m.bio AS bio " +
           "ORDER BY toLower(m.username)")
    List<SearchUserDTO> findMutualFriends(Long userId1, Long userId2);"""

content = content.replace(old_query, new_query)

with open('src/main/java/com/example/graft/UserRepository.java', 'w') as f:
    f.write(content)

