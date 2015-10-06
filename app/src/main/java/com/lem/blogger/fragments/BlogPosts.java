package com.lem.blogger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lem.blogger.R;
import com.lem.blogger.activities.MainActivities;
import com.lem.blogger.views.BlogItemListAdapter;

import java.util.HashMap;

import apis.Adapter;
import butterknife.BindString;
import butterknife.ButterKnife;
import models.Items;
import retrofit.Callback;
import retrofit.Response;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class BlogPosts extends Fragment {
    private String blogID;
    private String nextPageToken;

    @BindString(R.string.api_key)
    String apiKey;

    private RecyclerView recyclerView;

    private BlogItemListAdapter blogItemListAdapter;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading;
    private FragmentManager fm;
    private String backStackID;

    private boolean isFromOnCreate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFromOnCreate = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blogposts, container, false);
        ButterKnife.bind(this, view);

        backStackID = getClass().getName();

        fm = ((MainActivities) getActivity()).getFragmentManagerInstance();

        blogID = getActivity().getIntent().getStringExtra(MainActivities.BLOG_ID);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = llm.getChildCount();
                totalItemCount = llm.getItemCount();
                pastVisiblesItems = llm.findFirstVisibleItemPosition();

                if (!loading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                        loading = true;
                        getNextPage();
                    }
                }
            }
        });

        if(isFromOnCreate){
            getBlogPosts();
        }else{
            recyclerView.setAdapter(blogItemListAdapter);
        }

        isFromOnCreate = false;
        return view;
    }


    private void getBlogPosts() {
        Adapter.getAPIInterface().getBlogPost(blogID, apiKey).enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Response<Items> response) {
                nextPageToken = response.body().getNextPageToken();
                blogItemListAdapter = new BlogItemListAdapter(getActivity(), response.body().getItems(), new BlogItemListAdapter.CardPressListener() {
                    @Override
                    public void onCardPressed(Bundle bundle) {
                        ViewBlog viewBlog = new ViewBlog();
                        viewBlog.setArguments(bundle);
                        FragmentTransaction ft = fm.beginTransaction();
                        ft.addToBackStack(backStackID);
                        ft.replace(R.id.fragment_holder, viewBlog).commit();
                    }
                });
                recyclerView.setAdapter(blogItemListAdapter);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.i("lem", "fail");
            }
        });
    }

    private void getNextPage() {
        HashMap<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        params.put("pageToken", nextPageToken);

        Adapter.getAPIInterface().getBlogPost(blogID, params).enqueue(new Callback<Items>() {
            @Override
            public void onResponse(Response<Items> response) {
                loading = false;
                nextPageToken = response.body().getNextPageToken();
                blogItemListAdapter.addToModel(response.body().getItems());
                blogItemListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                loading = false;
            }
        });
    }
}
