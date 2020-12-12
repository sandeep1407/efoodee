package com.uj.myapplications.pojo.OrderPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderListPojo {
    @SerializedName("all")
    @Expose
    private List<OrderDetailPojo> all = null;
    @SerializedName("dinein")
    @Expose
    private List<OrderDetailPojo> dinein = null;
    @SerializedName("selfpickup")
    @Expose
    private List<OrderDetailPojo> selfpickup = null;
    @SerializedName("selfdelivery")
    @Expose
    private List<OrderDetailPojo> selfdelivery = null;
    @SerializedName("mymaadelivery")
    @Expose
    private List<OrderDetailPojo> mymaadelivery = null;

    public List<OrderDetailPojo> getAll() {
        return all;
    }

    public void setAll(List<OrderDetailPojo> all) {
        this.all = all;
    }

    public List<OrderDetailPojo> getDinein() {
        return dinein;
    }

    public void setDinein(List<OrderDetailPojo> dinein) {
        this.dinein = dinein;
    }

    public List<OrderDetailPojo> getSelfpickup() {
        return selfpickup;
    }

    public void setSelfpickup(List<OrderDetailPojo> selfpickup) {
        this.selfpickup = selfpickup;
    }

    public List<OrderDetailPojo> getSelfdelivery() {
        return selfdelivery;
    }

    public void setSelfdelivery(List<OrderDetailPojo> selfdelivery) {
        this.selfdelivery = selfdelivery;
    }

    public List<OrderDetailPojo> getMymaadelivery() {
        return mymaadelivery;
    }

    public void setMymaadelivery(List<OrderDetailPojo> mymaadelivery) {
        this.mymaadelivery = mymaadelivery;
    }

}
