package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatusUpdate {
    private final String id;
    private final String user;
    private final Date date;
    private final String text;
    private final List<String> likes;

    public StatusUpdate(
            String id,
            String user,
            Date date,
            String text,
            List<String> likes) {
        this.id = id;
        this.user = user;
        this.date = date;
        this.text = text;
        this.likes = likes;
    }

    public StatusUpdate(String userId, String text) {
        this(null, userId, new Date(), text, new ArrayList<String>());
    }

    public String getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public List<String> getLikes() {
        return likes;
    }

}
