package models;

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

    public User(
            String id,
            String email,
            String name,
            String passwordHash,
            int updates,
            Set<String> following,
            Set<String> followers) {
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

    public Set<String> getFollowing() {
        return following;
    }

    public Set<String> getFollowers() {
        return followers;
    }
}
