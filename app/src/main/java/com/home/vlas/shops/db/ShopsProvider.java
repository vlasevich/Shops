package com.home.vlas.shops.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.home.vlas.shops.model.DetailedInstrument;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.model.Location;
import com.home.vlas.shops.model.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ShopsProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.home.vlas.shops";
    static final String URL = "content://" + PROVIDER_NAME + "/shop";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    static final int uriCode = 1;
    static final UriMatcher uriMatcher;
    private static final String TAG = ShopsProvider.class.getSimpleName();
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "ShopDatabase";
    private static final String TABLE_SHOP = "shop";
    private static final String TABLE_INSTRUMENT = "instrument";
    private static final String KEY_SHOP_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_INST_PRI_KEY = "id";
    private static final String KEY_SHOP_INST_ID = "shop_id";
    private static final String KEY_INSTRUMENT_ID = "inst_id";
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
            + KEY_LONGITUDE + " TEXT);";

    private static final String CREATE_TABLE_INSTRUMENTS = "CREATE TABLE " + TABLE_INSTRUMENT
            + "(" + KEY_INST_PRI_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_SHOP_INST_ID + " TEXT,"
            + KEY_INSTRUMENT_ID + " TEXT,"
            + KEY_BRAND + " TEXT,"
            + KEY_MODEL + " TEXT,"
            + KEY_IMAGEURL + " TEXT,"
            + KEY_TYPE + " TEXT,"
            + KEY_PRICE + " TEXT,"
            + KEY_QUANTITY + " TEXT);";
    private static HashMap<String, String> values;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "shop", uriCode);
        uriMatcher.addURI(PROVIDER_NAME, "shop/*", uriCode);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DataBaseHelper dbHelper = new DataBaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_SHOP);

        switch (uriMatcher.match(uri)) {
            case uriCode:
                qb.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder.isEmpty()) {
            sortOrder = "id";
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null,
                null, sortOrder);
        try {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
        return c;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case uriCode: {
                return "vnd.android.cursor.dir/shops";
            }
            default: {
                throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(TABLE_SHOP, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            try {
                getContext().getContentResolver().notifyChange(_uri, null);
            } catch (NullPointerException e) {
                Log.e(TAG, e.getMessage());
            }
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode: {
                count = db.delete(TABLE_SHOP, selection, strings);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case uriCode: {
                count = db.update(TABLE_SHOP, values, selection, strings);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI " + uri);
            }
        }
        try {
            getContext().getContentResolver().notifyChange(uri, null);
        } catch (NullPointerException e) {
            Log.e(TAG, e.getMessage());
        }

        return count;
    }


    public static class DataBaseHelper extends SQLiteOpenHelper {
        private final String TAG = DataBaseHelper.class.getSimpleName();

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
            values.put(KEY_LONGITUDE, shop.getLocation().getLongitude());

            return db.insert(TABLE_SHOP, null, values);
        }

        public void createShopV2(Context context,Shop shop){
            ContentValues values=new ContentValues();
            values.put(KEY_SHOP_ID, shop.getId());
            values.put(KEY_NAME, shop.getName());
            values.put(KEY_ADDRESS, shop.getAddress());
            values.put(KEY_PHONE, shop.getPhone());
            values.put(KEY_LATITUDE, shop.getLocation().getLatitude());
            values.put(KEY_LONGITUDE, shop.getLocation().getLongitude());
            Uri uri=context.getContentResolver().insert(ShopsProvider.CONTENT_URI,values);
        }

        public Shop getShop(long shopId) {
            SQLiteDatabase db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM " + TABLE_SHOP
                    + " WHERE " + KEY_SHOP_ID + " = " + shopId;
            Cursor c = db.rawQuery(selectQuery, null);
            if (c != null) {
                c.moveToFirst();
            }
            Shop shop = new Shop();
            if (c != null) {
                shop.setId(c.getInt(c.getColumnIndex(KEY_SHOP_ID)));
                shop.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                shop.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                shop.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                Location location = new Location(
                        c.getInt(c.getColumnIndex(KEY_LATITUDE))
                        , c.getInt(c.getColumnIndex(KEY_LONGITUDE)
                ));
                shop.setLocation(location);
            }

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
                            , c.getInt(c.getColumnIndex(KEY_LONGITUDE)
                    ));
                    shop.setLocation(location);
                    shopList.add(shop);
                } while (c.moveToNext());
            }
            return shopList;
        }

        public int getShopCount() {
            String countQuery = "SELECT * FROM " + TABLE_SHOP;
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
            values.put(KEY_LONGITUDE, shop.getLocation().getLongitude());

            return db.update(TABLE_SHOP, values, KEY_SHOP_ID + "=?", new String[]{String.valueOf(shop.getId())});
        }

        public void deleteShop(long shopId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_SHOP, KEY_SHOP_ID + "=?", new String[]{String.valueOf(shopId)});
        }

        public long createInstrument(long shopId, Instrument inst) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_SHOP_INST_ID, shopId);
            values.put(KEY_INSTRUMENT_ID, inst.getInstrument().getId());
            values.put(KEY_BRAND, inst.getInstrument().getBrand());
            values.put(KEY_MODEL, inst.getInstrument().getModel());
            values.put(KEY_IMAGEURL, inst.getInstrument().getImageUrl());
            values.put(KEY_TYPE, inst.getInstrument().getType());
            values.put(KEY_PRICE, inst.getInstrument().getPrice());
            values.put(KEY_QUANTITY, inst.getQuantity());

            return db.insert(TABLE_INSTRUMENT, null, values);
        }

        public List<Instrument> getAllInstruments() {
            List<Instrument> instList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_INSTRUMENT;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    Instrument inst = new Instrument();
                    inst.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
                    DetailedInstrument detailInst = new DetailedInstrument();
                    detailInst.setId(c.getInt(c.getColumnIndex(KEY_INSTRUMENT_ID)));
                    detailInst.setBrand(c.getString(c.getColumnIndex(KEY_BRAND)));
                    detailInst.setModel(c.getString(c.getColumnIndex(KEY_MODEL)));
                    detailInst.setImageUrl(c.getString(c.getColumnIndex(KEY_IMAGEURL)));
                    detailInst.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                    detailInst.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                    inst.setInstrument(detailInst);
                    instList.add(inst);
                } while (c.moveToNext());
            }
            return instList;
        }

        public List<Instrument> getAllInstByShopId(long shopId) {
            List<Instrument> instList = new ArrayList<>();
            String selectQuery = "SELECT * FROM " + TABLE_INSTRUMENT
                    + " WHERE " + KEY_SHOP_INST_ID + " = " + shopId;

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor c = db.rawQuery(selectQuery, null);

            if (c.moveToFirst()) {
                do {
                    Instrument inst = new Instrument();
                    inst.setQuantity(c.getInt(c.getColumnIndex(KEY_QUANTITY)));
                    DetailedInstrument detailInst = new DetailedInstrument();
                    detailInst.setId(c.getInt(c.getColumnIndex(KEY_INSTRUMENT_ID)));
                    detailInst.setBrand(c.getString(c.getColumnIndex(KEY_BRAND)));
                    detailInst.setModel(c.getString(c.getColumnIndex(KEY_MODEL)));
                    detailInst.setImageUrl(c.getString(c.getColumnIndex(KEY_IMAGEURL)));
                    detailInst.setType(c.getString(c.getColumnIndex(KEY_TYPE)));
                    detailInst.setPrice(c.getDouble(c.getColumnIndex(KEY_PRICE)));
                    inst.setInstrument(detailInst);
                    instList.add(inst);
                } while (c.moveToNext());
            }
            return instList;
        }

        public void deleteInstrument(long instId) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_INSTRUMENT, KEY_INSTRUMENT_ID + "=?", new String[]{String.valueOf(instId)});
        }

        public void deleteInstById(int id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_INSTRUMENT, KEY_INSTRUMENT_ID + "=" + id, null);
        }

        public int updateInstrument(Instrument inst) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(KEY_INSTRUMENT_ID, inst.getInstrument().getId());
            values.put(KEY_BRAND, inst.getInstrument().getBrand());
            values.put(KEY_MODEL, inst.getInstrument().getModel());
            values.put(KEY_IMAGEURL, inst.getInstrument().getImageUrl());
            values.put(KEY_TYPE, inst.getInstrument().getType());
            values.put(KEY_PRICE, inst.getInstrument().getPrice());
            values.put(KEY_QUANTITY, inst.getQuantity());

            return db.update(TABLE_INSTRUMENT, values, KEY_INSTRUMENT_ID + "=?", new String[]{String.valueOf(inst.getInstrument().getId())});
        }

        public void closeBD() {
            SQLiteDatabase db = this.getReadableDatabase();
            if (db != null && db.isOpen()) {
                db.close();
            }
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
}
