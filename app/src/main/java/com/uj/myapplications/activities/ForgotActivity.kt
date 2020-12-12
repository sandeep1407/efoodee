package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.pojo.CommonRegisterResponsePojo
import com.uj.myapplications.pojo.OtpPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_forgot.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*
import android.os.CountDownTimer
import com.uj.myapplications.activities.ForgotActivity.MyCountDownTimer


class ForgotActivity : AppCompatActivity(), View.OnClickListener {
    private var response_str: String? = ""
    var OTP: String? = null
    var flag_resend: Int = 0
    var myCountDownTimer: MyCountDownTimer? = null
    var TIMECOUNTTIMER: Long = 120000 //120 SEConds
    var INTERVALTIMER: Long = 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        btnResetPassword.setOnClickListener(this)
        ibtnForgotClose.setOnClickListener(this)
        ibtnCloseOTPVerification.setOnClickListener(this)
        btnContinue.setOnClickListener(this)
        btnConfirm.setOnClickListener(this)
        btnResendVerificationCode.setOnClickListener(this)
    }

    @SuppressLint("StaticFieldLeak")
    fun getResponseForgot(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@ForgotActivity)
            }

            override fun doInBackground(vararg params: String): HashMap<String, String>? {
                try {
                    val res = OkhttpRequestUtils.doPostRequest(url, jsonObject)
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
                var message: String? = null
                var jsonObject: JSONObject? = null
                if (res != null) {
                    when (Integer.parseInt(res["code"])) {
                        200 -> {
                            UtilityClass.hideDialog()
                            response_str = res["response"]
                            if (response_str != null) {
                                Log.e("RESPONSE STRING:>", response_str)
                                try {
                                    val jsonObject = JSONObject(response_str)
                                    if (jsonObject != null) {
                                        val commonRegisterResponsePojo = Gson().fromJson<CommonRegisterResponsePojo>(
                                            jsonObject.toString(),
                                            CommonRegisterResponsePojo::class.java
                                        )
                                        if (commonRegisterResponsePojo != null && commonRegisterResponsePojo.success) {
                                            val otppojo: OtpPojo = commonRegisterResponsePojo.data
                                            OTP = otppojo.otp
                                            if (!commonRegisterResponsePojo.msg.isNullOrEmpty()) {
                                                UtilityClass.showToast(
                                                    this@ForgotActivity,
                                                    commonRegisterResponsePojo.msg
                                                )
                                            } else {
                                                UtilityClass.showToast(
                                                    this@ForgotActivity,
                                                    getString(R.string.otp_sent_success)
                                                )
                                            }
                                            //Make OTP Screen Visible
                                            // Make Text change
                                            // Resend Situation
                                            if (flag_resend == 101) {
                                                /*  UtilityClass.showToast(
                                                      this@ForgotActivity,
                                                      getString(R.string.otp_sent_success)
                                                  )*/
                                                //Start Timer(In Both the Cases)
                                                // (Disable Button Again for 120 Sec)
                                                btnResendVerificationCode.setTextColor(resources.getColor(R.color.colorGrey))
                                                btnResendVerificationCode.isEnabled = false
                                                myCountDownTimer = MyCountDownTimer(TIMECOUNTTIMER, INTERVALTIMER)
                                                myCountDownTimer?.start()
                                            } else {
                                                val mobile_num = tlMobileNumber.editText!!.text.toString().trim()
                                                txt_number.text = " $mobile_num"
                                                /* tvVerificationCodeTag.text =
                                                         getString(R.string.enter_verification_code).plus(" $mobile_num")*/
                                                rlSendOTP.visibility = View.GONE
                                                rlOTPVerification.visibility = View.VISIBLE
                                                rlResetPassScreen.visibility = View.GONE
                                                //Start Timer
                                                myCountDownTimer = MyCountDownTimer(TIMECOUNTTIMER, INTERVALTIMER)
                                                myCountDownTimer?.start()
                                            }
                                        }
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            try {
                                message = JSONObject(res["response"]).getString("msg")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            UtilityClass.showToast(
                                this@ForgotActivity,
                                if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                            )
                            UtilityClass.hideDialog()

                        }
                        500 -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING + " 500")
                            /*   activeUtils.hideDialog()
                               try {
                                   message = JSONObject(res["response"]).getString("message")
                               } catch (e: Exception) {
                                   e.printStackTrace()
                               }

                               ActiveUtils.showSnackBar(
                                   coordinatorLayoutLogin,
                                   message ?: "Issue on server side",
                                   Snackbar.LENGTH_LONG
                               )*/
                        }
                        else -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING)
                            /*   activeUtils.hideDialog()
                            try {
                                message = JSONObject(res["response"]).getString("message")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                            ActiveUtils.showSnackBar(
                                coordinatorLayoutLogin,
                                message ?: "Error in Authentication",
                                Snackbar.LENGTH_LONG
                            )
                        }*/
                        }
                    }
                } else {
                    /*  activeUtils.hideDialog()
                      ActiveUtils.showAlertDialog(context, StaticData.OOPS_STRING, 2)*/
                }
            }
        }.execute()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnResetPassword -> {
                if (!tlMobileNumber.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, getString(R.string.mobile_empty_message))
                } else if (tlMobileNumber.editText!!.text.toString().trim().length < 10) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, "Invalid Mobile Number.")
                } else {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
                        jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                        jsonObject.put(RestTags.OTP_TYPE, RestTags.OTP_TYPE_RESET_PASS)
                        Timber.e(jsonObject.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (UtilityClass.isInternetAvailable(this)) {
                        getResponseForgot(RestUrls.FORGOT_PASSWORD_URL, jsonObject)
                    } else {
                        UtilityClass.showToast(this, getString(R.string.device_offline_message))
                    }
                }
            }
            R.id.ibtnForgotClose -> {
                onBackPressed()
            }
            R.id.ibtnCloseOTPVerification -> {
                onBackPressed()
            }
            R.id.btnContinue -> {
                // Case OTP Verification Api Will Hit
                // OTP Screen Click and in  validate the same
                when {
                    OTP.isNullOrEmpty() -> {
                        UtilityClass.shakeItemView(editotpview, this)
                        UtilityClass.showToast(this, "OTP "+Constants.ERR_CANT_BE_EMPTY)
                    }
                    OTP != editotpview.otpText.toString() -> {
                        UtilityClass.shakeItemView(editotpview, this)
                        UtilityClass.showToast(this, getString(R.string.OTP_not_match_message))
                    }
                    else -> {
                        val jsonObject = JSONObject()
                        try {
                            jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                            jsonObject.put(RestTags.OTP, editotpview.otpText.toString().trim())
                            jsonObject.put(RestTags.OTP_TYPE, RestTags.OTP_TYPE_RESET_PASS)
                            jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
                            jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                            Timber.e(jsonObject.toString())
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        if (UtilityClass.isInternetAvailable(this)) {
                            getOTPVerifyResponse(RestUrls.VERIFY_OTP_URL, jsonObject)
                        } else {
                            UtilityClass.showToast(this, getString(R.string.device_offline_message))
                        }

                    }
                }
            }
            R.id.btnConfirm -> {
                // Reset password api will hit
                if (!tlNewPassword.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlNewPassword, this)
                    UtilityClass.showToast(this, "New Password "+Constants.ERR_CANT_BE_EMPTY)
                } else if (tlNewPassword.editText!!.text.toString().length < 8) {
                    UtilityClass.shakeItemView(tlNewPassword, this)
                    UtilityClass.showToast(this, getString(R.string.password_invalid_message))
                } else if (!UtilityClass.isValidPassword(tlNewPassword.editText!!.text.toString())) {
                    UtilityClass.shakeItemView(tlNewPassword, this)
                    UtilityClass.showToast(this, getString(R.string.password_invalid_message))
                } else if (!tlRenterPassword.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlRenterPassword, this)
                    UtilityClass.showToast(this, "Confirm Password "+Constants.ERR_CANT_BE_EMPTY)
                } else if (tlNewPassword.editText!!.text.toString().trim() != tlRenterPassword.editText!!.text.toString().trim()) {
                    UtilityClass.showToast(this, getString(R.string.password_not_match_message))
                } else {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.NEW_PASSWORD, tlNewPassword.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.CONFIRM_PASSWORD, tlRenterPassword.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
                        jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                        Timber.e(jsonObject.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (UtilityClass.isInternetAvailable(this)) {
                        getResetPasswordResponse(RestUrls.CHANGE_PASSWORD_URL, jsonObject)
                    } else {
                        UtilityClass.showToast(this, getString(R.string.device_offline_message))
                    }
                }

            }
            R.id.btnResendVerificationCode -> {
                // Resend OTP to same number from forgot Api
                // Resend Call Again Register Apis
                editotpview.clearTextFromView()

                flag_resend = 101
                val jsonObject = JSONObject()
                try {
                    jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
                    jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                    jsonObject.put(RestTags.OTP_TYPE, RestTags.OTP_TYPE_RESET_PASS)
                    Timber.e(jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (UtilityClass.isInternetAvailable(this)) {
                    getResponseForgot(RestUrls.FORGOT_PASSWORD_URL, jsonObject)
                } else {
                    UtilityClass.showToast(this, getString(R.string.device_offline_message))
                }
            }

        }
    }

    // OTP Verification Code :
    @SuppressLint("StaticFieldLeak")
    fun getOTPVerifyResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@ForgotActivity)
            }

            override fun doInBackground(vararg params: String): HashMap<String, String>? {
                try {
                    val res = OkhttpRequestUtils.doPostRequest(url, jsonObject)
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
                    when (Integer.parseInt(res["code"])) {
                        200 -> {
                            UtilityClass.hideDialog()
                            response_str = res["response"]
                            if (response_str != null) {
                                Log.e("RESPONSE STRING:>", response_str)
                                try {
                                    val jsonObject = JSONObject(response_str)
                                    if (jsonObject != null && jsonObject.optBoolean("success")
                                    ) {
                                        val dataObject = jsonObject.optJSONObject("data")
                                        // OTP Verified Successfully
                                        if (jsonObject.has("valid")) {
                                            if (jsonObject.optBoolean("valid")) {
                                                UtilityClass.showToast(
                                                    this@ForgotActivity,
                                                    getString(R.string.otp_verfied)
                                                )
                                                // show Reset Password Screen
                                                //Cancel Timer If Its running
                                                myCountDownTimer?.cancel()
                                                showResetPasswordScreen()
                                            }
                                        } else {
                                            if (dataObject != null && dataObject.optBoolean("valid")) {
                                                UtilityClass.showToast(
                                                    this@ForgotActivity,
                                                    getString(R.string.otp_verfied)
                                                )
                                                // show Reset Password Screen
                                                showResetPasswordScreen()
                                            }
                                        }

                                    } else {
                                        UtilityClass.showToast(
                                            this@ForgotActivity,
                                            getString(R.string.otp_failed)
                                        )
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()
                        }
                        500 -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING + " 500")
                            UtilityClass.hideDialog()
                        }
                        //Default case of when
                        else -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING)
                            UtilityClass.hideDialog()
                        }
                    }
                } else {
                    UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING)
                    UtilityClass.hideDialog()
                }
            }
        }.execute()

    }

    // OTP Verification Code :
    @SuppressLint("StaticFieldLeak")
    fun getResetPasswordResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@ForgotActivity)
            }

            override fun doInBackground(vararg params: String): HashMap<String, String>? {
                try {
                    val res = OkhttpRequestUtils.doPostRequest(url, jsonObject)
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
                    when (Integer.parseInt(res["code"])) {
                        200 -> {
                            UtilityClass.hideDialog()
                            response_str = res["response"]
                            if (response_str != null) {
                                Log.e("RESPONSE STRING:>", response_str)
                                try {
                                    val jsonObject = JSONObject(response_str)
                                    if (jsonObject != null && jsonObject.optBoolean("success") && jsonObject.optInt("statusCode") == 200) {
                                        UtilityClass.showToast(
                                            this@ForgotActivity,
                                            getString(R.string.password_change_success)
                                        )

                                    } else {
                                        UtilityClass.showToast(
                                            this@ForgotActivity,
                                            getString(R.string.password_reset_failed)
                                        )
                                    }
                                    finish()
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                        400 -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()
                        }
                        500 -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING + " 500")
                            UtilityClass.hideDialog()
                        }
                        //Default case of when
                        else -> {
                            UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING)
                            UtilityClass.hideDialog()
                        }
                    }
                } else {
                    UtilityClass.showToast(this@ForgotActivity, UtilityClass.OOPS_STRING)
                    UtilityClass.hideDialog()
                }
            }
        }.execute()

    }

    fun showResetPasswordScreen() {
        rlSendOTP.visibility = View.GONE
        rlOTPVerification.visibility = View.GONE
        rlResetPassScreen.visibility = View.VISIBLE
    }

    // MyCountDown Timer Classes
    inner class MyCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            val progress = (millisUntilFinished / 1000).toInt()
            //progressBar.setProgress(progressBar.getMax() - progress)
            btnResendVerificationCode.text = "Resend Verification Code (".plus(" $progress )")
        }

        override fun onFinish() {
            // finish()
            Log.e("Timer", "onFinish")
            btnResendVerificationCode.text = "Resend Verification Code"
            // btnResendVerificationCode.setBackgroundResource(R.drawable.bg_round_border_orange)
            btnResendVerificationCode.setTextColor(resources.getColor(R.color.colorOrange))
            btnResendVerificationCode.isEnabled = true
        }

    }
}
