package com.home.vlas.shops.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("latitude")
    @Expose
    private Integer latitude;
    @SerializedName("longitude")
    @Expose
    private Integer longitude;

    public Location() {
    }

    /**
     * @param longitude
     * @param latitude
     */
    public Location(Integer latitude, Integer longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getLatitude() {
        return latitude;
    }

    public void setLatitude(Integer latitude) {
        this.latitude = latitude;
    }

    public Location withLatitude(Integer latitude) {
        this.latitude = latitude;
        return this;
    }

    public Integer getLongitude() {
        return longitude;
    }

    public void setLongitude(Integer longitude) {
        this.longitude = longitude;
    }

    public Location withLongitude(Integer longitude) {
        this.longitude = longitude;
        return this;
    }

    public double getMapLatitude() {
        return ((double) latitude / 1_000_000);
    }

    public double getMapLongitude() {
        return ((double) longitude / 1_000_000);
    }

}