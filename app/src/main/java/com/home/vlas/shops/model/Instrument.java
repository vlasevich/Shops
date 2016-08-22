package com.home.vlas.shops.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Instrument {

    @SerializedName("instrument")
    @Expose
    private DetailedInstrument instrument;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;

    public Instrument() {
    }

    /**
     * @param quantity
     * @param instrument
     */
    public Instrument(DetailedInstrument instrument, Integer quantity) {
        this.instrument = instrument;
        this.quantity = quantity;
    }

    public DetailedInstrument getInstrument() {
        return instrument;
    }

    public void setInstrument(DetailedInstrument instrument) {
        this.instrument = instrument;
    }

    public Instrument withInstrument(DetailedInstrument instrument) {
        this.instrument = instrument;
        return this;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instrument withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}