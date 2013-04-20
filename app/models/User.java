package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableSet;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.HashSet;
import java.util.Set;

public class User {
    private final String id;
    private final String email;
    private final String name;
    private final String passwordHash;
    private final int updates;
    private final Set<String> following;
    private final Set<String> followers;

    @JsonCreator
    public User(@ObjectId @Id String id,
                @JsonProperty("email") String email,
                @JsonProperty("name") String name,
                @JsonProperty("passwordHash") String passwordHash,
                @JsonProperty("updates") int updates,
                @ObjectId @JsonProperty("following") Set<String> following,
                @ObjectId @JsonProperty("followers") Set<String> followers) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.passwordHash = passwordHash;
        this.updates = updates;
        this.following = following;
        this.followers = followers;
    }

    public User(String email, String name, String passwordHash) {
        this(null, email, name, passwordHash, 0, new HashSet<String>(), new HashSet<String>());
    }

    @ObjectId
    @Id
    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getUpdates() {
        return updates;
    }

    @ObjectId
    public Set<String> getFollowing() {
        return following;
    }

    @ObjectId
    public Set<String> getFollowers() {
        return followers;
    }
}
