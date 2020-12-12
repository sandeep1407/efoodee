package com.uj.myapplications.pojo.OrderPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("suplier")
    @Expose
    private LocationSupplier suplier;
    @SerializedName("customer")
    @Expose
    private LocationCustomer customer;
    @SerializedName("distance")
    @Expose
    private Distance distance;

    public LocationSupplier getSuplier() {
        return suplier;
    }

    public void setSuplier(LocationSupplier suplier) {
        this.suplier = suplier;
    }

    public LocationCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(LocationCustomer customer) {
        this.customer = customer;
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }
}