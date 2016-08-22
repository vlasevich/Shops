package com.home.vlas.shops.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

public abstract class AbstractTabFragment extends Fragment {

    protected Context context;
    protected View view;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}