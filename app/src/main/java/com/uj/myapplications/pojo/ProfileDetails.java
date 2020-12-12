package com.uj.myapplications.pojo;

import java.math.BigInteger;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileDetails {

    @SerializedName("mobile")
    @Expose
    private BigInteger mobile;
    @SerializedName("mobile2")
    @Expose
    private BigInteger alternateMobNum;
    @SerializedName("address")
    @Expose
    private UserAddressPojo address;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("name")
    @Expose
    private UserNamePojo name;
    @SerializedName("bio")
    @Expose
    private String bio;
    @SerializedName("kitchen_pics")
    @Expose
    private List<String> kitchenPics = null;
    @SerializedName("kitchen_type")
    @Expose
    private Integer kitchenType;
    @SerializedName("location")
    @Expose
    private ClassLocation location;
    @SerializedName("speciality")
    @Expose
    private String speciality;
    @SerializedName("food_gallery")
    @Expose
    private List<String> foodGallery = null;

    @SerializedName("profile_pic")
    @Expose
    private String profilePic = null;

    @SerializedName("native_place")
    @Expose
    private NativePlaces nativePlace;

    public NativePlaces getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(NativePlaces nativePlace) {
        this.nativePlace = nativePlace;
    }

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public UserAddressPojo getAddress() {
        return address;
    }

    public void setAddress(UserAddressPojo address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserNamePojo getName() {
        return name;
    }

    public void setName(UserNamePojo name) {
        this.name = name;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getKitchenPics() {
        return kitchenPics;
    }

    public void setKitchenPics(List<String> kitchenPics) {
        this.kitchenPics = kitchenPics;
    }

    public Integer getKitchenType() {
        return kitchenType;
    }

    public void setKitchenType(Integer kitchenType) {
        this.kitchenType = kitchenType;
    }

    public ClassLocation getLocation() {
        return location;
    }

    public void setLocation(ClassLocation location) {
        this.location = location;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public List<String> getFoodGallery() {
        return foodGallery;
    }

    public void setFoodGallery(List<String> foodGallery) {
        this.foodGallery = foodGallery;
    }

    public BigInteger getAlternateMobNum() {
        return alternateMobNum;
    }

    public void setAlternateMobNum(BigInteger alternateMobNum) {
        this.alternateMobNum = alternateMobNum;
    }


    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }
}