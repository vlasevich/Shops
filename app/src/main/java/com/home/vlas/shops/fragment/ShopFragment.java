package com.home.vlas.shops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.ShopListAdapter;
import com.home.vlas.shops.db.ShopsProvider;
import com.home.vlas.shops.model.Shop;
import com.home.vlas.shops.rest.ApiClient;
import com.home.vlas.shops.rest.ApiInterface;
import com.home.vlas.shops.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFragment extends AbstractTabFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ShopFragment.class.getSimpleName();
    private static final int LAYOUT = R.layout.fragment_shop;
    public List<Shop> shopList = new ArrayList<>();

    private RecyclerView rv;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        rv = (RecyclerView) view.findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        rv.setLayoutManager(new LinearLayoutManager(context));

        getShopsData();

        return this.view;

    }

    private void updateShopDB(List<Shop> list) {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(getContext());
        if (db.getAllShops().size() < list.size()) {
            int dif = list.size() - db.getAllShops().size();
            for (int i = list.size() - 1; i > list.size() - dif; i--) {
                // another method
                // db.createShop(list.get(i));
                db.createShopV2(getContext(), list.get(i));
            }
        } else {
            Log.i(TAG, "not need to update DB");
        }
        db.closeBD();
    }


    private void getShopsData() {
        if (checkConnection()) {
            getShopsDataFromWeb();
        } else {
            shopList = getShopsDataFromDB();
            rv.setAdapter(new ShopListAdapter(shopList));
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    private List<Shop> getShopsDataFromWeb() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Shop>> call = apiService.getShops();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                shopList = response.body();
                rv.setAdapter(new ShopListAdapter(shopList));
                updateShopDB(shopList);
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
            }
        });
        return shopList;
    }

    private List<Shop> getShopsDataFromDB() {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(getContext());
        if (db.getAllShops().size() > 0) {
        return db.getAllShops();
        } else {
            Log.i(TAG, "database is empty");
            Toast.makeText(getContext(), "no data", Toast.LENGTH_SHORT).show();
        }
        db.closeBD();
        return null;
    }


    @Override
    public void onRefresh() {
        getShopsData();
    }
}
