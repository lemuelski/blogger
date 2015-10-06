package models;

import java.util.List;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class Items {
    private String nextPageToken;
    private List<Blog> items;

    public List<Blog> getItems() {
        return items;
    }

    public void setItems(List<Blog> items) {
        this.items = items;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}
