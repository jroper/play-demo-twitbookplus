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
        return ok(getJavascriptReverseRouter()).as("text/javascript");
    }


    private static String javascriptReverseRouter;

    private static String getJavascriptReverseRouter() throws Exception {
        if (javascriptReverseRouter == null) {
            List<Router.JavascriptReverseRoute> javascriptRoutes = new ArrayList<>();
            javascriptRoutes.addAll(scanRouter(routes.javascript.UserController));
            javascriptRoutes.addAll(scanRouter(routes.javascript.FeedController));
            javascriptReverseRouter = Routes.javascriptRouter("routes", javascriptRoutes.toArray(new Router.JavascriptReverseRoute[0]));
        }
        return javascriptReverseRouter;
    }

    private static List<Router.JavascriptReverseRoute> scanRouter(Object router) throws Exception {
        List<Router.JavascriptReverseRoute> routes = new ArrayList<>();
        for (Method method: router.getClass().getMethods()) {
            if (method.getParameterTypes().length == 0 &&
                    Router.JavascriptReverseRoute.class.isAssignableFrom(method.getReturnType())) {
                routes.add(Router.JavascriptReverseRoute.class.cast(method.invoke(router)));
            }
        }
        return routes;
    }

}
