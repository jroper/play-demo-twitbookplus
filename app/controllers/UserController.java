package controllers;

import models.User;
import org.apache.commons.codec.digest.DigestUtils;
import play.libs.Crypto;
import play.mvc.Security;
import services.UserDao;
import org.apache.commons.codec.binary.Base64;
import org.mindrot.jbcrypt.BCrypt;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserController extends Controller {
    private final UserDao userDao;

    @Inject
    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    public Result getUser(String id) {
        User user = userDao.get(id);
        if (user == null) {
            return notFound();
        }
        List<User> following = userDao.get(user.getFollowing());
        List<User> followers = userDao.get(user.getFollowers());
        UserView view = UserView.fromUser(user);
        view.followers = UserView.fromUsers(followers);
        view.following = UserView.fromUsers(following);
        return ok(Json.toJson(view));
    }

    public Result newUser() {
        UserSignup signup = Json.fromJson(request().body().asJson(), UserSignup.class);
        if (userDao.findByEmail(signup.email) != null) {
            return badRequest();
        }
        User user = new User(signup.email, signup.name, BCrypt.hashpw(signup.password, BCrypt.gensalt()));
        User newUser = userDao.create(user);
        session().put("username", newUser.getId());
        response().setHeader(LOCATION, routes.UserController.getUser(newUser.getId()).url());
        return created();
    }

    public Result signIn() {
        UserSignup signup = Json.fromJson(request().body().asJson(), UserSignup.class);
        User user = userDao.findByEmail(signup.email);
        if (user == null) {
            return notFound();
        }
        if (BCrypt.checkpw(signup.password, user.getPasswordHash())) {
            session().put("username", user.getId());
            return getUser(user.getId());
        } else {
            return notFound();
        }
    }

    @Security.Authenticated
    public Result current() {
        return getUser(request().username());
    }

    public Result search(String term) {
        return ok(Json.toJson(UserView.fromUsers(userDao.search(term))));
    }

    public Result logOut() {
        session().clear();
        return ok();
    }

    @Security.Authenticated
    public Result followUser(String id) {
        if (userDao.get(id) == null) {
            return notFound();
        }
        userDao.follow(request().username(), id);
        return ok();
    }

    @Security.Authenticated
    public Result unfollowUser(String id) {
        userDao.unfollow(request().username(), id);
        return ok();
    }

    public static class UserSignup {
        public String name;
        public String email;
        public String password;
    }

    public static class UserView {
        public String id;
        public String name;
        public String gravatar;
        public int updates;
        public List<UserView> followers;
        public List<UserView> following;

        static List<UserView> fromUsers(List<User> users) {
            List<UserView> views = new ArrayList<UserView>(users.size());
            for (User user: users) {
                views.add(fromUser(user));
            }
            return views;
        }

        static UserView fromUser(User user) {
            UserView view = new UserView();
            view.id = user.getId();
            view.name = user.getName();
            view.gravatar = "//www.gravatar.com/avatar/" + DigestUtils.md5Hex(user.getEmail());
            view.updates = user.getUpdates();
            return view;
        }
    }

}

