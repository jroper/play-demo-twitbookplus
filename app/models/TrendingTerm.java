package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mongojack.Id;

public class TrendingTerm {

    private final String term;
    private final int count;

    public TrendingTerm(@Id String term, @JsonProperty("value") int count) {
        this.term = term;
        this.count = count;
    }

    public String getTerm() {
        return term;
    }

    public int getCount() {
        return count;
    }
}
