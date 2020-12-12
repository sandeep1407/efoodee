package com.uj.myapplications.utility

import android.content.Context
import android.content.SharedPreferences

class PrefManager(mCtx: Context) {
    private val mSharedPref: SharedPreferences

    init {
        mSharedPref = mCtx.applicationContext.getSharedPreferences(PREF_NAME, 0)
    }

    fun setStringPref(key: String, value: String) {
        mSharedPref.edit().putString(key, value).apply()
    }

    fun setBoolPref(key: String, value: Boolean) {
        mSharedPref.edit().putBoolean(key, value).apply()
    }

    fun setIntPref(key: String, value: Int) {
        mSharedPref.edit().putInt(key, value).apply()
    }

    fun setFloatPref(key: String, value: Float) {
        mSharedPref.edit().putFloat(key, value).apply()
    }

    fun getStringPref(key: String): String {
        return mSharedPref.getString(key, "")
    }

    fun getBoolPref(key: String): Boolean {
        return mSharedPref.getBoolean(key, false)
    }

    fun getIntPref(key: String): Int {
        return mSharedPref.getInt(key, 0)
    }

    fun getFloatPref(key: String): Float {
        return mSharedPref.getFloat(key, 0.0f)
    }


    fun clear() {
        mSharedPref.edit().clear().apply()
    }

    companion object {
        private val PREF_NAME = "MYMAS"
    }

}
