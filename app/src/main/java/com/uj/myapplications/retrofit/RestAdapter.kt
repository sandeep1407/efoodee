package com.uj.myapplications.utility

import android.util.Base64
import android.util.Log
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.uj.myapplications.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author : Umesh Jangid
 * Version: 1.0
 * 21/2/2019
 */

object RestAdapter {
    private val TAG = "RestAdapter"
    private val CONNECTION_TIMEOUT: Long = 30
    private val API_URL = RestUrls.BASE_URL

    val service: RestInterface
        get() {
            val client = okhttpClient
            return Retrofit.Builder().baseUrl(API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RestInterface::class.java)
        }

    private val okhttpClient: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i(TAG, message) })
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(interceptor)
                builder.addNetworkInterceptor(StethoInterceptor())
            }
            builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            builder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            builder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

            return builder.build()
        }

    fun getBasicAuth(login: String, pass: String): String {
        val source = "$login:$pass"
        return "Basic " + Base64.encodeToString(source.toByteArray(), Base64.URL_SAFE or Base64.NO_WRAP)
    }
}