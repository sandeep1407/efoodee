package com.uj.myapplications.fragments


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
import com.uj.myapplications.adapters.ReviewsAdapter
import com.uj.myapplications.pojo.MenuPojo.MenuDetailPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_reviews.*
import org.json.JSONException
import org.json.JSONObject
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
class ReviewsFragment : Fragment() {
    // Initializing an empty ArrayList to be filled with animals
    val names: ArrayList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        if (args != null) {
            when (args.getString(RestTags.FROM)) {
                "order" -> {
                    if (UtilityClass.isInternetAvailable(activity!!)) {
                        val jsonObject = JSONObject()
                        jsonObject.put("order_id",args.getString("orderId"))
                        getReviews(RestUrls.ASK_FOR_REVIEW,jsonObject)
                    } else {
                        UtilityClass.showToast(activity!!, "Please check internet connection")
                    }
                }
                else -> {
                    //Do Nothing
                    addNames()
                }
            }
        }

        // Creates a vertical Layout Manager
        recyclerView_reviews.layoutManager = LinearLayoutManager(activity)
        // Access the RecyclerView Adapter and load the data into it
        recyclerView_reviews.adapter = ReviewsAdapter(names, activity!!)
    }

    private fun addNames() {
        // Adds animals to the empty animals ArrayList
        names.add("Kavya Jain")
        names.add("Seema Thakur")
        names.add("Siri Singh Randhawa")
        names.add("Shalini Gupta")
        names.add("Kamala Sharma")
    }
    private var response_str: String? = ""
    private var message: String? = ""
    @SuppressLint("StaticFieldLeak")
    private fun getReviews(url: String, obj: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): java.util.HashMap<String, String>? {
                try {
                    val token = Config.geToken()
                    val res = OkhttpRequestUtils.doPostRequest(url, obj, token, RestTags.S_FLAG,RestTags.PUBLIC_KEY)
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
                                             /*   val data = jsonObject.optJSONObject("data")
                                                var menuDetailPojo = Gson().fromJson<MenuDetailPojo>(
                                                    data.toString(),
                                                    MenuDetailPojo::class.java
                                                )
                                                if (menuDetailPojo != null) {
                                                    //SetData in to UI
                                                    setDataToFields(menuDetailPojo)
                                                }*/
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
