package com.uj.myapplications.pojo.MenuPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuComResponsePojo {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private MenuDataPojo menuDataPojo;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MenuDataPojo getMenuDataPojo() {
        return menuDataPojo;
    }

    public void setMenuDataPojo(MenuDataPojo menuDataPojo) {
        this.menuDataPojo = menuDataPojo;
    }

}