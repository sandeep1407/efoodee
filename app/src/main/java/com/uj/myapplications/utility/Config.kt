package com.uj.myapplications.utility

import android.content.Context
import com.google.gson.Gson
import com.uj.myapplications.pojo.UserDataPojo

/**
 * Author : Vishvendra
 * Version: 1.0
 * 2/8/2018
 */
//Singleton Class
object Config {
    private const val USER_KEY = "user"
    private const val USER_LOGIN_STATUS = "user_login_status"
    private const val USER_DATA = "user_data"
    private const val CART_COUNT = "cartCount"
    private const val currentGST = "currentGST"
    private const val CART_LIST = "cartList"
    private const val IDN_VALUE = "idnValue"
    private const val IDN_LANGUAGE = "idnLanguage"
    private const val LANGUAGEARRAY = "language_array"
    private const val PURCHASE_DOMAIN = "purchase_domain"
    private const val TOKEN = "token"
    private const val FASSI_DOC = "fssai_doc"
    private const val DL_IMAGE = "dl_image"
    private const val BIKE_RC = "bike_rc"
    private const val PAN = "pan"
    private const val ADHAAR_CRD = "adhaar"
    private const val FCM_TOKEN = "fcmTOken"
    private const val PROFILE_IMAGE = "profileImg"

    private var manager: PrefManager? = null

    fun attachContext(context: Context) {
        if (manager == null) {
            manager = PrefManager(context.applicationContext)
        }
    }


    /*   fun saveUserModel(userModel: UserModel) {
           val serializedModel = Gson().toJson(userModel)
           manager?.setStringPref(USER_KEY, serializedModel)
       }

       fun getUserModel(): UserModel? {
           val savedValue: String? = manager?.getStringPref(USER_KEY)
           return Gson().fromJson(savedValue, UserModel::class.java)
       }

       //CartList
       fun saveCartList(list: ArrayList<ArrayList<DomainPriceListPojo>>) {
           val type = object : TypeToken<ArrayList<ArrayList<DomainPriceListPojo>>>() {
           }.type
           val serializedModel = Gson().toJson(list, type)
           manager?.setStringPref(CART_LIST, serializedModel)
       }

       fun getCartList(): ArrayList<ArrayList<DomainPriceListPojo>>? {
           val type = object : TypeToken<ArrayList<ArrayList<DomainPriceListPojo>>>() {
           }.type
           val savedValue: String? = manager?.getStringPref(CART_LIST)
           return Gson().fromJson(savedValue, type)
       }*/

    /* //CartList
     fun saveLanguageList(list: ArrayList<ArrayList<DomainPriceListPojo>>) {
         val type = object : TypeToken<ArrayList<ArrayList<DomainPriceListPojo>>>() {
         }.type
         val serializedModel = Gson().toJson(list, type)
         manager?.setStringPref(CART_LIST, serializedModel)
     }*/

    /* fun getLanguageList(): ArrayList<ArrayList<DomainPriceListPojo>>? {
         val type = object : TypeToken<ArrayList<ArrayList<DomainPriceListPojo>>>() {
         }.type
         val savedValue: String? = manager?.getStringPref(CART_LIST)
         return Gson().fromJson(savedValue, type)
     }*/
    //FCM TOken

    fun setFcmToken(token: String) {
        manager?.setStringPref(FCM_TOKEN, token)
    }

    fun getFCMToken(): String? {
        val token: String? = manager?.getStringPref(FCM_TOKEN)
        return token
    }

    //Pan Card Image
    fun setPANImage(token: String) {
        manager?.setStringPref(PAN, token)
    }

    fun getPANImage(): String? {
        val token: String? = manager?.getStringPref(PAN)
        return token
    }

    //DL
    fun setDLImage(token: String) {
        manager?.setStringPref(DL_IMAGE, token)
    }

    fun getDLImage(): String? {
        val token: String? = manager?.getStringPref(DL_IMAGE)
        return token
    }

    //Adhaar
    fun setADHAARImage(token: String) {
        manager?.setStringPref(ADHAAR_CRD, token)
    }

    fun getADHAARImage(): String? {
        val token: String? = manager?.getStringPref(ADHAAR_CRD)
        return token
    }

    //BIKE_RC
    fun setBIKERCImage(token: String) {
        manager?.setStringPref(BIKE_RC, token)
    }

    fun getBIKERCImage(): String? {
        val token: String? = manager?.getStringPref(BIKE_RC)
        return token
    }

    //Profile Pic
    fun setProfileImage(token: String) {
        manager?.setStringPref(PROFILE_IMAGE, token)
    }

    fun getProfileCImage(): String? {
        val token: String? = manager?.getStringPref(PROFILE_IMAGE)
        return token
    }

    //FSSAI
    fun setFSSAIImage(token: String) {
        manager?.setStringPref(FASSI_DOC, token)
    }

    fun geFSSAIImage(): String? {
        val token: String? = manager?.getStringPref(FASSI_DOC)
        return token
    }

    fun setToken(token: String) {
        manager?.setStringPref(TOKEN, token)
    }

    fun geToken(): String? {
        val token: String? = manager?.getStringPref(TOKEN)
        return token
    }

    fun setUserData(data: UserDataPojo) {
        val serialized = Gson().toJson(data)
        manager?.setStringPref(USER_DATA, serialized)
    }

    fun getUserData(): UserDataPojo? {
        val deserialized = manager?.getStringPref(USER_DATA)
        return Gson().fromJson(deserialized, UserDataPojo::class.java)
    }

    //Cart Value
    fun setCartCount(cartValue: Int) {
        manager?.setIntPref(CART_COUNT, cartValue)
    }

    fun getCartCount(): Int? {
        val cartValue: Int? = manager?.getIntPref(CART_COUNT)
        return cartValue
    }

    //User_Login
    fun setUserLogin(userLogin: Boolean) {
        manager?.setBoolPref(USER_LOGIN_STATUS, userLogin)
    }

    fun getUserLogin(): Boolean? {
        val userLogin: Boolean? = manager?.getBoolPref(USER_LOGIN_STATUS)
        return userLogin
    }


    //GST Value
    fun setCurrentGST(gstValue: Double) {
        manager?.setFloatPref(currentGST, gstValue.toFloat())
    }

    fun getGSTValue(): Double? {
        val cartValue: Double? = manager?.getFloatPref(currentGST)!!.toDouble()
        return cartValue
    }

    fun setIdnValue(idnValue: String) {
        manager?.setStringPref(IDN_VALUE, idnValue)
    }

    fun getIdnValue(): String? {
        val idnValue: String? = manager?.getStringPref(IDN_VALUE)
        return idnValue
    }

    //Ex."en" for English
    fun setIdnLanguage(idnLang: String) {
        manager?.setStringPref(IDN_LANGUAGE, idnLang)
    }

    fun getIdnLanguage(): String? {
        val idnLang: String? = manager?.getStringPref(IDN_LANGUAGE)
        return idnLang
    }


    //**********

    fun clear() {
        manager?.clear()
    }

}