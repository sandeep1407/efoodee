package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.pojo.LoginResponse
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.lang.Exception
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var response_str: String? = ""
    private var message: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
        btnForgotPassword.setOnClickListener(this)
    }


    @SuppressLint("StaticFieldLeak")
    fun getResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@LoginActivity)
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
                if (res != null) {
                    response_str = res["response"]
                    var jsonObject = JSONObject(response_str)
                    if (jsonObject.has("msg")) {
                        message = jsonObject.optString("msg")
                    }
                    when (Integer.parseInt(res["code"])) {
                        200 -> {
                            UtilityClass.hideDialog()
                            response_str = res["response"]
                            if (response_str != null) {
                                Log.e("RESPONSE STRING:>", response_str)
                                try {
                                    jsonObject = JSONObject(response_str)
                                    if (jsonObject != null) {
                                        /*  if (jsonObject.optBoolean(StaticData.STATUS)) {
                                              message = jsonObject.optString(StaticData.MESSAGE)
                                              val jsonObject_data = jsonObject.optJSONObject(StaticData.DATA)
                                              if (jsonObject_data != null) {
                                                  userModal = Gson().fromJson<UserModal>(
                                                      jsonObject_data!!.toString(),
                                                      UserModal::class.java!!
                                                  )
                                                  activeUtils.displayToast(context, "Login Successfully")
                                                  PrefUtils.clearPreferences(context)
                                                  PrefUtils.setUserLoginMode(true, this@LoginActivity)
                                                  PrefUtils.setEmail(context, userModal.getEmail())
                                                  PrefUtils.setUserLoggedWay(context, StaticData.EMAIL)
                                                  PrefUtils.setUserdata(this@LoginActivity, Gson().toJson(userModal))
                                                  PrefUtils.setUserID(context, userModal.getUserid() + "")
                                                  val intent = Intent(this@LoginActivity, MainClass::class.java)
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                  startActivity(intent)
                                                  finish()
                                              }
                                          } else {
                                              try {
                                                  message = JSONObject(res["response"]).getString("message")
                                              } catch (e: Exception) {
                                                  e.printStackTrace()
                                              }

                                              ActiveUtils.showSnackBar(
                                                  coordinatorLayoutLogin,
                                                  message ?: "Server refused the request",
                                                  Snackbar.LENGTH_LONG
                                              )
                                          }*/
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            UtilityClass.showToast(this@LoginActivity, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()

                        }
                        500 -> {
                            UtilityClass.showToast(this@LoginActivity, UtilityClass.OOPS_STRING + " 500")
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
                            UtilityClass.showToast(this@LoginActivity, UtilityClass.OOPS_STRING)
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

    fun resetFields() {
        etMobileNumber.setText("")
        etPassword.setText("")
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnForgotPassword -> {
                resetFields()
                startActivity(Intent(this, ForgotActivity::class.java))
            }

            R.id.btnRegister -> {
                resetFields()
                startActivity(Intent(this, RegistrationActivity::class.java))
            }
            R.id.btnLogin -> {
                //Go To Dashboard
                // 1.internet connction check
                //2. Validate Feilds
                //3.. Send to server
                // Save data and token in shared preferences
                if (!tlMobileNumber.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, "Mobile Number " + Constants.ERR_CANT_BE_EMPTY)
                } else if (tlMobileNumber.editText!!.text.toString().trim().length < 10) {
                    UtilityClass.shakeItemView(tlMobileNumber, this)
                    UtilityClass.showToast(this, "Invalid Mobile Number.")
                } else if (!tlPassword.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(this, "Password " + Constants.ERR_CANT_BE_EMPTY)
                }/* else if (tlPassword.editText!!.text.toString().trim().length < 8) {
                    UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(this, "Password Must be 8 to 20 char long in length.")
                }*/ else {
                    val jsonObject = JSONObject()
                    try {
                        jsonObject.put(RestTags.MOBILE, tlMobileNumber.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.PASSWORD, tlPassword.editText!!.text.toString().trim())
                        jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
                        jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)//TODO
                        Timber.e(jsonObject.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    if (UtilityClass.isInternetAvailable(this)) {
                        //  getResponse(RestUrls.LOGIN_URL, jsonObject)
                        getLoginResponse(RestUrls.LOGIN_URL, jsonObject)
                    } else {
                        UtilityClass.showToast(this, getString(R.string.device_offline_message))
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    fun getLoginResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@LoginActivity)
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
                            // Here We can handle the code
                            when (Integer.parseInt(res["code"]!!)) {
                                200 -> {
                                    UtilityClass.hideDialog()
                                    if (response_str != null) {
                                        try {
                                            jsonObject = JSONObject(response_str)
                                            if (jsonObject != null && jsonObject.optInt("statusCode") == 200
                                            ) {
                                                val loginResponse = Gson().fromJson<LoginResponse>(
                                                    jsonObject.toString(),
                                                    LoginResponse::class.java
                                                )
                                                if (loginResponse.success && loginResponse.statusCode == 200) {
                                                    val data = loginResponse.data
                                                    if (data != null && data.token != null) {
                                                        UtilityClass.showToast(
                                                            this@LoginActivity,
                                                            if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                        )
                                                        Config.setToken(data.token)
                                                        Config.setUserLogin(true)
                                                        Config.setUserData(data.user)
                                                        /*Now get User Object*/
                                                        val user = Config.getUserData()
                                                        if (user != null) {
                                                            var fssai = user?.fssai
                                                            if (fssai != null) {
                                                                val documents = data.user?.documents
                                                                if (documents != null) {
                                                                    if (documents.fssaiDoc != null) {
                                                                        fssai.userFssaiImagepath = documents.fssaiDoc
                                                                    }
                                                                }
                                                            }
                                                            //fssai.userFssaiImagepath = data.user?.documents?.fssaiDoc
                                                            val documents = data.user?.documents
                                                            if (documents != null) {
                                                                if (documents.pan != null) {
                                                                    Config.setPANImage(documents.pan!!)
                                                                }
                                                                if (documents.fssaiDoc != null) {
                                                                    Config.setFSSAIImage(documents.fssaiDoc!!)
                                                                }
                                                                if (documents.adhaar != null) {
                                                                    Config.setADHAARImage(documents.adhaar!!)
                                                                }
                                                                if (documents.bikeRc != null) {
                                                                    Config.setBIKERCImage(documents.bikeRc!!)
                                                                }
                                                                if (documents.drivingLicense != null) {
                                                                    Config.setDLImage(documents.drivingLicense!!)
                                                                }
                                                            }
                                                            val user = data.user
                                                            if (user != null) {
                                                                val profileImage = user.profilePic
                                                                if (profileImage != null) {
                                                                    Config.setProfileImage(profileImage)
                                                                }
                                                            }
                                                            /*  val profImage = data.user?.profilePic!!
                                                              if (profImage != null) {
                                                                  Config.setProfileImage(data.user?.profilePic!!)
                                                              }*/
                                                            //Config.setFSSAIImage(data.user?.documents?.fssaiDoc!!)
                                                            //Config.setDLImage(data.user?.documents?.fssaiDoc!!)
                                                            // Config.setBIKERCImage(data.user?.documents?.fssaiDoc!!)

                                                        }
                                                        Config.setUserData(user!!)
                                                        var intent =
                                                            Intent(this@LoginActivity, NavigationActivity::class.java)
                                                        intent.flags =
                                                                Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                        startActivity(intent)
                                                        finish()
                                                    }
                                                } else {
                                                    UtilityClass.showToast(
                                                        this@LoginActivity,
                                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                    )
                                                    UtilityClass.hideDialog()
                                                }

                                            } else {
                                                UtilityClass.showToast(
                                                    this@LoginActivity,
                                                    if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                )
                                            }
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                                400 -> {
                                    UtilityClass.showToast(
                                        this@LoginActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        this@LoginActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        this@LoginActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {

                    }

                } else {
                    UtilityClass.showToast(
                        this@LoginActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }


}
