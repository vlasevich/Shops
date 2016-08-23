package com.home.vlas.shops.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.home.vlas.shops.R;

public class ShopActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_activity);

        TextView shopName = (TextView) findViewById(R.id.shopName);
        TextView shopAddress = (TextView) findViewById(R.id.shopAddress);
        TextView shopPhone = (TextView) findViewById(R.id.shopPhone);
        TextView shopWebsite = (TextView) findViewById(R.id.shopWebsite);

        String name = getIntent().getStringExtra("SHOP_NAME");
        String address = getIntent().getStringExtra("SHOP_ADDRESS");
        String phone = getIntent().getStringExtra("SHOP_PHONE");
        String website = getIntent().getStringExtra("SHOP_WEBSITE");

        shopName.setText(name);
        shopAddress.setText(address);
        shopPhone.setText(phone);
        shopWebsite.setText(website);
    }
}
