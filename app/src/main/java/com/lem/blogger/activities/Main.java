package com.lem.blogger.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.lem.blogger.R;

import java.util.HashMap;

import apis.Adapter;
import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import models.BlogInfo;
import models.CurrentUser;
import retrofit.Callback;
import retrofit.Response;


public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindString(R.string.api_key)
    String apiKey;

    @BindString(R.string.blog_url2)
    String blogURL;

    @Bind(R.id.blogger_title)
    TextView bloggerTitle;

    @Bind(R.id.blog_title)
    TextView blogTitle;

    @Bind(R.id.view_more)
    View viewMore;

    private String blogID;
    private Animation wiggle;
    private boolean isLoaded;

    /* Request code used to invoke sign in user interactions. */
    private static final int RC_SIGN_IN = 0;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private boolean mIsResolving = false;
    private boolean mShouldResolve = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .addApi(Plus.API)
                                .addScope(new Scope(Scopes.PROFILE))
                                .build();

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShouldResolve = true;
                mGoogleApiClient.connect();
            }
        });

        startBlogInfoAPI();
        wiggle();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }

            mIsResolving = false;
            mGoogleApiClient.connect();
        }
    }

    final void wiggle() {
        if(isLoaded)
            return;

        wiggle = AnimationUtils.loadAnimation(this, R.anim.wiggle);
        bloggerTitle.startAnimation(wiggle);

        wiggle.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wiggle();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showBlogTitle() {
        Animation pop = AnimationUtils.loadAnimation(this, R.anim.pop);
        blogTitle.startAnimation(pop);

        pop.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGoogleApiClient.connect();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @OnClick(R.id.view_more)
    public final void startBlogPosts() {
        CurrentUser.init(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient));
        Intent intent = new Intent(this, MainActivities.class);
        intent.putExtra(MainActivities.BLOG_ID, blogID);
        intent.putExtra(MainActivities.FRAGMENT_TO_ATTACH, MainActivities.BLOGPOSTS);
        startActivity(intent);
    }

    private void voidShowMore() {
        viewMore.setVisibility(View.VISIBLE);
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(viewMore, "Y", viewMore.getBottom(), viewMore.getY());
        slideUp.setDuration(200);
        slideUp.start();
    }

    private void startBlogInfoAPI() {
        Adapter.getAPIInterface().getBlogInfoByURL(generateBlogInfoUrlParam()).enqueue(new Callback<BlogInfo>() {
            @Override
            public void onResponse(Response<BlogInfo> response) {
                isLoaded = true;
                wiggle.cancel();
                blogTitle.setText(response.body().getName());
                blogID = response.body().getId();
                showBlogTitle();
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(Main.this, "Failed", Toast.LENGTH_SHORT).show();
                startBlogInfoAPI();
            }
        });
    }

    private HashMap<String, String> generateBlogInfoUrlParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("url", blogURL);
        return params;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mShouldResolve = false;
        startBlogPosts();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                    try {
                        connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                    mIsResolving = true;
            } else {
                // Could not resolve the connection result, show the user an
                // error dialog.
                Log.i("lem", "error");
            }
        } else {
            // Show the signed-out UI
            voidShowMore();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }
}
