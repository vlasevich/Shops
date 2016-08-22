package com.home.vlas.shops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.vlas.shops.R;
import com.home.vlas.shops.db.DataBaseHelper;
import com.home.vlas.shops.model.Location;
import com.home.vlas.shops.model.Shop;

public class MapFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_map;
    private DataBaseHelper db;

    public static MapFragment getInstance(Context context) {
        Bundle args = new Bundle();
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("Map");
        return fragment;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(LAYOUT, container, false);
        runBD();
        return this.view;

    }

    public void runBD() {
        db = new DataBaseHelper(getContext());
        /*Shop shop=new Shop(111112,"a,","s","222",new Location(11,22),"sd");
        long shop_db=db.createShop(shop);
        System.out.println(shop_db);
        System.out.println(db.getShop(1).getAddress());*/
        for (Shop shop : db.getAllShops()) {
            System.out.println(shop.getId());
            System.out.println(shop.getAddress());
        }
        if (db.getShopCount() == 2) {
            db.deleteShop(11111);
            System.out.println("SHOP WAS DELETED");
        } else {
            Shop shop = new Shop(11114, "a,", "s", "222", new Location(11, 22), "sd");
            long shop_db = db.createShop(shop);
            db.createShop(shop);
        }
    }

}
