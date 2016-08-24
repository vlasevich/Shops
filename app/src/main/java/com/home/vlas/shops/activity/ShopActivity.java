package com.home.vlas.shops.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.home.vlas.shops.R;
import com.home.vlas.shops.adapter.InstrumentsListAdapter;
import com.home.vlas.shops.db.DataBaseHelper;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.rest.ApiClient;
import com.home.vlas.shops.rest.ApiInterface;
import com.home.vlas.shops.utils.ConnectivityReceiver;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends Activity {
    private static final String TAG = ShopActivity.class.getSimpleName();
    List<Instrument> instList = new ArrayList<>();
    ListView listView;
    long shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAddress = (TextView) findViewById(R.id.shopAddress);
        TextView shopPhone = (TextView) findViewById(R.id.shopPhone);
        TextView shopWebsite = (TextView) findViewById(R.id.shopWebsite);
        Button sendEmail = (Button) findViewById(R.id.sendEmailBtn);

        listView = (ListView) findViewById(R.id.listView);

        shopId = getIntent().getIntExtra("SHOP_ID", 0);
        String name = getIntent().getStringExtra("SHOP_NAME");
        String address = getIntent().getStringExtra("SHOP_ADDRESS");
        String phone = getIntent().getStringExtra("SHOP_PHONE");
        String website = getIntent().getStringExtra("SHOP_WEBSITE");

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
            Log.i(TAG, "READ DATA FROM WEB");
            getInstDataFromWeb(shopId);
        } else {
            Log.i(TAG, "READ DATA FROM DB");
            instList = getDataFromBD();
            InstrumentsListAdapter adapter = new InstrumentsListAdapter(ShopActivity.this, instList);
            listView.setAdapter(adapter);
        }
    }

    // Method to manually check connection status
    private boolean checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        return isConnected;
    }

    private List<Instrument> getDataFromBD() {
        DataBaseHelper db = new DataBaseHelper(this.getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() > 0) {
            return db.getAllInstByShopId(shopId);
        } else {
            Log.i(TAG, "DATABASE IS EMPTY");
        }
        return null;
    }

    private List<Instrument> getInstDataFromWeb(long shopId) {

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Instrument>> call = apiInterface.getInstrument((int) shopId);
        call.enqueue(new Callback<List<Instrument>>() {
            @Override
            public void onResponse(Call<List<Instrument>> call, Response<List<Instrument>> response) {
                instList = response.body();
                System.out.println(instList);
                for (Instrument i : instList) {
                    System.out.println(i.getInstrument().getBrand());
                    System.out.println(i.getQuantity());
                }
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
        DataBaseHelper db = new DataBaseHelper(getApplicationContext());
        if (db.getAllInstByShopId(shopId).size() < instList.size()) {
            for (Instrument inst : instList) {
                // Log.i("DB", shop.getName());
                // db.createShop(shop);
                db.createInstrument(shopId, inst);
            }
            Log.i(TAG, "WRITE TO DB ALL INSTs");
        } else {
            Log.i(TAG, "NOT NEED TO UPDATE BD");
        }
    }
}
