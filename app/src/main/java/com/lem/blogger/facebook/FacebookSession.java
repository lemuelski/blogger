package com.lem.blogger.facebook;

import android.app.Activity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

/**
 * Created by Lemuel on 8/16/2015.
 */
public class FacebookSession {

    private static FacebookSession fbSession;
    private static AccessTokenTracker accessTokenTracker;
    private static AccessToken accessToken;
    private static ProfileTracker profileTracker;
    private static Profile profile;
    private static SessionListener listener;

    public interface SessionListener{
        void onProfileChanged();
        void onAccessTokenChange();
    }

    private FacebookSession(){

    }

    public static FacebookSession getInstance(){
        if(fbSession == null){
            fbSession = new FacebookSession();
            accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {
                    accessToken = accessToken1;
                    if (listener!=null){
                        listener.onAccessTokenChange();
                    }
                }
            };

            accessToken = AccessToken.getCurrentAccessToken();


            profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {
                    profile = currentProfile;
                    if (listener!=null){
                        listener.onProfileChanged();
                    }
                }
            };
        }

        return fbSession;
    }

    public boolean isLoggedIn(){
        accessToken = AccessToken.getCurrentAccessToken();
        profile = Profile.getCurrentProfile();

        return accessToken!=null && profile!=null;
    }

    public void setupLoginListener(CallbackManager callbackManager){
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("lem", "success "+loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("lem", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("lem", "error "+e.getMessage());
            }
        });
    }

    public AccessToken getAccessToken(){
        return accessToken;
    }

    public Profile getProfile(){
        return profile;
    }

    public static void loginFacebook(Activity activity, SessionListener listenerFrom){
        listener = listenerFrom;
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList("public_profile", "user_friends"));
    }

}