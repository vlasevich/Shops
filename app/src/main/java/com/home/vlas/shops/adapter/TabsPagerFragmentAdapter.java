package com.home.vlas.shops.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.home.vlas.shops.fragment.AbstractTabFragment;
import com.home.vlas.shops.fragment.ShopFragment;

import java.util.HashMap;
import java.util.Map;

public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private Map<Integer, AbstractTabFragment> tabs;
    private Context context;

    public TabsPagerFragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
        initTabMap(context);
    }

    private void initTabMap(Context context) {
        tabs = new HashMap<>();
        tabs.put(0, ShopFragment.getInstance(context));
        tabs.put(1, ShopFragment.getInstance(context));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }

    @Override
    public Fragment getItem(int position) {
        return tabs.get(position);
    }

    @Override
    public int getCount() {
        return tabs.size();
    }
}
