package services;

import com.mongodb.DB;
import com.mongodb.Mongo;

import javax.inject.Singleton;

@Singleton
public class MongoConnectionManager {
    private final DB db;

    public MongoConnectionManager() throws Exception {
        db = new Mongo().getDB("play");
    }

    public DB getDb() {
        return db;
    }
}
