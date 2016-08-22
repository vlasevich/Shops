package com.home.vlas.shops.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailedInstrument {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("model")
    @Expose
    private String model;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("price")
    @Expose
    private Double price;

    public DetailedInstrument() {
    }

    /**
     * @param id
     * @param model
     * @param price
     * @param imageUrl
     * @param brand
     * @param type
     */
    public DetailedInstrument(Integer id, String brand, String model, String imageUrl, String type, Double price) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.imageUrl = imageUrl;
        this.type = type;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DetailedInstrument withId(Integer id) {
        this.id = id;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public DetailedInstrument withBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public DetailedInstrument withModel(String model) {
        this.model = model;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public DetailedInstrument withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DetailedInstrument withType(String type) {
        this.type = type;
        return this;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public DetailedInstrument withPrice(Double price) {
        this.price = price;
        return this;
    }

}