package com.uj.myapplications.pojo.MenuPojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuNamePojo {

@SerializedName("nameNative")
@Expose
private String nameNative;
@SerializedName("nameEnglish")
@Expose
private String nameEnglish;

public String getNameNative() {
return nameNative;
}

public void setNameNative(String nameNative) {
this.nameNative = nameNative;
}

public String getNameEnglish() {
return nameEnglish;
}

public void setNameEnglish(String nameEnglish) {
this.nameEnglish = nameEnglish;
}

}