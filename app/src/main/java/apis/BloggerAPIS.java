package apis;

import java.util.HashMap;

import models.BlogInfo;
import models.Items;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public interface BloggerAPIS {

    @GET("/blogger/v3/blogs/{blogID}")
    Call<BlogInfo> getBlogInfoByID(@Path("blogID") int blogID, @Query("key") String apiKey);

    @GET("/blogger/v3/blogs/byurl")
    Call<BlogInfo> getBlogInfoByURL(@QueryMap HashMap<String, String> params);

    @GET("/blogger/v3/blogs/{blogID}/posts")
    Call<Items> getBlogPost(@Path("blogID")String blogID, @Query("key") String apiKey);

    @GET("/blogger/v3/blogs/{blogID}/posts")
    Call<Items> getBlogPost(@Path("blogID")String blogID, @QueryMap HashMap<String, String>params);
}
