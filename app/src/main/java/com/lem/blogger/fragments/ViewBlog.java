package com.lem.blogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.facebook.CallbackManager;
import com.lem.blogger.R;
import com.lem.blogger.facebook.FacebookSession;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class ViewBlog extends Fragment {

    public static final String TITLE = "title";
    public static final String CONTENT = "content";

    private CallbackManager callbackManager;

    @Bind(R.id.webview)
    WebView content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_blog, container, false);
        ButterKnife.bind(this, view);
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().setLoadWithOverviewMode(true);
        content.getSettings().setUseWideViewPort(true);
        content.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);

        String text = "<html><head>"
                + "<style type=\"text/css\">"
                +    "body{color: #000000; font-weight: bold; font-size:200%;} p{margin : 0; padding :0; text-align:justify;}"
                + "</style></head>"
                + "<body>"
                +"<h1>"+getArguments().getString(TITLE)+"</h1>"
                + getArguments().getString(CONTENT)
                + "</body></html>";


        content.loadDataWithBaseURL("", text, "text/html", "UTF-8", "");

        callbackManager = CallbackManager.Factory.create();

        FacebookSession.getInstance().setupLoginListener(callbackManager);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.view_blog_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.action_share:
                FacebookSession.loginFacebook(getActivity(), new FacebookSession.SessionListener() {
                    @Override
                    public void onProfileChanged() {

                    }

                    @Override
                    public void onAccessTokenChange() {

                    }
                });
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
