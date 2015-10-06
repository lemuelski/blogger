package com.lem.blogger.views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.lem.blogger.R;

/**
 * Created by Lemuel Castro on 10/6/2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Activity activity;

    public NavigationDrawerAdapter(Activity activity){
        this.activity = activity;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = LayoutInflater.from(activity).inflate(R.layout.navigation_items, null);
        return v;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
