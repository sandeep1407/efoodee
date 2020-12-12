package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.pojo.*
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.HashMap
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import java.math.BigInteger


class RegistrationActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Toast.makeText(this, title[position], Toast.LENGTH_LONG).show();
        flagTitle = position

    }

    private var response_str: String? = ""
    private var message: String? = ""
    var OTP: String? = null
    var flagTitle: Int = 0 //0 = Mr. and 1 = Mrs.
    var flag_resend: Int = 0
    var myCountDownTimer: RegistrationCountDownTimer? = null
    var TIMECOUNTTIMER: Long = 120000 //120 SEConds
    var INTERVALTIMER: Long = 1000
    var title = arrayOf("Mr.", "Mrs.")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        var intent = intent;
        if (intent != null) {
            var isvalidSession = intent.getStringExtra("SESSION_NOT_VALID")
            if (isvalidSession != null && isvalidSession.equals("SESSION_NOT_VALID")) {
                //Than Make Myma view Visible and do the Same
                rlRegisterPage1.visibility = View.GONE
                rlRegisterPage2.visibility = View.VISIBLE
                rlOTPVerification.visibility = View.GONE
            }
        }
        btnRegisterPage1.setOnClickListener(this)
        ibtnRegisterClose.setOnClickListener(this)
        ibtnOTPVerification.setOnClickListener(this)
        btnContinue.setOnClickListener(this)
        btnRegisterPage2.setOnClickListener(this)
        btnResendVerificationCode.setOnClickListener(this)
        val normalLinkClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@RegistrationActivity, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "TANDC")
                startActivity(intent)
            }
        }
        val normalLinkClickSpan1 = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@RegistrationActivity, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "PP")
                startActivity(intent)
            }
        }
        makeLinks(
            tvCheckPolicy,
            arrayOf("Terms and Conditions", "Privacy Policy"),
            arrayOf(normalLinkClickSpan, normalLinkClickSpan1)
        )
        spinner1.onItemSelectedListener = this
        //Creating the ArrayAdapter instance having the country list
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, title)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinner1.adapter = aa
    }

    fun makeLinks(textView: TextView, links: Array<String>, clickableSpans: Array<ClickableSpan>) {
        var spannableString = SpannableString(textView.text)
        links.forEachIndexed { index, element ->
            var clickableSpan = clickableSpans[index]
            var link = links[index]
            var startIndexOfLink = textView.text.toString().indexOf(link)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink,
                startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.highlightColor = Color.TRANSPARENT// prevent TextView change background when highlight
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)

    }

    @SuppressLint("StaticFieldLeak")
    fun getResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@RegistrationActivity)
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
                    var jsonObject: JSONObject? = null
                    response_str = res["response"]
                    if (!response_str.isNullOrEmpty()) {
                        try {
                            Log.e("RESPONSE STRING:>", response_str)
                            jsonObject = JSONObject(response_str)
                            if (jsonObject.has("msg")) {
                                message = jsonObject.optString("msg")
                            }
                            // When Clause Start
                            when (Integer.parseInt(res["code"]!!)) {
                                200 -> {
                                    UtilityClass.hideDialog()
                                    if (response_str != null) {
                                        try {
                                            jsonObject = JSONObject(response_str)
                                            if (jsonObject != null) {
                                                val commonRegisterResponsePojo =
                                                    Gson().fromJson<CommonRegisterResponsePojo>(
                                                        jsonObject.toString(),
                                                        CommonRegisterResponsePojo::class.java
                                                    )
                                                if (commonRegisterResponsePojo != null && commonRegisterResponsePojo.success) {
                                                    val otppojo: OtpPojo = commonRegisterResponsePojo.data
                                                    OTP = otppojo.otp
                                                    if (!commonRegisterResponsePojo.msg.isNullOrEmpty()) {
                                                        UtilityClass.showToast(
                                                            this@RegistrationActivity,
                                                            if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                        )
                                                    } else {
                                                        UtilityClass.showToast(
                                                            this@RegistrationActivity,
                                                            if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                        )
                                                    }
                                                    //Make OTP Screen Visible
                                                    // Make Text change
                                                    // Resend Situation
                                                    if (flag_resend == 101) {
                                                        /*  UtilityClass.showToast(
                                                              this@RegistrationActivity,
                                                              getString(R.string.otp_sent_success)
                                                          )*/
                                                        //Start Timer(In Both the Cases)
                                                        // (Disable Button Again for 120 Sec)
                                                        btnResendVerificationCode.setTextColor(resources.getColor(R.color.colorGrey))
                                                        btnResendVerificationCode.isEnabled = false
                                                        myCountDownTimer = RegistrationCountDownTimer(
                                                            TIMECOUNTTIMER,
                                                            INTERVALTIMER
                                                        )
                                                        myCountDownTimer?.start()
                                                    } else {
                                                        val mobile_num =
                                                            tlMobileNumber.editText!!.text.toString().trim()
                                                        txt_number.text = " $mobile_num"
                                                        rlRegisterPage1.visibility = View.GONE
                                                        rlOTPVerification.visibility = View.VISIBLE
                                                        rlRegisterPage2.visibility = View.GONE
                                                        //Start Timer
                                                        myCountDownTimer = RegistrationCountDownTimer(
                                                            TIMECOUNTTIMER,
                                                            INTERVALTIMER
                                                        )
                                                        myCountDownTimer?.start()
                                                    }
                                                } else {
                                                    UtilityClass.showToast(
                                                        this@RegistrationActivity,
                                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                    )
                                                }
                                            }
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                                400 -> {
                                    UtilityClass.showToast(
                                        this@RegistrationActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        this@RegistrationActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        this@RegistrationActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                } else {
                    UtilityClass.showToast(
                        this@RegistrationActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnRegisterPage1 -> {
                if (!tlMobileNumber.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, getString(R.string.mobile_empty_message))
                } else if (tlMobileNumber.editText!!.text.toString().trim().length < 10) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, "Invalid Mobile Number.")
                } else if (!tlPassword.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(this, "Password " + Constants.ERR_CANT_BE_EMPTY)
                } else if (tlPassword.editText!!.text.toString().length < 8) {
                    UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(this, getString(R.string.password_invalid_message))
                } else if (!UtilityClass.isValidPassword(tlPassword.editText!!.text.toString())) {
                    UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(this, getString(R.string.password_invalid_message))
                } else if (!tlConfirmPassword.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlConfirmPassword, this)
                    UtilityClass.showToast(this, "Confirm Password " + Constants.ERR_CANT_BE_EMPTY)
                } else if (tlPassword.editText!!.text.toString().trim() != tlConfirmPassword.editText!!.text.toString().trim()) {
                    UtilityClass.showToast(this, getString(R.string.password_not_match_message))
                } else {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.PASSWORD, tlPassword.editText!!.text.toString().trim())
                        jsonObject.put("confirm_password", tlConfirmPassword.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
                        jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                        Timber.e(jsonObject.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (UtilityClass.isInternetAvailable(this)) {
                        getResponse(RestUrls.SIGN_UP_URL, jsonObject)
                    } else {
                        UtilityClass.showToast(this, getString(R.string.device_offline_message))
                    }
                }
            }
            R.id.ibtnRegisterClose -> {
                onBackPressed()
            }
            R.id.btnResendVerificationCode -> {
                // Resend Call Again Register Apis
                editotpview.clearTextFromView()
                flag_resend = 101
                val jsonObject = JSONObject()
                try {
                    jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.PASSWORD, tlPassword.editText!!.text.toString().trim())
                    jsonObject.put("confirm_password", tlConfirmPassword.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
                    jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                    Timber.e(jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (UtilityClass.isInternetAvailable(this)) {
                    getResponse(RestUrls.SIGN_UP_URL, jsonObject)
                } else {
                    UtilityClass.showToast(this, getString(R.string.device_offline_message))
                }
            }
            R.id.ibtnOTPVerification -> {
                onBackPressed()
            }
            R.id.btnRegisterPage2 -> {
                //GO To Service:
                when {
                    tlYourName.editText!!.text.isNullOrEmpty() -> {
                        UtilityClass.shakeItemView(tlYourName, this)
                        UtilityClass.showToast(this, "Name " + Constants.ERR_CANT_BE_EMPTY)
                    }
                    (tlYourName.editText!!.text.toString().trim().length < 2) -> {
                        UtilityClass.shakeItemView(tlYourName, this)
                        UtilityClass.showToast(this, "Name should be more than 2 characters long")
                    }
                    (tlMiddleName.editText!!.text.toString().trim().length < 2) -> {
                        UtilityClass.shakeItemView(tlMiddleName, this)
                        UtilityClass.showToast(this, "Middle Name should be more than 2 characters long")
                    }
                    (tlSurNAme.editText!!.text.toString().trim().length < 2) -> {
                        UtilityClass.shakeItemView(tlSurNAme, this)
                        UtilityClass.showToast(this, "Surname should be more than 2 characters long")
                    }
                    /*  tlEmail.editText!!.text.isNullOrEmpty() -> {
                          UtilityClass.shakeItemView(tlEmail, this)
                          UtilityClass.showToast(this, getString(R.string.email_empty_message))
                      }
                      !UtilityClass.isValidEmail(tlEmail.editText!!.text) -> {
                          UtilityClass.shakeItemView(tlEmail, this)
                          UtilityClass.showToast(this, getString(R.string.email_invalid_message))
                      }*/
                    tlAddress.editText!!.text.isNullOrEmpty() -> {
                        UtilityClass.shakeItemView(tlAddress, this)
                        UtilityClass.showToast(this, "Address " + Constants.ERR_CANT_BE_EMPTY)
                    }

                    (tlAddress.editText!!.text.toString().trim().length < 3) -> {
                        //UtilityClass.shakeItemView(tlAddress, this)
                        UtilityClass.showToast(this, "Address should be more than 3 characters long")
                    }
                    (etAddressLocality!!.text.toString().trim().length < 2) -> {
                        // UtilityClass.shakeItemView(tlAddress, this)
                        UtilityClass.showToast(this, "City should be more than 2 characters long")
                    }
                    (etAddressPinCode!!.text.toString().trim().length < 6) -> {
                        //UtilityClass.shakeItemView(tlAddress, this)
                        UtilityClass.showToast(this, "Pincode should not be empty and should be 6 digit long.")
                    }
                    (tlAddressNativePlaceName.editText!!.text.toString().trim().length < 2) -> {
                        // UtilityClass.shakeItemView(tlAddressNativePlaceName, this)
                        UtilityClass.showToast(
                            this,
                            "Native Address should be more than 2 characters long or It Should not be Empty"
                        )
                    }
                    (tlAddressNativePinCode.editText!!.text.toString().trim().length < 6) -> {
                        // UtilityClass.shakeItemView(tlAddressNativePlaceName, this)
                        UtilityClass.showToast(
                            this,
                            "Native Pincode should not be Empty and should be 6 digit long"
                        )
                    }
                    else -> {
                        //Save MenuDataPojo User model and then proceed further
                        var userObj =
                            Config.getUserData()// Use Previous UserData Object Only So that values cannot be be Override
                        if (userObj != null) {
                            userObj.displayName = tlYourName.editText!!.text.toString().trim()
                            var namePojo = UserNamePojo()
                            var address = UserAddressPojo()
                            // Client didnt have the UI for F Name and Last name here on Registration Page.
                            namePojo.fname = tlYourName.editText!!.text.toString()
                                .trim()// Right now we have to set this kind of name only.
                            namePojo.mname = tlMiddleName.editText!!.text.toString()
                                .trim()
                            namePojo.lname = tlSurNAme.editText!!.text.toString()
                                .trim()
                            if (flagTitle == 0) {
                                namePojo.title = "Mr."
                            } else {
                                namePojo.title = "Mrs."
                            }
                            userObj.name = namePojo
                            address.line1 = tlAddress.editText!!.text.toString().trim()
                            address.line2 = etAddressLine2!!.text.toString().trim()
                            address.zip = etAddressPinCode!!.text.toString().trim()
                            address.landmark = etAddressLocality!!.text.toString().trim()
                            address.city = etAddressLocality!!.text.toString().trim()
                            address.state = ""
                            address.nativeAddress = tlAddressNativePlaceName.editText!!.text.toString().trim()
                            address.nativePinCode = tlAddressNativePinCode.editText!!.text.toString().trim()
                            userObj.userEmail = tlEmail.editText!!.text.toString().trim()
                            // Save Address
                            userObj.userAddress = address
                            //Native Places
                            val nativePlaces = NativePlaces()
                            nativePlaces.name = tlAddressNativePlaceName.editText!!.text.toString().trim()
                            nativePlaces.pin = BigInteger(tlAddressNativePinCode.editText!!.text.toString().trim())
                            userObj.nativePlaces = nativePlaces
                            Config.setUserData(userObj)
                            val intent = Intent(this@RegistrationActivity, ServicesActivity::class.java)
                            intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            userObj = UserDataPojo()
                            userObj.displayName = tlYourName.editText!!.text.toString().trim()
                            var namePojo = UserNamePojo()
                            var address = UserAddressPojo()
                            // Client didnt have the UI for F Name and Last name here on Registration Page.
                            namePojo.fname = tlYourName.editText!!.text.toString()
                                .trim()// Right now we have to set this kind of name only.
                            namePojo.mname = tlMiddleName.editText!!.text.toString()
                                .trim()
                            namePojo.lname = tlSurNAme.editText!!.text.toString()
                                .trim()
                            if (flagTitle == 0) {
                                namePojo.title = "Mr."
                            } else {
                                namePojo.title = "Mrs."
                            }
                            userObj.name = namePojo
                            address.line1 = tlAddress.editText!!.text.toString().trim()
                            address.line2 = etAddressLine2!!.text.toString().trim()
                            address.zip = etAddressPinCode!!.text.toString().trim()
                            address.landmark = etAddressLocality!!.text.toString().trim()
                            address.city = etAddressLocality!!.text.toString().trim()
                            address.state = ""
                            address.nativeAddress = tlAddressNativePlaceName.editText!!.text.toString().trim()
                            address.nativePinCode = tlAddressNativePinCode.editText!!.text.toString().trim()
                            userObj.userEmail = tlEmail.editText!!.text.toString().trim()
                            // Save Address
                            userObj.userAddress = address
                            Config.setUserData(userObj)
                            val intent = Intent(this@RegistrationActivity, ServicesActivity::class.java)
                            intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }

                    }
                }

            }

            R.id.btnContinue -> {
                // onBackPressed()
                // OTP Screen Click and in  validate the same
                when {
                    OTP.isNullOrEmpty() -> {
                        UtilityClass.shakeItemView(editotpview, this)
                        UtilityClass.showToast(this, "OTP " + Constants.ERR_CANT_BE_EMPTY)
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
                            jsonObject.put(RestTags.OTP_TYPE, RestTags.OTP_TYPE_REGISTER)
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
        }
    }


    // OTP Verification Code :
    @SuppressLint("StaticFieldLeak")
    fun getOTPVerifyResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@RegistrationActivity)
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
                                                    this@RegistrationActivity,
                                                    getString(R.string.otp_verfied)
                                                )
                                                getLoginResponse(RestUrls.LOGIN_URL, makeJsonForLogin())
                                            }
                                        } else {
                                            if (dataObject != null && dataObject.optBoolean("valid")) {
                                                UtilityClass.showToast(
                                                    this@RegistrationActivity,
                                                    getString(R.string.otp_verfied)
                                                )
                                                getLoginResponse(RestUrls.LOGIN_URL, makeJsonForLogin())
                                            }
                                        }

                                    } else {
                                        UtilityClass.showToast(
                                            this@RegistrationActivity,
                                            getString(R.string.otp_failed)
                                        )
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()
                        }
                        500 -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING + " 500")
                            UtilityClass.hideDialog()
                        }
                        //Default case of when
                        else -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING)
                            UtilityClass.hideDialog()
                        }
                    }
                } else {
                    UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING)
                    UtilityClass.hideDialog()
                }
            }
        }.execute()

    }

    @SuppressLint("StaticFieldLeak")
    fun getLoginResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@RegistrationActivity)
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
                                    if (jsonObject != null && jsonObject.optInt("statusCode") == 200
                                    ) {
                                        val loginResponse = Gson().fromJson<LoginResponse>(
                                            jsonObject.toString(),
                                            LoginResponse::class.java
                                        )
                                        if (loginResponse.success && loginResponse.statusCode == 200) {
                                            val data = loginResponse.data
                                            if (data != null && data.token != null) {
                                                Config.setToken(data.token)
                                                Config.setUserLogin(true)
                                                Config.setUserData(data.user)
                                                // Load New Screen
                                                rlRegisterPage2.visibility = View.VISIBLE
                                                rlRegisterPage1.visibility = View.GONE
                                                rlOTPVerification.visibility = View.GONE
                                                // Load mew View
                                            }
                                        } else {
                                            UtilityClass.showToast(
                                                this@RegistrationActivity,
                                                UtilityClass.OOPS_STRING + " 404"
                                            )

                                            UtilityClass.hideDialog()
                                            // Load New Screen
                                            rlRegisterPage2.visibility = View.VISIBLE
                                            rlRegisterPage1.visibility = View.GONE
                                            rlOTPVerification.visibility = View.GONE
                                            // Load mew View
                                        }

                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()
                        }
                        500 -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING + " 500")
                            UtilityClass.hideDialog()
                        }
                        //Default case of when
                        else -> {
                            UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING)
                            UtilityClass.hideDialog()
                        }
                    }
                } else {
                    UtilityClass.showToast(this@RegistrationActivity, UtilityClass.OOPS_STRING)
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    fun makeJsonForLogin(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
            jsonObject.put(RestTags.PASSWORD, tlPassword.editText!!.text.toString().trim())
            jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
            jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)//TODO
            Timber.e(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }


    // MyCountDown Timer Classes
    inner class RegistrationCountDownTimer(millisInFuture: Long, countDownInterval: Long) :
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
