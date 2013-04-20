package services;

import models.StatusUpdate;
import models.TrendingTerm;
import org.mongojack.*;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class FeedDao {
    private final JacksonDBCollection<StatusUpdate, String> coll;

    @Inject
    public FeedDao(MongoConnectionManager mongo) {
        this.coll = JacksonDBCollection.wrap(mongo.getDb().getCollection("feed"), StatusUpdate.class, String.class);
    }

    public List<StatusUpdate> getFeed(List<String> userIds) {
        return this.coll.find()
                .in("user", userIds)
                .sort(DBSort.desc("_id"))
                .limit(100).toArray();
    }

    public List<StatusUpdate> getFeed(String userId) {
        return this.coll.find()
                .is("user", userId)
                .sort(DBSort.desc("_id"))
                .limit(100).toArray();
    }

    public void save(StatusUpdate statusUpdate) {
        this.coll.save(statusUpdate);
    }

    public void like(String statusUpdateId, String userId) {
        this.coll.updateById(statusUpdateId, DBUpdate.addToSet("likes", userId));
    }

    public void unlike(String statusUpdateId, String userId) {
        this.coll.updateById(statusUpdateId, DBUpdate.pull("likes", userId));
    }

    public List<TrendingTerm> trending() {
        JacksonDBCollection<TrendingTerm, String> trending = this.coll.mapReduce(MapReduce.build(
            "function() {" +
            "  var terms = this.text.split(' ');" +
            "  for (var i in terms) {" +
            "    var term = /[\\w+#]+/.exec(terms[i])[0].toLowerCase();" +
            "    if (term.length > 2) {" +
            "      emit(term, 1);" +
            "    }" +
            "  }" +
            "}",
            "function(k, vals) {" +
            "  var count = 0;" +
            "  for (var i in vals) {" +
            "    count += vals[i];" +
            "  }" +
            "  return count;" +
            "}",
            MapReduce.OutputType.REPLACE,
            "trending",
            TrendingTerm.class,
            String.class
        ).setQuery(
                DBQuery.greaterThan("date", new Date(System.currentTimeMillis() - 84600000))
        )).getOutputCollection();
        return trending.find().sort(DBSort.desc("value")).toArray();
    }
}
