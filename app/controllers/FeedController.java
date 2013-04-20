package controllers;

import models.StatusUpdate;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import services.FeedDao;
import services.UserDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class FeedController extends Controller {

    private final FeedDao feedDao;
    private final UserDao userDao;

    @Inject
    public FeedController(FeedDao feedDao, UserDao userDao) {
        this.feedDao = feedDao;
        this.userDao = userDao;
    }

    @Security.Authenticated
    public Result getFeed() {
        // Get followers
        List<String> following = new ArrayList<>(userDao.get(request().username()).getFollowing());
        following.add(request().username());

        // Get updates
        List<StatusUpdate> updates = feedDao.getFeed(following);

        // Create map of user ids to users
        List<UserController.UserView> users = UserController.UserView.fromUsers(userDao.get(following));
        Map<String, UserController.UserView> userMap = new HashMap<>();
        for (UserController.UserView user: users) {
            userMap.put(user.id, user);
        }

        // Create feed
        List<StatusUpdateView> feed = new ArrayList<>();
        for (StatusUpdate update: updates) {
            StatusUpdateView view = StatusUpdateView.fromStatusUpdate(update);
            view.user = userMap.get(update.getUser());
            feed.add(view);
        }
        return ok(Json.toJson(feed));
    }

    public Result getFeedForUser(String id) {
        List<StatusUpdateView> feed = new ArrayList<>();
        UserController.UserView userView = UserController.UserView.fromUser(userDao.get(id));

        for (StatusUpdate update: feedDao.getFeed(id)) {
            StatusUpdateView view = StatusUpdateView.fromStatusUpdate(update);
            view.user = userView;
            feed.add(view);
        }

        return ok(Json.toJson(feed));
    }

    @Security.Authenticated
    public Result like(String id) {
        feedDao.like(id, request().username());
        return noContent();
    }

    @Security.Authenticated
    public Result unlike(String id) {
        feedDao.unlike(id, request().username());
        return noContent();
    }

    @Security.Authenticated
    public Result updateStatus() {
        String text = request().body().asJson().get("status").asText();
        feedDao.save(new StatusUpdate(request().username(), text));
        return noContent();
    }

    public Result trending() {
        return ok(Json.toJson(feedDao.trending()));
    }

    public static class StatusUpdateView {
        public String id;
        public UserController.UserView user;
        public Date date;
        public String text;
        public List<String> likes;

        public static StatusUpdateView fromStatusUpdate(StatusUpdate update) {
            StatusUpdateView view = new StatusUpdateView();
            view.id = update.getId();
            view.date = update.getDate();
            view.text = update.getText();
            view.likes = update.getLikes();
            return view;
        }
    }
}
