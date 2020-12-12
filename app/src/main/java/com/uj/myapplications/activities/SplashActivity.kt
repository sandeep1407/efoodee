package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import com.uj.myapplications.R
import com.uj.myapplications.fcm.MyFirebaseMessagingService
import com.uj.myapplications.utility.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.net.URL
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern
import java.util.zip.CheckedInputStream


class SplashActivity : AppCompatActivity() {
    private var response_str: String? = ""
    private var message: String? = ""

    //var stringFile = "/storage/emulated/0/Pictures/MYMA/Temp/df6486ac-e761-4558-840d-7166fd63d3c6.jpg";
    //var stringUrl =
      //  "https://ec2-54-225-35-190.compute-1.amazonaws.com:31000/resources/supplier/5cab293156b00e4a3f531bb5/documents/fssai_doc.jpg"
    private var activityContext: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            // Do something for KITKAT and above versions
            setTheme(R.style.AppTheme)
        } else {
            // do something for phones running an SDK before lollipop
            setTheme(R.style.MyTheme)
        }
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        activityContext = this

        try {
            FirebaseApp.initializeApp(applicationContext)
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
                this@SplashActivity
            ) { instanceIdResult ->
                var fcmToken = instanceIdResult.token
                Timber.e("newToken", fcmToken)
                Log.e("newToken", fcmToken)
                //PrefUtils.setFcmToken(mContext, fcmToken)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
        hitIsValidSession()
    }

    fun hitIsValidSession() {
        // Check for session valid if not than clear pref and send it to login again
        val jsonObject = JSONObject()
        try {
            jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
            jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
            jsonObject.put(RestTags.DEVICE_TOKEN, MyFirebaseMessagingService.D_TOKEN)
            jsonObject.put(RestTags.DEVICE_TYPE, RestTags.PLATFORM)
            Timber.e(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (UtilityClass.isInternetAvailable(this)) {
            //  getResponse(RestUrls.LOGIN_URL, jsonObject)
            getResponseSessionValid(RestUrls.IS_SEESION_VALID, jsonObject)
        } else {
            UtilityClass.showToast(this, getString(R.string.device_offline_message))
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun getResponseSessionValid(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                //  UtilityClass.showDialog(this@SplashActivity)
            }

            override fun doInBackground(vararg params: String): HashMap<String, String>? {
                try {
                    val token = Config.geToken()
                    val res = OkhttpRequestUtils.doPostRequest(url, jsonObject, token)
                    if (res != null) {
                        val stringHashMap = HashMap<String, String>()
                        stringHashMap["code"] = res!!.code().toString()
                        stringHashMap["response"] = res!!.body()!!.string()
                        return stringHashMap
                    } else {
                        return null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(res: HashMap<String, String>?) {
                super.onPostExecute(res)
                if (res != null) {
                    var flagValidSession = false
                    var isTermAccepted = false
                    var jsonObject: JSONObject? = null
                    response_str = res["response"]
                    if (!response_str.isNullOrEmpty()) {
                        try {
                            Log.e("RESPONSE STRING:>", response_str)
                            jsonObject = JSONObject(response_str)
                            if (jsonObject.has("msg")) {
                                message = jsonObject.optString("msg")
                            }
                            // Here We can handle the code
                            when (Integer.parseInt(res["code"]!!)) {
                                200 -> {
                                    //   UtilityClass.hideDialog()
                                    if (response_str != null) {
                                        try {
                                            jsonObject = JSONObject(response_str)
                                            if (jsonObject != null && jsonObject.optInt("statusCode") == 200
                                                && jsonObject.optBoolean("isValidSession")
                                            ) {
                                                flagValidSession = true
                                                val data = jsonObject.optJSONObject("data")
                                                if (data != null && data.has("user")) {
                                                    val user = data.optJSONObject("user")

                                                    var userObject = Config.getUserData()
                                                    if (userObject != null) {
                                                        if (user != null) {
                                                            isTermAccepted = user.optBoolean("is_term_accepted")
                                                            userObject.isTermAccepted = isTermAccepted
                                                            userObject.bankDetailsEntered =
                                                                    user.optBoolean("bank_details_entered")
                                                            userObject.isActive = user.optBoolean("is_active")
                                                            userObject.isTermAccepted =
                                                                    user.optBoolean("is_term_accepted")
                                                        }
                                                        Config.setUserData(userObject)
                                                    }
                                                }
                                                /*     UtilityClass.showToast(
                                                         this@SplashActivity,
                                                         if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                     )*/
                                            } else {
                                                /*  UtilityClass.showToast(
                                                      this@SplashActivity,
                                                      if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                  )*/
                                                // showSessionInvalidAlertDialog()
                                            }
                                            // appCompatTextViewVersionNumber.setText("Version " + BuildConfig.VERSION_NAME)
                                            Handler().postDelayed({
                                                val status = Config.getUserLogin()
                                                if (status!!) {
                                                    if (flagValidSession && isTermAccepted) {
                                                        //Go to Navigation
                                                        startActivity(
                                                            Intent(
                                                                this@SplashActivity,
                                                                NavigationActivity::class.java
                                                            )
                                                        )
                                                        finish()
                                                    } else {
                                                        //Got To Register Fill the entries
                                                        // GO to Registration Activity
                                                        // If You not Found Valid Session
                                                        var intent = Intent(
                                                            this@SplashActivity,
                                                            RegistrationActivity::class.java
                                                        )
                                                        intent.putExtra(RestTags.FROM, "Splash")
                                                        intent.putExtra("SESSION_NOT_VALID", "SESSION_NOT_VALID")
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                } else {
                                                    //Go To Welcome Activity
                                                    startActivity(
                                                        Intent(
                                                            this@SplashActivity,
                                                            WelcomeActivity::class.java
                                                        )
                                                    )
                                                    //startActivity(new Intent(SplashActivity.this, MainClass.class));
                                                    finish()

                                                }

                                            }, 1000)

                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                                400 -> {
                                    UtilityClass.showToast(
                                        this@SplashActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                    gotOLogin()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        this@SplashActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                    gotOLogin()
                                }
                                //Default case of when
                                else -> {
                                    /* UtilityClass.showToast(
                                         this@SplashActivity,
                                         if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                     )*/
                                    UtilityClass.hideDialog()

                                }
                            }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                    } else {

                    }

                } else {
                    UtilityClass.showToast(
                        this@SplashActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                    NotificationUtil.askForInput(
                        this@SplashActivity,
                        "Timeout Error!",
                        "Your internet speed is slow,Please check your connectivity and retry again",
                        "Ok",
                        "Cancel",
                        true
                    ) {
                        if (it == "1") {
                            finish()
                        }
                    }
                }
            }
        }.execute()
    }

    fun gotOLogin() {
        //Go To Welcome Activity
        startActivity(
            Intent(
                this@SplashActivity,
                LoginActivity::class.java
            )
        )
        //startActivity(new Intent(SplashActivity.this, MainClass.class));
        finish()
    }

    var array = arrayOf("SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")
    fun setLunchDinnerTimings() {
        val Date = Date()
        val Cal = GregorianCalendar()
        Cal.time = Date
        val hr = Cal.get(GregorianCalendar.HOUR_OF_DAY)
        if (hr < 11) {
            // Set Dinner and Lunch Enable
            Log.e("Date", "Dinner and Lunch Enable")
        } else if (hr > 11 && hr < 16) {
            //OnLy Show Lunch Enabled and Dinner is disable
            Log.e("Date", "Lunch Enabled and Dinner is disable")
        } else if (hr > 16 && hr < 24) {
            //OnLy Show Dinner Enabled and Lunch is disable
            Log.e("Date", "Dinner Enabled and Lunch is disable")
        }
        for (i in 1..4) {
            println(i)
            val gc = GregorianCalendar()
            // Add Date
            gc.add(Calendar.DATE, i)
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val todayAsString = dateFormat.format(gc.time)
            gc.get(GregorianCalendar.DAY_OF_MONTH)
            val outFormat = SimpleDateFormat("EEEE")
            val goal = outFormat.format(gc.time)
            //val day = array[gc.get(Calendar.DAY_OF_WEEK)]
            Log.e("Day and Date", "Day is $goal and $todayAsString")
        }
    }


}
