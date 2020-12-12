package com.uj.myapplications.pojo.OrderPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationCustomer {
    @SerializedName("lat")
    @Expose
    private Integer lat;
    @SerializedName("long")
    @Expose
    private Integer _long;

    public Integer getLat() {
        return lat;
    }

    public void setLat(Integer lat) {
        this.lat = lat;
    }

    public Integer getLong() {
        return _long;
    }

    public void setLong(Integer _long) {
        this._long = _long;
    }
}
