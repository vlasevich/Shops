package com.home.vlas.shops.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.home.vlas.shops.R;
import com.home.vlas.shops.db.DataBaseHelper;
import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends AbstractTabFragment {
    private static final int LAYOUT = R.layout.fragment_map;
    private static final String TAG = MapFragment.class.getSimpleName();
    private DataBaseHelper db;
    private SupportMapFragment mSupportMapFragment;
    private boolean firstInit = false;

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
        return this.view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !firstInit) {
            System.out.println("VISIBLE");
            //runBD();
            initMap();
            firstInit = true;
        } else {
            System.out.println("INVISIBLE");
        }
    }

    private void initMap() {
        Log.i(TAG, "Map init");
        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapwhere);
        if (mSupportMapFragment == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentTransaction.replace(R.id.mapwhere, mSupportMapFragment).commit();
        }

        if (mSupportMapFragment != null) {
            mSupportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    if (googleMap != null) {

                        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        googleMap.getUiSettings().setAllGesturesEnabled(true);

                        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(48.464095, 35.045630)).zoom(15.0f).build();
                        //CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        for (MarkerOptions marker : getShopsLocations()) {
                            googleMap.addMarker(marker).showInfoWindow();
                        }

                        googleMap.getUiSettings().setZoomControlsEnabled(true);
                        googleMap.getUiSettings().setCompassEnabled(true);

                        showCurrentLocation(googleMap);


                    }

                }
            });
        }
    }

    public void showCurrentLocation(GoogleMap googleMap) {
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

            showCurrentLocation(googleMap);
        }

    }

    private List<MarkerOptions> getShopsLocations() {
        db = new DataBaseHelper(getContext());
        List<MarkerOptions> shopLocList = new ArrayList<>();
        for (Shop shop : db.getAllShops()) {
            shopLocList.add((new MarkerOptions().position(
                    new LatLng(
                            shop.getLocation().getMapLatitude(),
                            shop.getLocation().getMapLongitude()))
                    .title(shop.getName())));
        }
        return shopLocList;
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
