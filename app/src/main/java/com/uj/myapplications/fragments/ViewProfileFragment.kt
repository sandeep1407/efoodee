package com.uj.myapplications.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.activities.EditProfileActivity
import com.uj.myapplications.adapters.ImageAdapter
import com.uj.myapplications.pojo.ProfileDetails
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_view_profile.*
import org.json.JSONException
import org.json.JSONObject
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
private var response_str: String? = ""
private var message: String? = ""

/**
 * A simple [Fragment] subclass.
 *
 */
class ViewProfileFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_profile, container, false)
    }


    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {
            onResume()
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("ViewProfile", "onResume")
        intializeUI()
        if (!userVisibleHint) {
            return
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iBtn_editProfile.setOnClickListener(this)
        btn_show_followers.setOnClickListener(this)
        btn_show_reviews.setOnClickListener(this)
        // Intialize Recyler view android
        grid_view_kitchenPhotos.layoutManager = GridLayoutManager(activity, 2)
        grid_view_kitchenPhotos.hasFixedSize()
        // intializeUI()
        if (UtilityClass.isInternetAvailable(activity!!)) {
            val url = RestUrls.GET_USER_PROFILE + Config.getUserData()?.id
            Log.e("URL", url)
            getProfileDetail(url)
        } else {
            UtilityClass.showToast(activity!!, "Please check internet connection")
        }
    }

    private fun intializeUI() {
        val userDataPojo = Config.getUserData()
        if (userDataPojo != null) {
            var name = userDataPojo?.name
            if (name != null) {
                txt_user_name.text = name.title.plus("${name?.fname.plus(" ${name?.mname.plus(" ${name?.lname}")}")}")
            }
            //Address
            val userAddress = userDataPojo?.userAddress
            if (userAddress != null) {
                txt_my_nativeplace.text = userAddress?.nativeAddress
            }
            //About Me / Bio
            val profileObj = userDataPojo?.profileDetails
            if (profileObj != null) {
                //Bio
                if (profileObj.bio != null) {
                    txt_about_me.text = profileObj.bio
                } else {
                    txt_about_me.text = "N/A"
                }
                //Speciality
                if (profileObj.speciality != null) {
                    txt_my_speciality.text = profileObj.speciality
                } else {
                    txt_my_speciality.text = "N/A"
                }
                //Speciality
                if (profileObj.kitchenType != null) {
                    val kitchenType = profileObj.kitchenType
                    when (kitchenType) {
                        0 -> {
                            txt_kitchen_type.text = "Veg"
                        }
                        1 -> {
                            txt_kitchen_type.text = "Non-Veg"
                        }
                        else -> {
                            txt_kitchen_type.text = "N/A"
                        }
                    }
                } else {
                    txt_kitchen_type.text = "N/A"
                }
                val listkitchenPics = profileObj.kitchenPics
                if (!listkitchenPics.isNullOrEmpty()) {
                    lableNo_Kitchen_Pics.visibility = View.GONE
                    val imageAdapter = ImageAdapter(listkitchenPics.toList(), activity!!)
                    grid_view_kitchenPhotos.adapter = imageAdapter
                } else {
                    lableNo_Kitchen_Pics.visibility = View.VISIBLE
                }
            }

            if (Config?.getProfileCImage() != null) {
                UtilityClass.setImageFromUrlOrFileProfile(activity!!, Config.getProfileCImage()!!, profile_image)
            } else {

            }
            //When Starting with kitchen photos coming in user object set from that
            // recyclerView_imagesupload.isNestedScrollingEnabled = true
            /* val listOfImages = userDataPojo.kitchenPics
             if (!listOfImages.isNullOrEmpty()) {
                 val imageAdapter = ImageAdapter(listOfImages.toList(), activity!!)
                 grid_view_kitchenPhotos.adapter = imageAdapter
             }*/
        }
    }


    /*  override fun onResume() {
          super.onResume()
          Log.e("ViewProfile", "onResume")
          intializeUI()
      }*/

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iBtn_editProfile -> {
                startActivity(Intent(activity, EditProfileActivity::class.java))
            }
            R.id.btn_show_followers -> {
                UtilityClass.showToast(activity!!, "We will show followers of this user")
            }
            R.id.btn_show_reviews -> {
                UtilityClass.showToast(activity!!, "We will show reviews of this user")
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private fun getProfileDetail(url: String) {
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
                                                    val obj = data.optJSONObject("profile_details")
                                                    var userDetail = Gson().fromJson<ProfileDetails>(
                                                        obj.toString(),
                                                        ProfileDetails::class.java
                                                    )
                                                    if (userDetail != null) {
                                                        //SetData in to UI
                                                        setDataToFields(userDetail)
                                                        //Now Save this to User Object
                                                        var user = Config.getUserData()
                                                        user?.profileDetails = userDetail
                                                        Config.setUserData(user!!)
                                                        intializeUI()
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


    private fun setDataToFields(userDetail: ProfileDetails) {
       /* txt_user_name.text =
                userDetail.name.title.plus(userDetail?.name?.fname.plus(" " + userDetail?.name?.mname + " " + userDetail?.name?.lname))
*/    }

}
