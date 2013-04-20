package services;

import com.google.inject.AbstractModule;
import play.Application;

public class PlayModule extends AbstractModule {
    private final Application application;

    public PlayModule(Application application) {
        this.application = application;
    }

    @Override
    protected void configure() {
        bind(Application.class).toInstance(application);
    }
}
