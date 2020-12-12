package com.uj.myapplications.pojo;

import android.support.annotation.Keep;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class UserCommunicationPreferences {
    @SerializedName("push")
    @Expose
    private Boolean push = true;
    @SerializedName("phone")
    @Expose
    private Boolean phone = true;
    @SerializedName("sms")
    @Expose
    private Boolean sms = true;
    @SerializedName("email")
    @Expose
    private Boolean email = true;

    public Boolean getPush() {
        return push;
    }

    public void setPush(Boolean push) {
        this.push = push;
    }

    public Boolean getPhone() {
        return phone;
    }

    public void setPhone(Boolean phone) {
        this.phone = phone;
    }

    public Boolean getSms() {
        return sms;
    }

    public void setSms(Boolean sms) {
        this.sms = sms;
    }

    public Boolean getEmail() {
        return email;
    }

    public void setEmail(Boolean email) {
        this.email = email;
    }
}
