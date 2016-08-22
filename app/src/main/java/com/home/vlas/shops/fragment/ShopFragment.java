package com.home.vlas.shops.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.ShopListAdapter;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.model.Shop;
import com.home.vlas.shops.rest.ApiClient;
import com.home.vlas.shops.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ShopFragment extends AbstractTabFragment {
    public static final String BASE_URL = "http://aschoolapi.appspot.com/";
    private static final int LAYOUT = R.layout.fragment_shop;
    private static Retrofit retrofit = null;
    public List<Shop> shopList = new ArrayList<>();
    List<Instrument> instrumentList = new ArrayList<Instrument>();
    private RecyclerView rv;

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
        rv.setLayoutManager(new LinearLayoutManager(context));
        shopList = getShopsArray();
        getInstrumentsArray();
        //rv.setAdapter(new ShopListAdapter(shopList));
        return this.view;


    }

    private List<Instrument> getInstrumentsArray() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Instrument>> call = apiInterface.getInstrument(1);
        call.enqueue(new Callback<List<Instrument>>() {
            @Override
            public void onResponse(Call<List<Instrument>> call, Response<List<Instrument>> response) {

                instrumentList = response.body();
                System.out.println(instrumentList);
                for (Instrument i : instrumentList) {
                    System.out.println(i.getInstrument().getBrand());
                    System.out.println(i.getQuantity());
                }
            }

            @Override
            public void onFailure(Call<List<Instrument>> call, Throwable t) {

            }
        });
        return instrumentList;
    }

    private List<Shop> getShopsArray() {
        Log.i("TAG", "----====START====----");
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Shop>> call = apiService.getShops();
        call.enqueue(new Callback<List<Shop>>() {
            @Override
            public void onResponse(Call<List<Shop>> call, Response<List<Shop>> response) {
                shopList = response.body();

                System.out.println(shopList.size());
                rv.setAdapter(new ShopListAdapter(shopList));
            }

            @Override
            public void onFailure(Call<List<Shop>> call, Throwable t) {
                Log.e("ERR", t.getMessage());
            }
        });
        return shopList;
    }

}
