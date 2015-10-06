package com.lem.blogger.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.lem.blogger.R;
import com.lem.blogger.facebook.FacebookSession;
import com.lem.blogger.fragments.BlogPosts;
import com.lem.blogger.views.CircleTransform;
import com.lem.blogger.views.NavigationDrawerAdapter;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import models.CurrentUser;

/**
 * Created by Lemuel Castro on 10/6/2015.
 */
public class MainActivities extends AppCompatActivity {

    public static final String BLOG_ID = "blog_id";
    public static final String FRAGMENT_TO_ATTACH = "to_attach_fragment";
    public static final int BLOGPOSTS = 0;
    public static final int VIEWPOST = 1;

    private Toolbar toolbar;
    private CallbackManager callbackManager;

    private FragmentManager fm;

    @BindString(R.string.open_drawer)
    String openDrawer;

    @BindString(R.string.close_drawer)
    String closeDrawer;

    @Bind(R.id.listDrawer)
    ListView navigationDrawer;

    DrawerLayout drawerLayout;

    private ActionBarDrawerToggle actionBarToggle;

    private CurrentUser currentUser;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.main_activities);
        ButterKnife.bind(this);

        currentUser = CurrentUser.getInstance();

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer){
                /** Called when a drawer has settled in a completely open state. */
                public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }

                /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
            }
        };

        fm = getSupportFragmentManager();

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(actionBarToggle);

        String[] choices = {"lem", "lem2"};
        navigationDrawer.setAdapter(new NavigationDrawerAdapter(this));
        View navigationHeaderView = LayoutInflater.from(this).inflate(R.layout.navigation_accounts, null);
        Log.i("lem", "url "+currentUser.getImageURL());
        String parsedURL = currentUser.getImageURL().replaceFirst("sz=50", "sz=450");

        Picasso.with(this).load(parsedURL).transform(new CircleTransform()).into((ImageView) navigationHeaderView.findViewById(R.id.user_photo));
        ((TextView)navigationHeaderView.findViewById(R.id.user_name)).setText(currentUser.getPersonName());

        navigationDrawer.addHeaderView(navigationHeaderView);

        callbackManager = CallbackManager.Factory.create();
        FacebookSession.getInstance().setupLoginListener(callbackManager);


        determineFragmentToAttach();
    }

    private void determineFragmentToAttach(){
        Fragment fragment = null;
        switch (getIntent().getIntExtra(FRAGMENT_TO_ATTACH, 0)){
            case BLOGPOSTS:
                fragment = new BlogPosts();
                getSupportActionBar().setTitle(R.string.blog_posts);
                break;
        }

        fm.beginTransaction().add(R.id.fragment_holder, fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public FragmentManager getFragmentManagerInstance() {
        return fm;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarToggle.syncState();
    }
}
