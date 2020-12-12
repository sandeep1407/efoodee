package com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments


import android.annotation.SuppressLint
import android.content.Context
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
import com.uj.myapplications.adapters.OrdersMultipleAdapter
import com.uj.myapplications.fragments.MyOrdersFragment
import com.uj.myapplications.pojo.OrderPojo.OrderComResponse
import com.uj.myapplications.pojo.OrderPojo.OrderDetailPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_sub__all_.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.MutableList
import kotlin.collections.set

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@SuppressLint("ValidFragment")
class Sub_All_Fragment(var dinE_IN: Int) : Fragment() {
    companion object {
        var listAll = ArrayList<OrderDetailPojo>()
        var listSelfPickup = ArrayList<OrderDetailPojo>()
        var listSelfDelivery = ArrayList<OrderDetailPojo>()
        var listMymaDelivery = ArrayList<OrderDetailPojo>()
    }

    private var fragmentResume = false
    private var fragmentVisible = false
    private var fragmentOnCreated = false
    private var response_str: String? = ""
    private var message: String? = ""
    private var id: String? = ""
    var mContext: Context? = null
    val names: ArrayList<String> = ArrayList()
    var adapter: OrdersMultipleAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub__all_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Creates a vertical Layout Manager
        recyclerView_sub_all_tab.layoutManager = LinearLayoutManager(activity)
        recyclerView_sub_all_tab.hasFixedSize()
        // Access the RecyclerView Adapter and load the data into it
        adapter = OrdersMultipleAdapter(activity!!, listAll, RestTags.DINE_IN_STR)
        recyclerView_sub_all_tab.adapter = adapter
        getDataForList()
    }

    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {   // only at fragment screen is resumed
            fragmentResume = true
            fragmentVisible = false
            fragmentOnCreated = true
            getDataForList()
        } else if (visible) {        // only at fragment onCreated
            fragmentResume = false
            fragmentVisible = true
            fragmentOnCreated = true
        } else if (!visible && fragmentOnCreated) {// only when you go out of fragment screen
            fragmentVisible = false
            fragmentResume = false
        }
    }

    private fun getDataForList() {
        if (listAll.size == 0) {
            val jsonObject = JSONObject()
            try {
                jsonObject.put(RestTags.ORDER_PROGRESS_STATUS, MyOrdersFragment.ORDERPROGRESSSTATUS)
                jsonObject.put(RestTags.SERVICE_TYPE, dinE_IN)
                jsonObject.put(RestTags.PAGE, RestTags.PAGE_MIN_LENGTH)
                jsonObject.put(RestTags.PER_PAGE, RestTags.PAGE_MAX_LENGTH)
                Timber.e(jsonObject.toString())
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            if (UtilityClass.isInternetAvailable(activity!!.applicationContext)) {
                //  getResponse(RestUrls.LOGIN_URL, jsonObject)
                getOrdersResponse(RestUrls.GET_ORDERS, jsonObject)
            } else {
                UtilityClass.showToast(activity!!, getString(R.string.device_offline_message))
            }
        } else {
            //  setAdapter(listAll, RestTags.DINE_IN_STR)
            adapter?.notifyDataSetChanged()
        }

    }

    fun setAdapter(menuList: List<OrderDetailPojo>, restTags: String) {
        // Access the RecyclerView Adapter and load the data into it
        //Hello
        adapter?.notifyDataSetChanged()
    }

    @SuppressLint("StaticFieldLeak")
    fun getOrdersResponse(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): HashMap<String, String>? {
                try {
                    val token = Config.geToken()
                    val res =
                        OkhttpRequestUtils.doPostRequest(url, jsonObject, token, RestTags.S_FLAG, RestTags.PUBLIC_KEY)
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
                                                val comResponsePojo = Gson().fromJson<OrderComResponse>(
                                                    jsonObject.toString().trim(),
                                                    OrderComResponse::class.java
                                                )
                                                val dataPojo = comResponsePojo.data
                                                if (dataPojo != null) {
                                                    listAll.clear()
                                                    listMymaDelivery.clear()
                                                    listSelfDelivery.clear()
                                                    listSelfPickup.clear()
                                                    listAll.addAll(dataPojo.all)
                                                    listMymaDelivery.addAll(dataPojo.mymaadelivery)
                                                    listSelfPickup.addAll(dataPojo.selfpickup)
                                                    listSelfDelivery.addAll(dataPojo.selfdelivery)
                                                    setAdapter(listAll, RestTags.DINE_IN_STR)
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
                            txt_no_records.visibility = View.VISIBLE
                            UtilityClass.showToast(
                                activity!!,
                                if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                            )
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
