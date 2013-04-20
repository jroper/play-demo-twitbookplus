package controllers;

import play.*;
import play.core.Router;
import play.mvc.*;

import views.html.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Application extends Controller {
  
    public static Result index(String path) {
        return ok(index.render());
    }

    public static Result router() throws Exception {
        return ok(Routes.javascriptRouter("routes",
                routes.javascript.UserController.getUser(),
                routes.javascript.UserController.search(),
                routes.javascript.UserController.newUser(),
                routes.javascript.UserController.current(),
                routes.javascript.UserController.signIn(),
                routes.javascript.UserController.logOut(),
                routes.javascript.UserController.followUser(),
                routes.javascript.UserController.unfollowUser(),
                routes.javascript.FeedController.updateStatus(),
                routes.javascript.FeedController.getFeed(),
                routes.javascript.FeedController.getFeedForUser(),
                routes.javascript.FeedController.like(),
                routes.javascript.FeedController.unlike(),
                routes.javascript.FeedController.trending()
        )).as("text/javascript");
    }
}
