package com.uj.myapplications.pojo.OrderPojo;

import android.support.annotation.Keep;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Keep
public class OrderDetailPojo {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("progress_status")
    @Expose
    private Integer progressStatus;
    @SerializedName("review_status")
    @Expose
    private Integer reviewStatus;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("customer")
    @Expose
    private CustomerPojo customer;
    @SerializedName("bill")
    @Expose
    private Integer bill;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType;
    @SerializedName("order_progress_log")
    @Expose
    private List<OrderProgressLog> orderProgressLog = null;
    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("order_number")
    @Expose
    private Integer orderNumber;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("meal")
    @Expose
    private Meal meal;
    @SerializedName("location")
    @Expose
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(Integer progressStatus) {
        this.progressStatus = progressStatus;
    }

    public Integer getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(Integer reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CustomerPojo getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerPojo customer) {
        this.customer = customer;
    }

    public Integer getBill() {
        return bill;
    }

    public void setBill(Integer bill) {
        this.bill = bill;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public List<OrderProgressLog> getOrderProgressLog() {
        return orderProgressLog;
    }

    public void setOrderProgressLog(List<OrderProgressLog> orderProgressLog) {
        this.orderProgressLog = orderProgressLog;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
