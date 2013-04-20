package services;

import models.StatusUpdate;
import models.TrendingTerm;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static services.MockObjects.*;

public class FeedDao {

    @Inject
    public FeedDao(MongoConnectionManager mongo) {
    }

    public List<StatusUpdate> getFeed(List<String> userIds) {
        return Arrays.asList(status1, status2, status3, status4);
    }

    public List<StatusUpdate> getFeed(String userId) {
        return Arrays.asList(status1, status3);
    }

    public void save(StatusUpdate statusUpdate) {
    }

    public void like(String statusUpdateId, String userId) {
    }

    public void unlike(String statusUpdateId, String userId) {
    }

    public List<TrendingTerm> trending() {
        return Arrays.asList(term1, term2, term3);
    }
}
