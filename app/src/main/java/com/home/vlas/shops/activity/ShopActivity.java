package com.home.vlas.shops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.InstrumentsListAdapter;
import com.home.vlas.shops.db.ShopsProvider;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.rest.ApiClient;
import com.home.vlas.shops.rest.ApiInterface;
import com.home.vlas.shops.utils.ConnectivityReceiver;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = ShopActivity.class.getSimpleName();
    private static final String TEST_IMAGE_URL = "https://defcon.ru/wp-content/uploads/2015/12/ico_android-3.png";
    private List<Instrument> instList = new ArrayList<>();
    private ListView listView;
    private long shopId;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAddress = (TextView) findViewById(R.id.shopAddress);
        TextView shopPhone = (TextView) findViewById(R.id.shopPhone);
        TextView shopWebsite = (TextView) findViewById(R.id.shopWebsite);
        Button sendEmail = (Button) findViewById(R.id.sendEmailBtn);
        ImageView image = (ImageView) findViewById(R.id.imageView);

        listView = (ListView) findViewById(R.id.listView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutInst);
        swipeRefreshLayout.setOnRefreshListener(this);

        //get data about shop
        shopId = getIntent().getIntExtra("SHOP_ID", 0);
        String name = getIntent().getStringExtra("SHOP_NAME");
        String address = getIntent().getStringExtra("SHOP_ADDRESS");
        String phone = getIntent().getStringExtra("SHOP_PHONE");
        String website = getIntent().getStringExtra("SHOP_WEBSITE");
        //show test image from web by Picasso because shop's image field is always empty
        Picasso.with(getApplicationContext())
                .load(TEST_IMAGE_URL)
                .placeholder(R.mipmap.ic_launcher)
                .into(image);
        // add simple button to send email, but shops again do not have emails
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                intent.putExtra(Intent.EXTRA_TEXT, "I'm email body.");

                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });

        shopName.setText(name);
        shopAddress.setText(address);
        shopPhone.setText(phone);
        shopWebsite.setText(website);

        getInstData();
    }

    // get data from web or from db
    private void getInstData() {
        //check internet connection
        if (checkConnection()) {
            Log.i(TAG, "read data from web");
            getInstDataFromWeb(shopId);
        } else {
            Log.i(TAG, "read data from db");
            instList = getDataFromBD();
            if (instList != null) {
                Collections.reverse(instList);
            }
            //add list of shops to adapter
            InstrumentsListAdapter adapter = new InstrumentsListAdapter(ShopActivity.this, instList);
            listView.setAdapter(adapter);
        }
        //stop show refreshing
        swipeRefreshLayout.setRefreshing(false);
    }

    // Method to manually check connection status
    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    //get data from db if there is no internet connection
    private List<Instrument> getDataFromBD() {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(this.getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() > 0) {
            List<Instrument> list = db.getAllInstByShopId(shopId);
            Collections.reverse(list);
            return list;
        } else {
            Log.i(TAG, "database is empty");
            Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_SHORT).show();
        }
        db.closeBD();
        return null;
    }

    //get data from web using Retrofit
    private List<Instrument> getInstDataFromWeb(long shopId) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Instrument>> call = apiInterface.getInstrument((int) shopId);
        call.enqueue(new Callback<List<Instrument>>() {
            @Override
            public void onResponse(Call<List<Instrument>> call, Response<List<Instrument>> response) {
                //insert data to list
                instList = response.body();
                //update db
                updateShopDB(instList);
                //add our list to adapter
                InstrumentsListAdapter adapter = new InstrumentsListAdapter(ShopActivity.this, instList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Instrument>> call, Throwable t) {
                Log.e(TAG, "some problems with getting data from web");
            }
        });
        return instList;
    }

    //update db if it gets new data
    private void updateShopDB(List<Instrument> list) {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() < list.size()) {
            int instListSize = db.getAllInstByShopId(shopId).size();
            for (int i = instListSize; i < list.size(); i++) {
                db.createInstrument(shopId, list.get(i));
            }
            Log.i(TAG, "insert to db instruments");
        } else {
            Log.i(TAG, "not need to update db");
        }
        db.closeBD();
    }

    @Override
    public void onRefresh() {
        //refresh data on swipe
        getInstData();
    }
}
