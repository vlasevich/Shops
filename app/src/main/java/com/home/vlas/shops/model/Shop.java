package com.home.vlas.shops.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shop {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("location")
    @Expose
    private Location location;
    @SerializedName("website")
    @Expose
    private String website;

    public Shop() {
    }

    /**
     * @param id
     * @param phone
     * @param website
     * @param location
     * @param address
     * @param name
     */
    public Shop(Integer id, String name, String address, String phone, Location location, String website) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.location = location;
        this.website = website;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getWebsite() {
        return website;
    }

}