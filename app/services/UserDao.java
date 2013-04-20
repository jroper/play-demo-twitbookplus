package services;

import models.User;
import org.mongojack.DBQuery;
import org.mongojack.DBUpdate;
import org.mongojack.JacksonDBCollection;
import play.Application;
import play.modules.mongojack.MongoDBPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Singleton
public class UserDao {

    private final JacksonDBCollection<User, String> coll;

    @Inject
    public UserDao(Application app) {
        this.coll = app.plugin(MongoDBPlugin.class).getCollection("users", User.class, String.class);
    }

    public User get(String id) {
        return coll.findOneById(id);
    }

    public List<User> get(Collection<String> ids) {
        return coll.find().in("_id", ids).toArray();
    }

    public User findByEmail(String email) {
        return coll.findOne(DBQuery.is("email", email));
    }

    public List<User> search(String term) {
        return coll.find().regex("name", Pattern.compile("^" + Pattern.quote(term))).toArray();
    }

    public User create(User user) {
        coll.save(user);
        return findByEmail(user.getEmail());
    }

    public void follow(String id, String toFollow) {
        coll.updateById(id, DBUpdate.addToSet("following", toFollow));
        coll.updateById(toFollow, DBUpdate.addToSet("followers", id));
    }

    public void unfollow(String id, String toFollow) {
        coll.updateById(id, DBUpdate.pull("following", toFollow));
        coll.updateById(toFollow, DBUpdate.pull("followers", id));
    }

    public void incrementUpdates(String id) {
        coll.updateById(id, DBUpdate.inc("updates"));
    }

}
