package models;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class BlogInfo {

    private String id;
    private String name;
    private String description;
    private String url;
    private Posts posts;

    public BlogInfo(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Posts getPosts() {
        return posts;
    }

    public void setPosts(Posts posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "[ id : "+id+", name : "+name+", description : "+description+", url : "+url+", number of post : "+posts.getTotalItems()+"]";
    }
}
