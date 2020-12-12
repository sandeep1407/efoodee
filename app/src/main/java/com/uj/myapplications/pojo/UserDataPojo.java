package com.uj.myapplications.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.List;

public class UserDataPojo {
    @SerializedName("name")
    @Expose
    private UserNamePojo name;
    @SerializedName("fssai")
    @Expose
    private FssaiRegPojo fssai;
    @SerializedName("is_term_accepted")
    @Expose
    private Boolean isTermAccepted = false;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("bank_details_entered")
    @Expose
    private Boolean bankDetailsEntered = false;
    @SerializedName("is_active")
    @Expose
    private Boolean isActive;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("service_type")
    @Expose
    private Integer serviceType = -1;
    @SerializedName("mobile")
    @Expose
    private BigInteger mobile;
    @SerializedName("mobile2")
    @Expose
    private BigInteger alternamteMobileNum;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("documents")
    @Expose
    private Documents documents;
    @SerializedName("follower_count")
    @Expose
    private Integer followerCount;
    @SerializedName("kitchen_pics")
    @Expose
    private List<String> kitchenPics = null;
    @SerializedName("dinein_pics")
    @Expose
    private List<Object> dineinPics = null;
    @SerializedName("profile_pic")
    @Expose
    private String profilePic;
    //  private String name;
    private String displayName;
    private String userAdharImagepath;
    private String userPancardImagepath;
    private String userDLImagepath;
    private String userBikeRCImagepath;
    private String userEmail;
    @SerializedName("native_place")
    @Expose
    private NativePlaces nativePlaces;
    @SerializedName("address")
    @Expose
    private UserAddressPojo userAddress;
    private BankPojo bankPojo;
    private ProfileDetails profileDetails;
    @SerializedName("vehicle_type")
    @Expose
    private Integer vehicleType;
    @SerializedName("area_limit")
    @Expose
    private Integer areaLimit;
    @SerializedName("location")
    @Expose
    private ClassLocation location;

    public NativePlaces getNativePlaces() {
        return nativePlaces;
    }

    public void setNativePlaces(NativePlaces nativePlaces) {
        this.nativePlaces = nativePlaces;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getAreaLimit() {
        return areaLimit;
    }

    public void setAreaLimit(Integer areaLimit) {
        this.areaLimit = areaLimit;
    }

    public ClassLocation getLocation() {
        return location;
    }

    public void setLocation(ClassLocation location) {
        this.location = location;
    }

    public ProfileDetails getProfileDetails() {
        return profileDetails;
    }

    public void setProfileDetails(ProfileDetails profileDetails) {
        this.profileDetails = profileDetails;
    }

    public BankPojo getBankPojo() {
        return bankPojo;
    }

    public void setBankPojo(BankPojo bankPojo) {
        this.bankPojo = bankPojo;
    }

    private UserCommunicationPreferences userCommunication;


    public String getUserAdharImagepath() {
        return userAdharImagepath;
    }

    public void setUserAdharImagepath(String userAdharImagepath) {
        this.userAdharImagepath = userAdharImagepath;
    }

    public String getUserPancardImagepath() {
        return userPancardImagepath;
    }

    public void setUserPancardImagepath(String userPancardImagepath) {
        this.userPancardImagepath = userPancardImagepath;
    }

    public String getUserDLImagepath() {
        return userDLImagepath;
    }

    public void setUserDLImagepath(String userDLImagepath) {
        this.userDLImagepath = userDLImagepath;
    }

    public String getUserBikeRCImagepath() {
        return userBikeRCImagepath;
    }

    public void setUserBikeRCImagepath(String userBikeRCImagepath) {
        this.userBikeRCImagepath = userBikeRCImagepath;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public UserCommunicationPreferences getUserCommunication() {
        return userCommunication;
    }

    public void setUserCommunication(UserCommunicationPreferences userCommunication) {
        this.userCommunication = userCommunication;
    }

    public Boolean getTermAccepted() {
        return isTermAccepted;
    }

    public void setTermAccepted(Boolean termAccepted) {
        isTermAccepted = termAccepted;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public UserAddressPojo getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddressPojo userAddress) {
        this.userAddress = userAddress;
    }

    public UserNamePojo getName() {
        return name;
    }

    public void setName(UserNamePojo name) {
        this.name = name;
    }

    public FssaiRegPojo getFssai() {
        return fssai;
    }

    public void setFssai(FssaiRegPojo fssai) {
        this.fssai = fssai;
    }

    public Boolean getIsTermAccepted() {
        return isTermAccepted;
    }

    public void setIsTermAccepted(Boolean isTermAccepted) {
        this.isTermAccepted = isTermAccepted;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Boolean getBankDetailsEntered() {
        return bankDetailsEntered;
    }

    public void setBankDetailsEntered(Boolean bankDetailsEntered) {
        this.bankDetailsEntered = bankDetailsEntered;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getServiceType() {
        return serviceType;
    }

    public void setServiceType(Integer serviceType) {
        this.serviceType = serviceType;
    }

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents documents) {
        this.documents = documents;
    }

    public Integer getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public List<String> getKitchenPics() {
        return kitchenPics;
    }

    public void setKitchenPics(List<String> kitchenPics) {
        this.kitchenPics = kitchenPics;
    }

    public List<Object> getDineinPics() {
        return dineinPics;
    }

    public void setDineinPics(List<Object> dineinPics) {
        this.dineinPics = dineinPics;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public BigInteger getAlternamteMobileNum() {
        return alternamteMobileNum;
    }

    public void setAlternamteMobileNum(BigInteger alternamteMobileNum) {
        this.alternamteMobileNum = alternamteMobileNum;
    }
}
