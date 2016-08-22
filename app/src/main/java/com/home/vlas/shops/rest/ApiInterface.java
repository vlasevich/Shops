package com.home.vlas.shops.rest;

import com.home.vlas.shops.model.Shop;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {
    @GET("stores")
    Call<List<Shop>> getShops();

/*    @GET("/stores/{id}/instruments")
    Call<ShopResponse> getDetailedShopInfo(@Path("id") int id);*/
}
