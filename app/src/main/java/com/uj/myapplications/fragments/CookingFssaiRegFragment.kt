package com.uj.myapplications.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import com.uj.myapplications.R
import com.uj.myapplications.activities.BaseActivity
import com.uj.myapplications.activities.DashboardActivity
import com.uj.myapplications.activities.EditProfileActivity
import com.uj.myapplications.interfaces.IImageCompressTaskListener
import com.uj.myapplications.pojo.FssaiRegPojo
import com.uj.myapplications.pojo.UserDataPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_cooking_fssai_reg.*
import org.json.JSONException
import org.json.JSONObject
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import rebus.permissionutils.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.Executors


/**
 * A simple [Fragment] subclass.
 *
 */
class CookingFssaiRegFragment : Fragment(), View.OnClickListener, FullCallback, CustomDialog.ChooseImageFromListener {
    private var response_str: String? = ""
    private var mPermissionFlag = false
    var dialog: Dialog? = null
    var mContext: Context? = null
    var isImageTaken: Boolean = false
    var mobileNumber = ""
    var FROM = ""
    var data: UserDataPojo? = null
    var myactivity: Activity? = null
    private var photos = ArrayList<File>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        myactivity = activity!!
        try {
            (myactivity as BaseActivity).setOnBackPressedListener { myactivity }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cooking_fssai_reg, container, false)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(EditProfileActivity.PHOTOS_KEY, photos)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.mContext = activity
        if (savedInstanceState != null) {
            photos = savedInstanceState.getSerializable(EditProfileActivity.PHOTOS_KEY) as ArrayList<File>
        }
        val normalLinkClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                val browserIntent =
                    Intent("android.intent.action.VIEW", Uri.parse("https://foodlicensing.fssai.gov.in/index.aspx"))
                startActivity(browserIntent)
            }
        }
        makeLinks(
            tvFssaiStandard,
            arrayOf("Food Safety and Standards"),
            arrayOf(normalLinkClickSpan)
        )
        // Config
        // Image Picker Default Configuration
        var config = EasyImage.configuration(activity)
        config.saveInAppExternalFilesDir()
        config.saveInRootPicturesDirectory()
        config.setImagesFolderName(EditProfileActivity.FOLDER_NAME)
        config.setCopyExistingPicturesToPublicLocation(true)
        FROM = ServicesFragment.caller
        Log.e("From : ", FROM)
        data = Config.getUserData()
        providePermission()
        btnRegisterOnFSSAI.setOnClickListener(this)
        btnNeedHelp.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        btnFSSAIReglicense.setOnClickListener(this)
        btnFinishFssaiReg.setOnClickListener(this)

        btnFssaiGreen.setOnClickListener(this)
        btnfssaiView.setOnClickListener(this)
        btnfssaiDelete.setOnClickListener(this)

        ibtn_cooking1.setOnClickListener(this)
        ibtn_cooking2.setOnClickListener(this)
        ibtn_cooking3.setOnClickListener(this)

        var editTextViewDatePicker = TextViewDatePicker(activity!!, etExpiryOn, System.currentTimeMillis(), 0)
        intilizeUIWithPreviousConfig()
    }

    private fun intilizeUIWithPreviousConfig() {
        val fssaiRegPojo = data?.fssai
        if (fssaiRegPojo != null) {
            tlRegLicenseNo.editText?.setText(fssaiRegPojo?.licenceNo)
            tlExpiryOn.editText?.setText(fssaiRegPojo?.expiry)
            val image = Config.geFSSAIImage()
            if (image != null && image.length > 0) {
                //Make That green View Visible and Hide that long button
                btnFSSAIReglicense.visibility = View.GONE
                ll_fssai_btn_green.visibility = View.VISIBLE
            } else {
                btnFSSAIReglicense.visibility = View.VISIBLE
                ll_fssai_btn_green.visibility = View.GONE
            }
        }
        when (ServicesFragment.serviceType) {
            0 -> {
                label1.text = "Cooking"
                label2.text = "Cooking"
            }
            1 -> {
                label1.text = "Cooking + Self Delivery"
                label2.text = "Cooking + Self Delivery"
            }
            2 -> {
                label1.text = "Self Delivery"
                label2.text = "Self Delivery"
            }
        }
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ibtn_cooking1 -> {

            }

            R.id.ibtn_cooking2 -> {
                scCookingDes.visibility = View.VISIBLE
                rlRegForm.visibility = View.GONE
                scFoodBusiness.visibility = View.GONE
            }

            R.id.ibtn_cooking3 -> {
                if (etRegLicenseNo.text.toString().trim().isNotEmpty() && etExpiryOn.text.toString().isNotEmpty()) {
                    scCookingDes.visibility = View.GONE
                    rlRegForm.visibility = View.VISIBLE
                    scFoodBusiness.visibility = View.GONE
                } else {
                    scCookingDes.visibility = View.VISIBLE
                    rlRegForm.visibility = View.GONE
                    scFoodBusiness.visibility = View.GONE
                }
            }
            R.id.btnFssaiGreen -> {
                if (PermissionUtils.isGranted(
                        activity!!,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE
                    ) && PermissionUtils.isGranted(
                        activity,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_picker, "Title")
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Permission not granted")
                    Timber.e("Permission not granted")
                }
            }
            R.id.btnfssaiView -> {
                if (Config.geFSSAIImage() != null) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_doc_view, Config.geFSSAIImage()!!)
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to view")
                    //Timber.e("Permission not granted")
                }
            }

            R.id.btnfssaiDelete -> {
                //Delete the Config Fssai image
                // Load tthat button to previous state and ll_green to visiblity Gone
                val userDataPojo = Config.getUserData()
                var fssaiRegPojo = userDataPojo?.fssai
                //1 : Fssai Deletion
                if (fssaiRegPojo != null) {
                    val image = fssaiRegPojo?.userFssaiImagepath
                    if (!image.isNullOrEmpty()) {
                        showDeleteImageDialog(1)
                    } else {
                        UtilityClass.showToast(activity!!, "Nothing to delete")
                    }
                }

            }
            R.id.btnRegisterOnFSSAI -> {
                scCookingDes.visibility = View.GONE
                rlRegForm.visibility = View.VISIBLE
                scFoodBusiness.visibility = View.GONE
            }
            R.id.btnNeedHelp -> {
                //hit need help APi
                try {
                    if (data != null) {
                        mobileNumber = data!!.mobile?.toString()!!
                    } else {
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val jsonObject = JSONObject()
                try {
                    jsonObject.put(RestTags.MOBILE, mobileNumber)
                    jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)//TODO
                    jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
                    Timber.e(jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                if (UtilityClass.isInternetAvailable(activity!!)) {
                    getNeedHelpResponse(RestUrls.NEED_FSSAI_HELP, jsonObject)
                } else {
                    UtilityClass.showToast(activity!!, getString(R.string.device_offline_message))
                }
            }
            R.id.btnFSSAIReglicense -> {
                //Check Permission than show dialog
                // showImagePickerDialog()
                if (PermissionUtils.isGranted(
                        activity!!,
                        PermissionEnum.WRITE_EXTERNAL_STORAGE
                    ) && PermissionUtils.isGranted(
                        activity,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_picker, "Title")
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Permission not granted")
                    Timber.e("Permission not granted")
                }
            }
            R.id.btnFinishFssaiReg -> {
                //Check Permission than show dialog
                //MyMa GuideLines
                loadPreferencesActivity()
            }
            R.id.btnNext -> {
                // Check for data fill for FSSAI Reg and validate Image uploading
                if (!tlRegLicenseNo.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlRegLicenseNo, mContext!!)
                    UtilityClass.showToast(mContext!!, "Registration Number " + Constants.ERR_CANT_BE_EMPTY)
                }
                if (tlRegLicenseNo.editText!!.text.toString().trim().length < 14) {
                    UtilityClass.shakeItemView(tlRegLicenseNo, mContext!!)
                    UtilityClass.showToast(mContext!!, "Registration number cannot be less than 14 digit")
                } else if (!tlExpiryOn.editText!!.text.isNotEmpty()) {
                    UtilityClass.shakeItemView(tlExpiryOn, mContext!!)
                    UtilityClass.showToast(mContext!!, "Expiry Date " + Constants.ERR_CANT_BE_EMPTY)
                } /*else if (!isImageTaken) {
                    UtilityClass.shakeItemView(btnFSSAIReglicense, mContext!!)
                    UtilityClass.showToast(mContext!!, getString(R.string.Upload_FSSAI_Reg_License))
                }*/ else {
                    // Save FSSAI Details To temp User Object
                    val fssaiObj = FssaiRegPojo()
                    fssaiObj.expiry = tlExpiryOn.editText!!.text.toString().trim()
                    fssaiObj.licenceNo = tlRegLicenseNo.editText!!.text.toString().trim()
                    fssaiObj.isRegistered = true
                    //Update Temp UserData Obj
                    ServicesFragment.userTempObj.fssai = fssaiObj
                    ServicesFragment.userTempObj.serviceType = ServicesFragment.serviceType
                    //UtilityClass.showToast(mContext!!, "AllCorrect Test")
                    // Show your Another view Cooking View two Only deleivery Fragment
                    if (FROM == "1") {
                        UtilityClass.switchToFragment(
                            OnlyDeliveryFragment(),
                            "",
                            R.id.fragment,
                            activity!!.supportFragmentManager,
                            true
                        )
                    } else {
                        loadPreferencesActivity()
                    }
                }
            }

        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    fun showImagePickerDialog() {
        // Create custom dialog object
        if (dialog != null) {
            dialog!!.dismiss()
        }
        dialog = Dialog(mContext!!)
        // Include dialog.xml file
        // inflate and adjust layout
        // retrieve display dimensions
        val displayRectangle = Rect()
        val window = activity!!.window
        window.decorView.getWindowVisibleDisplayFrame(displayRectangle)
        val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout = inflater.inflate(R.layout.dialog_image_picker, null)
        layout.minimumWidth = Math.round(displayRectangle.width() * 0.9f)
        layout.minimumHeight = Math.round(displayRectangle.height() * 0.6f)
        dialog!!.setContentView(layout)
        // Set dialog title
        dialog!!.setTitle(getString(R.string.take_a_pic))
        // set values for custom dialog components - text, image and button
        val ll_camera = dialog!!.findViewById(R.id.ll_camera) as LinearLayout
        val ll_gallery = dialog!!.findViewById(R.id.ll_gallery) as LinearLayout
        val iBtnCloseImagePickerDialog = dialog!!.findViewById(R.id.iBtnCloseImagePickerDialog) as ImageButton
        ll_camera.setOnClickListener {
            UtilityClass.showToast(mContext!!, "Camera:Test")
        }
        ll_gallery.setOnClickListener {
            UtilityClass.showToast(mContext!!, "Gallery:Test")
        }
        iBtnCloseImagePickerDialog.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.setCancelable(false)
        dialog?.show()

    }

    fun providePermission() {
        PermissionManager.Builder()
            .permission(
                PermissionEnum.READ_PHONE_STATE,
                PermissionEnum.CAMERA,
                PermissionEnum.READ_EXTERNAL_STORAGE,
                PermissionEnum.WRITE_EXTERNAL_STORAGE
            )
            .askAgain(true)
            .key(2100)
            .askAgainCallback { response -> showDialog(response) }.callback(this)
            .ask(this)
    }

    private fun showDialog(response: AskAgainCallback.UserResponse) {
        AlertDialog.Builder(mContext!!)
            .setTitle("Permission needed")
            .setMessage("This app realy need to use this permission, you wont to authorize it?")
            .setPositiveButton(
                "OK"
            ) { dialogInterface, i -> response.result(true) }
            .setNegativeButton(
                "NOT NOW"
            ) { dialogInterface, i -> response.result(false) }
            .setCancelable(false)
            .show()
    }

    override fun result(
        permissionsGranted: ArrayList<PermissionEnum>?,
        permissionsDenied: ArrayList<PermissionEnum>?,
        permissionsDeniedForever: ArrayList<PermissionEnum>?,
        permissionsAsked: ArrayList<PermissionEnum>?
    ) {
        if (permissionsGranted?.size!! > 0) {
            mPermissionFlag = true
        }
        if (permissionsDenied?.size!! > 0) {
            mPermissionFlag = false
        }
        if (permissionsDeniedForever?.size!! > 0) {
            // PermissionUtils.goToAppSetting(mContext)
//          UtilityClass.goToAppSetting(mContext!!)

        }
    }


    @SuppressLint("StaticFieldLeak")
    fun getNeedHelpResponse(url: String, jsonObject: JSONObject) {
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
                    when (Integer.parseInt(res["code"])) {
                        200 -> {
                            UtilityClass.hideDialog()
                            response_str = res["response"]
                            if (response_str != null) {
                                Log.e("RESPONSE STRING:>", response_str)
                                try {
                                    val jsonObject = JSONObject(response_str)
                                    if (jsonObject != null && jsonObject.optBoolean("success")) {
                                        UtilityClass.showToast(
                                            activity!!,
                                            "Thanks for your interest, MyMa will get back to you soon."
                                        )
                                        scCookingDes.visibility = View.GONE
                                        rlRegForm.visibility = View.GONE
                                        scFoodBusiness.visibility = View.VISIBLE
                                    }
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }

                            }
                        }
                        400 -> {
                            UtilityClass.showToast(activity!!, UtilityClass.OOPS_STRING + " 400")
                            UtilityClass.hideDialog()
                        }
                        500 -> {
                            UtilityClass.showToast(activity!!, UtilityClass.OOPS_STRING + " 500")
                            UtilityClass.hideDialog()

                        }
                        //Default case of when
                        else -> {
                            UtilityClass.showToast(activity!!, UtilityClass.OOPS_STRING)
                            UtilityClass.hideDialog()
                        }
                    }
                } else {
                    UtilityClass.showToast(activity!!, UtilityClass.OOPS_STRING)
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }


    fun loadPreferencesActivity() {
        var intent = Intent(mContext, DashboardActivity::class.java)
        /* intent.flags =
                 Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or
                 Intent.FLAG_ACTIVITY_CLEAR_TASK*/
        startActivity(intent)
        // activity!!.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, activity!!, object : DefaultCallback() {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                onPhotosReturned(imageFile!!)
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                //Some error handling
                e!!.printStackTrace()
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(activity!!)
                    photoFile?.delete()
                }
            }
        })
    }

    private var mExecutorService = Executors.newFixedThreadPool(1)
    private var imageCompressTask: ImageCompressTask? = null
    private fun onPhotosReturned(returnedPhoto: File) {
        //Compress Photo with the help of Image Compresser
        // Than save it to Config
        imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
        mExecutorService.execute(imageCompressTask)

    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(activity!!)
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clean up!
        mExecutorService?.shutdown()
        mExecutorService = null
        imageCompressTask = null
    }

    override fun selectedImageFrom(from: Int) {
        when (from) {
            1 -> {
                // UtilityClass.showToast(activity!!, "camera")
                EasyImage.openCamera(this, 0)
            }
            2 -> {
                // UtilityClass.showToast(activity!!, "gallery")
                EasyImage.openGallery(this, 0)
            }
        }
    }

    private fun showDeleteImageDialog(flag: Int) {
        AlertDialog.Builder(ContextThemeWrapper(activity!!, R.style.AlertDialogCustom))
            .setTitle("Delete File")
            .setCancelable(false)
            .setIcon(resources.getDrawable(R.mipmap.ic_launcher))
            .setMessage(getString(R.string.are_you_sure_delete_file))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                when (flag) {
                    1 -> {
                        //Clear Image and make Button Disable // TODO
                        val userDataPojo = Config.getUserData()
                        var fssaiRegPojo = userDataPojo?.fssai
                        //1 : Fssai Deletion
                        if (fssaiRegPojo != null) {
                            fssaiRegPojo.userFssaiImagepath = ""
                        }
                        Config.setUserData(userDataPojo!!)
                        ll_fssai_btn_green.visibility = View.GONE
                        btnFSSAIReglicense.visibility = View.VISIBLE
                    }
                }
            }
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                // user doesn't want to logout
                dialog.dismiss()
            }
            .show()
    }

    //image compress task callback
    private var iImageCompressTaskListener = object : IImageCompressTaskListener {
        override fun onComplete(compressed: List<File>) {
            //photo compressed. Yay!
            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)
            var file = compressed[0]
            if (file != null) {
                Log.d("ImageCompressor", "New Fssai photo size ==> " + file.length()) //log new file size.
                val userDataPojo = Config.getUserData()
                var fssaiRegPojo = userDataPojo?.fssai
                fssaiRegPojo?.userFssaiImagepath = file.path.toString()
                userDataPojo?.fssai = fssaiRegPojo
                Config.setFSSAIImage(file.path)
                Config.setUserData(userDataPojo!!)
                //Set Image to Hide Fssai Disable button and load green view
                btnFSSAIReglicense.visibility = View.GONE
                ll_fssai_btn_green.visibility = View.VISIBLE
            } else {
                Log.d("ImageCompressor", "New photo size ==>File null")
            }
        }

        override fun onError(error: Throwable) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error);
        }

    }


}
