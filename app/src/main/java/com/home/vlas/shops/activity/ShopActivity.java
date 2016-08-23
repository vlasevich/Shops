package com.home.vlas.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.InstrumentsListAdapter;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.rest.ApiClient;
import com.home.vlas.shops.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends Activity {
    List<Instrument> instList = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAddress = (TextView) findViewById(R.id.shopAddress);
        TextView shopPhone = (TextView) findViewById(R.id.shopPhone);
        TextView shopWebsite = (TextView) findViewById(R.id.shopWebsite);

        listView = (ListView) findViewById(R.id.listView);

        String name = getIntent().getStringExtra("SHOP_NAME");
        String address = getIntent().getStringExtra("SHOP_ADDRESS");
        String phone = getIntent().getStringExtra("SHOP_PHONE");
        String website = getIntent().getStringExtra("SHOP_WEBSITE");

        shopName.setText(name);
        shopAddress.setText(address);
        shopPhone.setText(phone);
        shopWebsite.setText(website);

        getInstrumentsArray();
    }

    private List<Instrument> getInstrumentsArray() {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Instrument>> call = apiInterface.getInstrument(1);
        call.enqueue(new Callback<List<Instrument>>() {
            @Override
            public void onResponse(Call<List<Instrument>> call, Response<List<Instrument>> response) {
                instList = response.body();
                System.out.println(instList);
                for (Instrument i : instList) {
                    System.out.println(i.getInstrument().getBrand());
                    System.out.println(i.getQuantity());
                }

                InstrumentsListAdapter aDapter = new InstrumentsListAdapter(ShopActivity.this, instList);
                listView.setAdapter(aDapter);

            }

            @Override
            public void onFailure(Call<List<Instrument>> call, Throwable t) {

            }
        });
        return instList;
    }
}
