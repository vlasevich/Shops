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
import com.home.vlas.shops.db.ShopsProvider;
import com.home.vlas.shops.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends AbstractTabFragment {
    final int LAYOUT = R.layout.fragment_map;
    final String TAG = MapFragment.class.getSimpleName();
    private ShopsProvider.DataBaseHelper db;
    private SupportMapFragment mSupportMapFragment;

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
        if (isVisibleToUser) {
            System.out.println("VISIBLE");
            initMap();
            //addDataToDBTest();
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
        db = new ShopsProvider.DataBaseHelper(getContext());
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

}
