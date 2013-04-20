package services;

import models.StatusUpdate;
import models.TrendingTerm;
import models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class MockObjects {

    public static final String foobarHash = "$2a$10$32rmuAF9yk3g1KGVCdaL5uJOO47cVnnPpOCBS12EuJ6sAQ75qgXcS";
    public static final String bobId = "516e0d99300493c7fc86dd88";
    public static final String janeId = "516e0d99300493c7fc86dd89";
    public static final String jackId = "516e0d99300493c7fc86dd90";


    public static User bob = new User(bobId, "bob@example.com", "Bob", foobarHash, 3,
            new HashSet<>(Arrays.asList(janeId, jackId)), new HashSet<>(Arrays.asList(janeId)));
    public static User jane = new User(janeId, "jane@example.com", "Jane", foobarHash, 0,
            new HashSet<>(Arrays.asList(bobId)), new HashSet<>(Arrays.asList(bobId)));
    public static User jack = new User(jackId, "jack@example.com", "Jack", foobarHash, 0,
            new HashSet<String>(), new HashSet<String>());

    public static StatusUpdate status1 = new StatusUpdate("516e0d99300493c7fc86dd91", bobId, new Date(), "This is a status update.", Arrays.asList(janeId));
    public static StatusUpdate status2 = new StatusUpdate("516e0d99300493c7fc86dd92", janeId, new Date(), "This is another status update.", Arrays.asList(bobId));
    public static StatusUpdate status3 = new StatusUpdate("516e0d99300493c7fc86dd93", bobId, new Date(), "Mock data is boring.", new ArrayList<String>());
    public static StatusUpdate status4 = new StatusUpdate("516e0d99300493c7fc86dd94", jackId, new Date(), "I don't have any followers.", new ArrayList<String>());

    public static TrendingTerm term1 = new TrendingTerm("#gids2013", 20);
    public static TrendingTerm term2 = new TrendingTerm("mongodb", 18);
    public static TrendingTerm term3 = new TrendingTerm("java", 5);
}
