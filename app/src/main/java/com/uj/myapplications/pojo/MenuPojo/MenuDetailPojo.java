package com.uj.myapplications.pojo.MenuPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MenuDetailPojo {
    @SerializedName("name")
    @Expose
    private MenuNamePojo name;
    @SerializedName("pics")
    @Expose
    private List<String> pics = null;
    @SerializedName("menu_type")
    @Expose
    private Integer menuType;
    @SerializedName("service_type")
    @Expose
    private List<String> serviceType = null;
    @SerializedName("spice_index")
    @Expose
    private Integer spiceIndex;
    @SerializedName("last_updated_date")
    @Expose
    private String lastUpdatedDate;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("contents")
    @Expose
    private List<Content> contents = null;
    @SerializedName("extras")
    @Expose
    private List<Extra> extras = null;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("cuisine")
    @Expose
    private String cuisine;
    @SerializedName("desciption")
    @Expose
    private String desciption;
    @SerializedName("meal_type")
    @Expose
    private Integer mealType;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("supplier")
    @Expose
    private String supplier;
    @SerializedName("scheduled_to")
    @Expose
    private String scheduledTo;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("is_published")
    @Expose
    private Boolean isPublished;
    @SerializedName("is_scheduled")
    @Expose
    private Boolean isScheduled;
    @SerializedName("orders")
    @Expose
    private List<Object> orders = null;
    @SerializedName("meal_serving_time")
    @Expose
    private String mealServingTime;
    @SerializedName("closing_time")
    @Expose
    private String closingTime;
    @SerializedName("created_date")
    @Expose
    private String createdDate;

    public String getScheduledTo() {
        return scheduledTo;
    }

    public void setScheduledTo(String scheduledTo) {
        this.scheduledTo = scheduledTo;
    }

    public MenuNamePojo getName() {
        return name;
    }

    public void setName(MenuNamePojo name) {
        this.name = name;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public List<String> getServiceType() {
        return serviceType;
    }

    public void setServiceType(List<String> serviceType) {
        this.serviceType = serviceType;
    }

    public Integer getSpiceIndex() {
        return spiceIndex;
    }

    public void setSpiceIndex(Integer spiceIndex) {
        this.spiceIndex = spiceIndex;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Content> getContents() {
        return contents;
    }

    public void setContents(List<Content> contents) {
        this.contents = contents;
    }

    public List<Extra> getExtras() {
        return extras;
    }

    public void setExtras(List<Extra> extras) {
        this.extras = extras;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public Integer getMealType() {
        return mealType;
    }

    public void setMealType(Integer mealType) {
        this.mealType = mealType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getPublished() {
        return isPublished;
    }

    public void setPublished(Boolean published) {
        isPublished = published;
    }

    public Boolean getScheduled() {
        return isScheduled;
    }

    public void setScheduled(Boolean scheduled) {
        isScheduled = scheduled;
    }

    public List<Object> getOrders() {
        return orders;
    }

    public void setOrders(List<Object> orders) {
        this.orders = orders;
    }

    public String getMealServingTime() {
        return mealServingTime;
    }

    public void setMealServingTime(String mealServingTime) {
        this.mealServingTime = mealServingTime;
    }

    public String getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(String closingTime) {
        this.closingTime = closingTime;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}



