package com.uj.myapplications.fragments.TabsFragment


import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson

import com.uj.myapplications.R
import com.uj.myapplications.adapters.MutipleAdapter
import com.uj.myapplications.pojo.MenuPojo.MenuComResponsePojo
import com.uj.myapplications.pojo.MenuPojo.MenuDetailPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_all_menu_current.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class AllMenuCurrentFragment : Fragment() {
    private var response_str: String? = ""
    private var message: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_menu_current, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creates a vertical Layout Manager
        recyclerView_all_current_menu.layoutManager = LinearLayoutManager(activity)
        recyclerView_all_current_menu.hasFixedSize()
        // Hit web call and than set it to recycler view
        val jsonObject = JSONObject()
        try {
            jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
            jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
            jsonObject.put(RestTags.TYPE, RestTags.CURRENT)
            jsonObject.put(RestTags.PAGE, RestTags.PAGE_MIN_LENGTH)
            jsonObject.put(RestTags.PER_PAGE, RestTags.PAGE_MAX_LENGTH)
            Timber.e(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (UtilityClass.isInternetAvailable(activity!!)) {
            //  getResponse(RestUrls.LOGIN_URL, jsonObject)
            getMenuResponse(RestUrls.GET_MENU, jsonObject)
        } else {
            UtilityClass.showToast(activity!!, getString(R.string.device_offline_message))
        }
    }

    fun setAdapter(menuList: MutableList<MenuDetailPojo>) {
        // Access the RecyclerView Adapter and load the data into it
        val adapter = MutipleAdapter(activity!!, menuList, RestTags.CURRENT)
        recyclerView_all_current_menu.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    @SuppressLint("StaticFieldLeak")
    fun getMenuResponse(url: String, jsonObject: JSONObject) {
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
                                                var comResponsePojo = Gson().fromJson<MenuComResponsePojo>(
                                                    jsonObject.toString(),
                                                    MenuComResponsePojo::class.java
                                                )
                                                val dataPojo = comResponsePojo.menuDataPojo
                                                if (dataPojo != null) {
                                                    val menuList = dataPojo.menuDetailPojos
                                                    // SetAdapter to Recycler view
                                                    if (menuList != null && menuList.size > 0) {
                                                        setAdapter(menuList)
                                                    } else {
                                                        txt_no_records.visibility = View.VISIBLE
                                                    }

                                                } else {
                                                    txt_no_records.visibility = View.VISIBLE
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


}
