package com.uj.myapplications.pojo;

import android.support.annotation.Keep;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class FssaiRegPojo {
    @SerializedName("licence_no")
    @Expose
    private String licenceNo;
    @SerializedName("is_registered")
    @Expose
    private Boolean isRegistered = false;
    @SerializedName("expiry")
    @Expose
    private String expiry;
    private String userFssaiImagepath;

    public String getUserFssaiImagepath() {
        return userFssaiImagepath;
    }

    public void setUserFssaiImagepath(String userFssaiImagepath) {
        this.userFssaiImagepath = userFssaiImagepath;
    }

    public String getLicenceNo() {
        return licenceNo;
    }

    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    public Boolean getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Boolean isRegistered) {
        this.isRegistered = isRegistered;
    }

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }
}
