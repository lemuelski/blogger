package apis;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class Adapter {

    private static Retrofit adapter;
    private static BloggerAPIS bloggerAPIS;

    private Adapter(){
    }

    public static void init(){
        adapter = new Retrofit.Builder()
                              .baseUrl("https://www.googleapis.com")
                              .addConverterFactory(GsonConverterFactory.create())
                              .build();

        bloggerAPIS = adapter.create(BloggerAPIS.class);
    }

    public static BloggerAPIS getAPIInterface(){
        if (adapter == null){
            init();
        }

        return bloggerAPIS;
    }
}
