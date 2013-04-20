package services;

import models.TrendingTerm;
import org.mongojack.DBSort;
import org.mongojack.JacksonDBCollection;
import play.Application;
import play.modules.mongojack.MongoDBPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class TrendingDao {

    private final JacksonDBCollection<TrendingTerm, String> coll;

    @Inject
    public TrendingDao(Application app) {
        this.coll = app.plugin(MongoDBPlugin.class).getCollection("trending", TrendingTerm.class, String.class);
    }

    public List<TrendingTerm> getTrendingTerms() {
        return coll.find().sort(DBSort.desc("value")).toArray();
    }

}
