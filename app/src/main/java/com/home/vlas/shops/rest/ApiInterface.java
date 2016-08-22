package com.home.vlas.shops.rest;

import com.home.vlas.shops.model.Instrument;
import com.home.vlas.shops.model.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {
    @GET("stores")
    Call<List<Shop>> getShops();

    @GET("/stores/{id}/instruments")
    Call<List<Instrument>> getInstrument(@Path("id") int id);
}
