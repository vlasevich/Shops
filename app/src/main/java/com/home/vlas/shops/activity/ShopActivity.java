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


        shopId = getIntent().getIntExtra("SHOP_ID", 0);
        String name = getIntent().getStringExtra("SHOP_NAME");
        String address = getIntent().getStringExtra("SHOP_ADDRESS");
        String phone = getIntent().getStringExtra("SHOP_PHONE");
        String website = getIntent().getStringExtra("SHOP_WEBSITE");

        Picasso.with(getApplicationContext())
                .load(TEST_IMAGE_URL)
                .placeholder(R.mipmap.ic_launcher)
                .into(image);

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

    private void getInstData() {
        if (checkConnection()) {
            Log.i(TAG, "read data from web");
            getInstDataFromWeb(shopId);
        } else {
            Log.i(TAG, "read data from db");
            instList = getDataFromBD();
            if (instList != null) {
                Collections.reverse(instList);
            }
            InstrumentsListAdapter adapter = new InstrumentsListAdapter(ShopActivity.this, instList);
            listView.setAdapter(adapter);
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    // Method to manually check connection status
    private boolean checkConnection() {
        return ConnectivityReceiver.isConnected();
    }

    private List<Instrument> getDataFromBD() {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(this.getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() > 0) {
            return db.getAllInstByShopId(shopId);
        } else {
            Log.i(TAG, "database is empty");
            Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_SHORT).show();
        }
        db.closeBD();
        return null;
    }

    private List<Instrument> getInstDataFromWeb(long shopId) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Instrument>> call = apiInterface.getInstrument((int) shopId);
        call.enqueue(new Callback<List<Instrument>>() {
            @Override
            public void onResponse(Call<List<Instrument>> call, Response<List<Instrument>> response) {
                instList = response.body();

                updateShopDB(instList);

                InstrumentsListAdapter adapter = new InstrumentsListAdapter(ShopActivity.this, instList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Instrument>> call, Throwable t) {
            }
        });
        return instList;
    }

    private void updateShopDB(List<Instrument> list) {
        ShopsProvider.DataBaseHelper db = new ShopsProvider.DataBaseHelper(getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() < list.size()) {

            int dif = list.size() - db.getAllInstByShopId(shopId).size();
            for (int i = list.size() - 1; i > list.size() - dif; i--) {
                db.createInstrument(shopId, list.get(i));
            }
            Log.i(TAG, "insert to db insts");
        } else {
            Log.i(TAG, "not need to update db");
        }
        db.closeBD();
    }

    @Override
    public void onRefresh() {
        getInstData();
    }
}
