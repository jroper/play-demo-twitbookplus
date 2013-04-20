import com.google.inject.Guice;
import com.google.inject.Injector;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;
import scala.concurrent.duration.Duration;
import services.FeedDao;
import services.PlayModule;

import java.util.concurrent.TimeUnit;

public class Global extends GlobalSettings {
    private Application application;
    private Injector injector;

    @Override
    public void onStart(Application application) {
        this.application = application;
        injector = Guice.createInjector(new PlayModule(application));
        Akka.system().scheduler().schedule(Duration.create(1, TimeUnit.SECONDS), Duration.create(1, TimeUnit.MINUTES), new Runnable() {
            @Override
            public void run() {
                injector.getInstance(FeedDao.class).indexTrendingTerms();
            }
        }, Akka.system().dispatcher());
    }

    @Override
    public void onStop(Application application) {
        this.application = null;
        this.injector = null;
    }

    @Override
    public <A> A getControllerInstance(Class<A> aClass) throws Exception {
        return injector.getInstance(aClass);
    }
}
