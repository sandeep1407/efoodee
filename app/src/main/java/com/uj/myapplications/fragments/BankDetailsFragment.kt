package com.uj.myapplications.fragments


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.pojo.BankPojo
import com.uj.myapplications.pojo.ProfileDetails
import com.uj.myapplications.pojo.UserNamePojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_bank_details.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class BankDetailsFragment : Fragment() {
    var response_str: String? = ""
    var mobileNumber: String? = ""
    var displayName: String? = ""
    var message: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bank_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = Config.getUserData()
        //Hit the URL to get the bank detail if exist else load from Preferences
        if (data != null) {
            if (data.bankPojo != null) {
                intitalizeUI(data.bankPojo)
            }else{
                if (UtilityClass.isInternetAvailable(activity!!)) {
                    val url = RestUrls.GET_BANK_DETAIL + Config.getUserData()?.mobile
                    Log.e("URL", url)
                    getBankDetail(url)
                } else {
                    UtilityClass.showToast(activity!!, "Please check internet connection")
                }
            }
        }
        btnUpdateBankDetails.setOnClickListener {
            try {
                if (data != null) {
                    mobileNumber = data.mobile?.toString()
                    displayName = data.displayName?.toString()
                } else {
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (tlAccNumber.editText!!.text.toString().trim().length < 9) {
                UtilityClass.shakeItemView(tlAccNumber, activity!!)
                UtilityClass.showToast(activity!!, Constants.ERR_CANT_BE_EMPTY + " and It should be min. 9 char long")
            } else if (tlAccReEnter.editText!!.text.toString().trim().length < 9) {
                UtilityClass.shakeItemView(tlAccReEnter, activity!!)
                UtilityClass.showToast(activity!!, Constants.ERR_CANT_BE_EMPTY + " and It should be min. 9 char long")
            } else if (!(tlAccReEnter.editText!!.text.toString().trim() == tlAccNumber.editText!!.text.toString().trim())) {
                UtilityClass.shakeItemView(tlAccReEnter, activity!!)
                UtilityClass.showToast(activity!!, " Account number should be same in both the fields")
            } else if (tlIFSCCode.editText!!.text.toString().trim().length < 3) {
                UtilityClass.shakeItemView(tlIFSCCode, activity!!)
                UtilityClass.showToast(activity!!, Constants.ERR_CANT_BE_EMPTY + " and It should be min. 3 char long")
            } else if (tlBankName.editText!!.text.toString().trim().length < 3) {
                UtilityClass.shakeItemView(tlBankName, activity!!)
                UtilityClass.showToast(activity!!, Constants.ERR_CANT_BE_EMPTY + " and It should be min. 3 char long")
            } else if (tlBankBranch.editText!!.text.toString().trim().length < 3) {
                UtilityClass.shakeItemView(tlBankBranch, activity!!)
                UtilityClass.showToast(activity!!, Constants.ERR_CANT_BE_EMPTY + " and It should be min. 3 char long")
            } else {
                val jsonObject = JSONObject()
                try {
                    jsonObject.put(RestTags.MOBILE, mobileNumber)
                    jsonObject.put(RestTags.DISPLAY_NAME, displayName)
                    jsonObject.put(RestTags.ACC_NUMBER, tlAccNumber.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.IFSC_CODE, tlIFSCCode.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.BANK_NAME, tlBankName.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.ADDRESS, tlBankBranch.editText!!.text.toString().trim())
                    jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
                    jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                    Timber.e(jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (UtilityClass.isInternetAvailable(activity!!)) {
                    getResponse(RestUrls.UPDATE_BANKDETAILS, jsonObject)
                } else {
                    UtilityClass.showToast(activity!!, getString(R.string.device_offline_message))
                }
            }
        }
    }

    private fun intitalizeUI(pojo: BankPojo) {
        if (pojo != null) {
            tlAccNumber.editText?.setText(pojo.accountNo)
            tlBankBranch.editText?.setText(pojo.address)
            tlAccReEnter.editText?.setText(pojo.accountNo)
            tlBankName.editText?.setText(pojo.bankName)
            tlIFSCCode.editText?.setText(pojo.ifscCode)
        }

    }


    @SuppressLint("StaticFieldLeak")
    fun getResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
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
                                                UtilityClass.showToast(
                                                    activity!!,
                                                    if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                )
                                                //Save These Details to User Object
                                                val userDataPojo = Config.getUserData()
                                                val bankPojo = BankPojo()
                                                bankPojo.accountNo = tlAccNumber.editText?.text.toString().trim()
                                                bankPojo.address = tlBankBranch.editText?.text.toString().trim()
                                                bankPojo.bankName = tlBankName.editText?.text.toString().trim()
                                                bankPojo.ifscCode = tlIFSCCode.editText?.text.toString().trim()
                                                if (userDataPojo != null) {
                                                    userDataPojo.bankPojo = bankPojo
                                                    userDataPojo.bankDetailsEntered = true
                                                }
                                                Config.setUserData(userDataPojo!!)
                                            } else {
                                                UtilityClass.showToast(
                                                    activity!!,
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
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
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
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }


    @SuppressLint("StaticFieldLeak")
    private fun getBankDetail(url: String) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): java.util.HashMap<String, String>? {
                try {
                    val token = Config.geToken()
                    val res = OkhttpRequestUtils.doGetRequest(url, token, RestTags.S_FLAG)
                    if (res != null) {
                        val stringHashMap = java.util.HashMap<String, String>()
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

            override fun onPostExecute(res: java.util.HashMap<String, String>?) {
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
                                                val data = jsonObject.optJSONObject("data")
                                                if (data != null) {
                                                    val obj = data.optJSONObject("bank_details")
                                                    val nameobj = data.optJSONObject("name")
                                                    var nameDetail = Gson().fromJson<UserNamePojo>(
                                                        obj.toString(),
                                                        UserNamePojo::class.java
                                                    )

                                                    var bankDetail = Gson().fromJson<BankPojo>(
                                                        obj.toString(),
                                                        BankPojo::class.java
                                                    )
                                                    if (bankDetail != null) {
                                                        //SetData in to UI
                                                        // Set Name in display name of account holder
                                                        if (nameDetail != null) {
                                                            var name =
                                                                nameDetail.title + " " + nameDetail.fname + " " + nameDetail.mname + " " + nameDetail.lname
                                                            if (!name.isNullOrEmpty()) {
                                                                bankDetail.displayName = name
                                                            } else {
                                                                name = ""
                                                            }
                                                        }
                                                        intitalizeUI(bankDetail)
                                                        //  setDataToFields(bankDetail)
                                                        //Now Save this to User Object
                                                        var user = Config.getUserData()
                                                        user?.bankPojo = bankDetail
                                                        Config.setUserData(user!!)
                                                        //intializeUI()
                                                    }
                                                }

                                                UtilityClass.showToast(
                                                    activity!!,
                                                    if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                )
                                            } else {
                                                UtilityClass.showToast(
                                                    activity!!,
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
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        activity!!,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
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
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    /*   private fun setDataToFields(bankDetail: BankPojo) {
           if(bankDetail!=null){

           }
       }*/


}
