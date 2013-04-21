package services;

import models.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static services.MockObjects.*;

@Singleton
public class UserDao {

    @Inject
    public UserDao(MongoConnectionManager mongo) {
    }

    public User get(String id) {
        return bob;
    }

    public User create(User user) {
        return bob;
    }

    public User findByEmail(String email) {
        return bob;
    }

    public List<User> get(Collection<String> ids) {
        return Arrays.asList(bob, jane, jack);
    }

    public List<User> search(String term) {
        return Arrays.asList(jane, jack);
    }

    public void follow(String id, String toFollow) {
    }

    public void unfollow(String id, String toFollow) {
    }

    public void incrementUpdates(String id) {
    }

}
