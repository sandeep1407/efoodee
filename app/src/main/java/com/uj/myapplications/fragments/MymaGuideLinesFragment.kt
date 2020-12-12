package com.uj.myapplications.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.uj.myapplications.R
import com.uj.myapplications.activities.FullScreenDialogActivity
import com.uj.myapplications.activities.NavigationActivity
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_add_menu.*
import kotlinx.android.synthetic.main.fragment_myma_guide_lines.*
import org.json.JSONObject
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException


/**
 * A simple [Fragment] subclass.    +hf
 *
 */
class MymaGuideLinesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_myma_guide_lines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnFinishMymaGuideLines.setOnClickListener {
            if (!chk_tandc.isChecked) {
                UtilityClass.showToast(activity!!, "Please accept term and condition")
                UtilityClass.shakeItemView(chk_tandc, activity!!)


            } else {
                // Finish the Set Up and Proceed Further
                //Hit the Api for Additional Details and
                /* var userObject = Config.getUserData()
                 if (userObject != null) {
                     userObject.setIsTermAccepted(true)
                 }
                 Config.setUserData(userObject!!)*/
                ServicesFragment.userTempObj.isTermAccepted = true
                getResponse(makeJsonObjectForAdditionalDetails())
            }
        }

        iBtnCloseMymaGuidelines.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }

        val normalLinkClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                //startActivity(Intent(activity!!, FullScreenDialogActivity::class.java))
                var intent = Intent(activity!!, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "TANDC")
                startActivity(intent)
            }
        }
        val normalLinkClickSpan1 = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                // startActivity(Intent(activity!!, FullScreenDialogActivity::class.java))
                var intent = Intent(activity!!, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "PP")
                startActivity(intent)
            }
        }
        makeLinks(
            txt_tandc,
            arrayOf("Terms and Conditions", "Privacy Policy"),
            arrayOf(normalLinkClickSpan, normalLinkClickSpan1)
        )

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

    private var message: String? = ""
    @SuppressLint("StaticFieldLeak")
    fun getResponse(jsonObject: JSONObject) {
        object : AsyncTask<String, Void, JSONObject>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): JSONObject? {
                try {
                    val res =
                        OkhttpRequestUtils.userAddtionalDetailsResponse(
                            Config.geToken(),
                            Config.getUserData()?.id,
                            Config.geFSSAIImage(),
                            Config.getPANImage(),
                            Config.getADHAARImage(),
                            Config.getDLImage(),
                            Config.getBIKERCImage(),
                            jsonObject.toString()
                        )
                    if (res != null) {
                        return res
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

            override fun onPostExecute(res: JSONObject?) {
                super.onPostExecute(res)
                UtilityClass.hideDialog()
                if (res != null) {
                    message = res.optString("msg")
                    if (res.optBoolean("success") && res.optInt("statusCode") == 200) {
                        Log.e("USErDataAdd", "success")
                        // Save that temp object into user object and proceed further with complete information
                        Config.setUserData(ServicesFragment.userTempObj) // New Born Object.
                        var intent = Intent(activity!!, NavigationActivity::class.java)
                        intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        activity!!.finish()
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
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    val jsonObjectMain = JSONObject()
    fun makeJsonObjectForAdditionalDetails(): JSONObject {
        val userData = ServicesFragment.userTempObj
        val jsonObjectName = JSONObject()
        val jsonObjectadddress = JSONObject()
        val jsonObjectFssai = JSONObject()
        val jsonObjectPreferences = JSONObject()
        val jsonObjectNativePlace = JSONObject()
        val jsonObjectMainAddtionalDetail = JSONObject()

        //Name
        jsonObjectName.put("title", userData?.name?.title)
        jsonObjectName.put("fname", userData?.name?.fname)
        jsonObjectName.put("mname", userData?.name?.mname)
        jsonObjectName.put("lname", userData?.name?.lname)
        // Address
        jsonObjectadddress.put("line1", userData?.userAddress?.line1)
        jsonObjectadddress.put("line2", userData?.userAddress?.line2)
        jsonObjectadddress.put("landmark", userData?.userAddress?.landmark)
        jsonObjectadddress.put("city", userData?.userAddress?.city)
        jsonObjectadddress.put("state", userData?.userAddress?.state)
        jsonObjectadddress.put("zip", userData?.userAddress?.zip)
        //Fssai
        jsonObjectFssai.put("licence_no", userData?.fssai?.licenceNo)
        jsonObjectFssai.put("is_registered", userData?.fssai?.isRegistered)
        jsonObjectFssai.put("expiry", userData?.fssai?.expiry)
        // Perferences
        jsonObjectPreferences.put("push", userData?.userCommunication?.push)
        jsonObjectPreferences.put("phone", userData?.userCommunication?.phone)
        jsonObjectPreferences.put("sms", userData?.userCommunication?.sms)
        jsonObjectPreferences.put("email", userData?.userCommunication?.email)
        //Native Places
        jsonObjectNativePlace.put("name", userData?.nativePlaces?.name)
        jsonObjectNativePlace.put("pin", userData?.nativePlaces?.pin)

        //**********************
        jsonObjectMainAddtionalDetail.put("is_term_accepted", userData?.isTermAccepted)
        jsonObjectMainAddtionalDetail.put("service_type", userData?.serviceType)
        jsonObjectMainAddtionalDetail.put("vehicle_type", userData?.vehicleType)
        jsonObjectMainAddtionalDetail.put("area_limit", userData?.areaLimit)
        jsonObjectMainAddtionalDetail.put("email", userData?.userEmail)
        jsonObjectMainAddtionalDetail.put("name", jsonObjectName)
        jsonObjectMainAddtionalDetail.put("address", jsonObjectadddress)
        jsonObjectMainAddtionalDetail.put("fssai", jsonObjectFssai)
        jsonObjectMainAddtionalDetail.put("native_place", jsonObjectNativePlace)
        jsonObjectMainAddtionalDetail.put("communication_preference", jsonObjectPreferences)

        //******************
        //jsonObjectMain.put("supplier_id", userData?.id)
        // jsonObjectMain.put("additional_details", jsonObjectMainAddtionalDetail)
        Log.e("JsonObjMain:TempUserobj", jsonObjectMainAddtionalDetail.toString())
        return jsonObjectMainAddtionalDetail
    }

}
