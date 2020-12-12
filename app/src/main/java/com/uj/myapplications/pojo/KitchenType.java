package com.uj.myapplications.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KitchenType {

    @SerializedName("veg")
    @Expose
    private Boolean veg;
    @SerializedName("nonveg")
    @Expose
    private Boolean nonveg;

    public Boolean getVeg() {
        return veg;
    }

    public void setVeg(Boolean veg) {
        this.veg = veg;
    }

    public Boolean getNonveg() {
        return nonveg;
    }

    public void setNonveg(Boolean nonveg) {
        this.nonveg = nonveg;
    }

}