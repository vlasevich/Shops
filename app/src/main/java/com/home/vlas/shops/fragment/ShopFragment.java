package com.home.vlas.shops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.ShopListAdapter;
import com.home.vlas.shops.dto.ShopDTO;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_shop;

    public static ShopFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("Shop");
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setAdapter(new ShopListAdapter(createMockRemindListData()));
        return this.view;
    }

    private List<ShopDTO> createMockRemindListData() {
        List<ShopDTO> data = new ArrayList<>();
        data.add(new ShopDTO("item 1"));
        data.add(new ShopDTO("item 2"));
        data.add(new ShopDTO("item 3"));
        data.add(new ShopDTO("item 4"));
        data.add(new ShopDTO("item 5"));
        return data;
    }
}
