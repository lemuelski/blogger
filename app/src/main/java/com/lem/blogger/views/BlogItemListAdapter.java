package com.lem.blogger.views;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lem.blogger.R;
import com.lem.blogger.fragments.ViewBlog;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import models.Blog;

/**
 * Created by Lemuel Castro on 9/28/2015.
 */
public class BlogItemListAdapter extends RecyclerView.Adapter<BlogItemListAdapter.BlogItemViewHolder> implements Serializable {

    private CardPressListener listener;
    private List<Blog> blogsList;
    private Activity activity;

    public interface CardPressListener{
        public void onCardPressed(Bundle bundle);
    }

    public BlogItemListAdapter(Activity activity, List<Blog> blogsList, CardPressListener listener) {
        this.blogsList = blogsList;
        this.activity = activity;
        this.listener = listener;
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.blog_item_card, viewGroup, false);
        return new BlogItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder blogItemViewHolder, final int i) {
        blogItemViewHolder.blogTitle.setText(blogsList.get(i).getTitle());
        StringBuilder builder = new StringBuilder();

        if(blogsList.get(i).getLabels()!=null){
            builder.append(activity.getString(R.string.prefix_labels));
            for(String blog: blogsList.get(i).getLabels()){
                builder.append(blog+", ");
            }
            builder.replace(builder.length()-2, builder.length(), "");
            blogItemViewHolder.authorName.setText(builder.toString());
        }

        blogItemViewHolder.itemHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(ViewBlog.CONTENT, blogsList.get(i).getContent());
                bundle.putString(ViewBlog.TITLE, blogsList.get(i).getTitle());
                listener.onCardPressed(bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return blogsList.size();
    }

    public static class BlogItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.blog_title)
        TextView blogTitle;

        @Bind(R.id.blog_author)
        TextView authorName;

        @Bind(R.id.item_holder)
        CardView itemHolder;

        BlogItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addToModel(List<Blog> blogs){
        for(Blog blog:blogs){
            blogsList.add(blog);
        }
    }
}
