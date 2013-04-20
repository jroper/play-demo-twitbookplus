package models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;
import org.mongojack.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusUpdate {
    private final String id;
    private final String user;
    private final Date date;
    private final String text;
    private final List<String> likes;

    @JsonCreator
    public StatusUpdate(
            @Id @ObjectId String id,
            @JsonProperty("user") @ObjectId String user,
            @JsonProperty("date") Date date,
            @JsonProperty("text") String text,
            @JsonProperty("likes") @ObjectId List<String> likes) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.text = text;
        this.likes = likes;
    }

    public StatusUpdate(String userId, String text) {
        this(null, userId, new Date(), text, new ArrayList<String>());
    }

    @Id
    @ObjectId
    public String getId() {
        return id;
    }

    @ObjectId
    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    @ObjectId
    public List<String> getLikes() {
        return likes;
    }

}
