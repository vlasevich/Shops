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

    /**
     * No args constructor for use in serialization
     */
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

    /**
     * @return The instrument
     */
    public DetailedInstrument getInstrument() {
        return instrument;
    }

    /**
     * @param instrument The instrument
     */
    public void setInstrument(DetailedInstrument instrument) {
        this.instrument = instrument;
    }

    public Instrument withInstrument(DetailedInstrument instrument) {
        this.instrument = instrument;
        return this;
    }

    /**
     * @return The quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity The quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Instrument withQuantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

}