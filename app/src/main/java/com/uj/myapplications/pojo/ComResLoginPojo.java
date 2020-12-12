package com.uj.myapplications.pojo;

import android.support.annotation.Keep;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Keep
public class ComResLoginPojo {
    @SerializedName("user")
    @Expose
    private UserDataPojo user;
    @SerializedName("token")
    @Expose
    private String token;

    public UserDataPojo getUser() {
        return user;
    }

    public void setUser(UserDataPojo user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}