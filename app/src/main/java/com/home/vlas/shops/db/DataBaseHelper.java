package com.home.vlas.shops.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.home.vlas.shops.model.Location;
import com.home.vlas.shops.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = DataBaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ShopDatabase";

    private static final String TABLE_SHOP = "shop";
    private static final String TABLE_INSTRUMENT = "instrument";

    private static final String KEY_SHOP_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGTITUDE = "longitude";

    private static final String KEY_INSTRUMENT_ID = "id";
    private static final String KEY_BRAND = "brand";
    private static final String KEY_MODEL = "model";
    private static final String KEY_IMAGEURL = "imageUrl";
    private static final String KEY_TYPE = "type";
    private static final String KEY_PRICE = "price";
    private static final String KEY_QUANTITY = "quantity";

    private static final String CREATE_TABLE_SHOP = "CREATE TABLE " + TABLE_SHOP
            + "(" + KEY_SHOP_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_LATITUDE + " TEXT,"
            + KEY_LONGTITUDE + " TEXT);";

    private static final String CREATE_TABLE_INSTRUMENTS = "CREATE TABLE " + TABLE_INSTRUMENT
            + "(" + KEY_INSTRUMENT_ID + " INTEGER PRIMARY KEY,"
            + KEY_BRAND + " TEXT,"
            + KEY_MODEL + " TEXT,"
            + KEY_IMAGEURL + " TEXT,"
            + KEY_TYPE + " TEXT,"
            + KEY_PRICE + " TEXT,"
            + KEY_QUANTITY + " TEXT);";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public long createShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHOP_ID, shop.getId());
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_ADDRESS, shop.getAddress());
        values.put(KEY_PHONE, shop.getPhone());
        values.put(KEY_LATITUDE, shop.getLocation().getLatitude());
        values.put(KEY_LONGTITUDE, shop.getLocation().getLongitude());

        long shopId = db.insert(TABLE_SHOP, null, values);

        return shopId;
    }

    public Shop getShop(long shopId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_SHOP
                + "WHERE " + KEY_SHOP_ID + " = " + shopId;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        Shop shop = new Shop();
        shop.setId(c.getInt(c.getColumnIndex(KEY_SHOP_ID)));
        shop.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        shop.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
        shop.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
        Location location = new Location(
                c.getInt(c.getColumnIndex(KEY_LATITUDE))
                , c.getInt(c.getColumnIndex(KEY_LONGTITUDE)
        ));
        shop.setLocation(location);

        return shop;
    }

    public List<Shop> getAllShops() {
        List<Shop> shopList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SHOP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Shop shop = new Shop();
                shop.setId(c.getInt(c.getColumnIndex(KEY_SHOP_ID)));
                shop.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                shop.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                shop.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                Location location = new Location(
                        c.getInt(c.getColumnIndex(KEY_LATITUDE))
                        , c.getInt(c.getColumnIndex(KEY_LONGTITUDE)
                ));
                shop.setLocation(location);
                shopList.add(shop);
            } while (c.moveToNext());
        }
        return shopList;
    }

    public int getShopCount() {
        String countQuery = "SELECT * FROm " + TABLE_SHOP;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(countQuery, null);
        int count = c.getCount();
        c.close();

        return count;
    }

    public int updateShop(Shop shop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SHOP_ID, shop.getId());
        values.put(KEY_NAME, shop.getName());
        values.put(KEY_ADDRESS, shop.getAddress());
        values.put(KEY_PHONE, shop.getPhone());
        values.put(KEY_LATITUDE, shop.getLocation().getLatitude());
        values.put(KEY_LONGTITUDE, shop.getLocation().getLongitude());

        return db.update(TABLE_SHOP, values, KEY_SHOP_ID + "=?", new String[]{String.valueOf(shop.getId())});
    }

    public void deleteShop(long shopId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOP, KEY_SHOP_ID + "=?", new String[]{String.valueOf(shopId)});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SHOP);
        db.execSQL(CREATE_TABLE_INSTRUMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTRUMENT);

        onCreate(db);
    }
}
