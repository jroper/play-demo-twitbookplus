package services;

import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import play.Application;
import play.api.Plugin;
import scala.collection.JavaConversions;

import java.util.*;

public class PlayModule extends AbstractModule {
    private final Application application;

    public PlayModule(Application application) {
        this.application = application;
    }

    @Override
    protected void configure() {
        bind(Application.class).toInstance(application);

        Map<Class, Set<Class>> allPlugins = new HashMap<>();

        for (Plugin plugin: (Iterable<Plugin>) JavaConversions.asJavaIterable(application.getWrappedApplication().plugins())) {
            allPlugins.put(plugin.getClass(), findAllPluginSuperTypes(plugin.getClass()));
        }

        Set<Class> allPluginTypes = new HashSet<>();
        Set<Class> duplicatePluginTypes = new HashSet<>();
        // Find duplicates
        for (Set<Class> classes: allPlugins.values()) {
            duplicatePluginTypes.addAll(Sets.union(allPluginTypes, classes));
            allPluginTypes.addAll(classes);
        }

        // Register all plugins
        for (Map.Entry<Class, Set<Class>> classes: allPlugins.entrySet()) {
            Object plugin = application.plugin(classes.getKey());
            for (Class superType: classes.getValue()) {
                bind(superType).toInstance(plugin);
            }
        }
    }

    private Set<Class> findAllPluginSuperTypes(Class target) {
        Set<Class> superTypes = new HashSet<>();
        while (Plugin.class.isAssignableFrom(target)) {
            superTypes.add(target.asSubclass(Plugin.class));
            for (Class inter: target.getInterfaces()) {
                if (Plugin.class.isAssignableFrom(inter)) {
                    superTypes.add(target.asSubclass(Plugin.class));
                }
            }
            target = target.getSuperclass();
        }
        return superTypes;
    }
}
