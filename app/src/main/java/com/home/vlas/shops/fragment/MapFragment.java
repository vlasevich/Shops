package com.home.vlas.shops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.vlas.shops.R;
import com.home.vlas.shops.db.DataBaseHelper;
import com.home.vlas.shops.model.Instrument;

public class MapFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_map;
    private static final String TAG = MapFragment.class.getSimpleName();
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
        if (getUserVisibleHint()) {
            Log.i(TAG, "RUN");
            runBD();
        } else {
            Log.i(TAG, "NOT RUN");
        }
        return this.view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("VISIBLE");
            runBD();
        } else {
            System.out.println("INVISIBLE");
        }
    }

    public void runBD() {
        System.out.println("=============");
        System.out.println("RUNBD() INST");
        System.out.println("=============");
        db = new DataBaseHelper(getContext());

        //DetailedInstrument detailedInstrument=new DetailedInstrument(1,"brand1","model1","image1","type1",1000.0);
        /*for (int i = 0; i < 5; i++) {
            DetailedInstrument detailedInstrument=new DetailedInstrument(i,"brand"+i*i,"model"+i*i,"image"+i*i,"type"+i*i,1000.0+i*i);
            Instrument instrument=new Instrument(detailedInstrument,5);
            db.createInstrument(2,instrument);
        }*/

        /*for (Instrument i:db.getAllInstruments()){
            System.out.println("==============");
            Log.i(TAG, i.getInstrument().getId()+" - "+i.getInstrument().getBrand());
        }*/
        //db.deleteInstrument(1l);
        db.deleteInstById(3);

        System.out.println("SIZE: " + db.getAllInstByShopId(2l).size());
        for (Instrument i : db.getAllInstByShopId(2l)) {
            Log.i(TAG, i.getInstrument().getId() + " - " + i.getInstrument().getBrand());
        }
        System.out.println("==============");

        /*Shop shop=new Shop(111112,"a,","s","222",new Location(11,22),"sd");
        long shop_db=db.createShop(shop);
        System.out.println(shop_db);
        System.out.println(db.getShop(1).getAddress());*/
        /*for (Shop shop : db.getAllShops()) {
            System.out.println(shop.getId());
            System.out.println(shop.getAddress());
        }*/
        /*if (db.getShopCount() == 2) {
            db.deleteShop(11111);
            System.out.println("SHOP WAS DELETED");
        } else {
            Shop shop = new Shop(11114, "a,", "s", "222", new Location(11, 22), "sd");
            long shop_db = db.createShop(shop);
            db.createShop(shop);
        }*/
    }

}
