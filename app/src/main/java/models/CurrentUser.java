package models;

import android.util.Log;

import com.google.android.gms.plus.model.people.Person;

/**
 * Created by Lemuel Castro on 10/6/2015.
 */
public class CurrentUser {

    private static CurrentUser currentUser;
    private String personName;
    private String imageURL;

    private CurrentUser(Person person){
        Log.i("lem", "init");
        this.personName = person.getDisplayName();
        this.imageURL = person.getImage().getUrl();
    }

    public static void init(Person person){
        currentUser = new CurrentUser(person);
    }

    public static CurrentUser getInstance(){
        return currentUser;
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
