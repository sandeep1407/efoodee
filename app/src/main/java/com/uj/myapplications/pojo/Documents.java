package com.uj.myapplications.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Documents {

    @SerializedName("fssai_doc")
    @Expose
    private String fssaiDoc;
    @SerializedName("pan")
    @Expose
    private String pan;
    @SerializedName("adhaar")
    @Expose
    private String adhaar;
    @SerializedName("driving_license")
    @Expose
    private String drivingLicense;
    @SerializedName("bike_rc")
    @Expose
    private String bikeRc;

    public String getFssaiDoc() {
        return fssaiDoc;
    }

    public void setFssaiDoc(String fssaiDoc) {
        this.fssaiDoc = fssaiDoc;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAdhaar() {
        return adhaar;
    }

    public void setAdhaar(String adhaar) {
        this.adhaar = adhaar;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getBikeRc() {
        return bikeRc;
    }

    public void setBikeRc(String bikeRc) {
        this.bikeRc = bikeRc;
    }

}