package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.interfaces.IImageCompressTaskListener
import com.uj.myapplications.pojo.ProfileDetails
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.nav_header_dummy_navigation.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import rebus.permissionutils.*
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*
import java.util.concurrent.Executors


class EditProfileActivity : AppCompatActivity(), View.OnClickListener, FullCallback,
    CustomDialog.ChooseImageFromListener, AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        flagTitle = position
    }

    companion object {
        val PHOTOS_KEY = "myma_image_photos_list"
        val FOLDER_NAME = "MYMA"
        var photosKitchen = ArrayList<File>()
        @JvmField
        var finalLat: Double = 0.0
        @JvmField
        var finalLng: Double = 0.0
    }

    //create a single thread pool to our image compression class.
    private var mExecutorService = Executors.newFixedThreadPool(1)
    private var imageCompressTask: ImageCompressTask? = null
    var flagTitle: Int = 0 //0 = Mr. and 1 = Mrs.
    var title = arrayOf("Mr.", "Mrs.")
    var listKitchenPics: List<String> = ArrayList<String>()
    private var photosDineIn = ArrayList<File>()
    private var deletedPhotos = ArrayList<String>()
    private var profilePhotos = ArrayList<String>()
    private var profilePhotosUrlFromServer = ArrayList<String>()
    private var profileImagePath = ""
    private var imagePickFlag = 0 //0-Profile Image 1- Kitchen 2- DineIn
    private var imagePickFlagLocation = 0 //1,2,3,4
    /*Google Location  Code*/
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /******/
    override fun selectedImageFrom(from: Int) {
        when (from) {
            1 -> {
                // UtilityClass.showToast(this, "camera")
                EasyImage.openCamera(this, 0)
            }
            2 -> {
                //  UtilityClass.showToast(this, "gallery")
                EasyImage.openGallery(this, 0)
            }
        }
    }

    var veg_nonVeg: Int = 0 // 0 -means Veg and 1 - means non veg
    private var mPermissionFlag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        providePermission()
        //Clear deleted Photos
        deletedPhotos.clear()
        profilePhotos.clear()
        profile_image.setOnClickListener(this)
        btn_pure_veg.setOnClickListener(this)
        btn_non_veg.setOnClickListener(this)
        btn_upload_kitchen_photos.setOnClickListener(this)
        btn_upload_dine_in_photos.setOnClickListener(this)
        btnUpdateProfile.setOnClickListener(this)
        ibtn_location.setOnClickListener(this)
        spinner1.onItemSelectedListener = this
        //Creating the ArrayAdapter instance having the country list
        var aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, title)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinner1.adapter = aa
        imageView1.setOnClickListener(this)
        imageView2.setOnClickListener(this)
        imageView3.setOnClickListener(this)
        imageView4.setOnClickListener(this)
        imageView_k1.setOnClickListener(this)
        imageView_K2.setOnClickListener(this)
        imageView_K3.setOnClickListener(this)
        imageView_k4.setOnClickListener(this)
        iBtn_ic_delete_1.setOnClickListener(this)
        iBtn_ic_delete_2.setOnClickListener(this)
        iBtn_ic_delete_3.setOnClickListener(this)
        iBtn_ic_delete_4.setOnClickListener(this)

        if (savedInstanceState != null) {
            photosDineIn = savedInstanceState.getSerializable(PHOTOS_KEY) as ArrayList<File>
        }
        // Image Picker Default Configuration
        var config = EasyImage.configuration(this)
        config.saveInAppExternalFilesDir()
        config.saveInRootPicturesDirectory()
        config.setImagesFolderName(FOLDER_NAME)
        config.setCopyExistingPicturesToPublicLocation(true)
        if (UtilityClass.isInternetAvailable(this@EditProfileActivity)) {
            val url = RestUrls.GET_USER_PROFILE + Config.getUserData()?.id
            Log.e("URL", url)
            getProfileDetail(url)
        } else {
            UtilityClass.showToast(this, "Please check internet connection")
        }
        // initialize the necessary libraries
        photosKitchen.clear()
        init()
        initUI()
    }

    private fun initUI() {
        val userDataPojo = Config.getUserData()
        if (userDataPojo != null) {
            var name = userDataPojo?.name
            etEmail.setText(userDataPojo?.userEmail)
            if (name != null) {
                if (name?.title.equals("Mr.")) {
                    //Spinner Position Set
                } else {

                }
                etYourName.setText(name?.fname)
                etMiddleNAme.setText(name?.mname)
                etSurName.setText(name?.lname)
            }
            //Address
            val userAddress = userDataPojo?.userAddress
            if (userAddress != null) {
                etAddress.setText(userAddress?.line1)
                etAddressLine2.setText(userAddress?.line2)
                etAddressLocality.setText(userAddress?.city)
                etAddressPinCode.setText(userAddress?.zip)
                etAddressNativePlaceName.setText(userAddress?.nativeAddress)
                etAddressNativePinCode.setText(userAddress?.nativePinCode)
            }


            //About Me / Bio
            val profileObj = userDataPojo?.profileDetails
            if (profileObj != null) {
                //Bio
                if (profileObj.bio != null) {
                    etBio.setText(profileObj?.bio)
                } else {
                    etBio.setText("N/A")
                }
                //Speciality
                //  Spinner Section
                /*   if (profileObj.speciality != null) {
                       etSp.text = profileObj.speciality
                   } else {
                       txt_my_speciality.text = "N/A"
                   }*/
                //Speciality
                if (profileObj.kitchenType != null) {
                    val kitchenType = profileObj.kitchenType
                    when (kitchenType) {
                        0 -> {
                            //VEG
                            btn_pure_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                            btn_pure_veg.setTextColor(resources.getColor(R.color.white))
                            btn_non_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                            btn_non_veg.setTextColor(resources.getColor(R.color.gray))
                        }
                        1 -> {
                            //Non VEG
                            btn_non_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                            btn_non_veg.setTextColor(resources.getColor(R.color.white))
                            btn_pure_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                            btn_pure_veg.setTextColor(resources.getColor(R.color.gray))
                        }
                    }
                }
            }
            if (Config?.getProfileCImage() != null) {
                if (UtilityClass.checkURL(Config?.getProfileCImage().toString())) {
                    profilePhotosUrlFromServer.add(Config?.getProfileCImage()!!)
                }
                UtilityClass.setImageFromUrlOrFileProfile(this, Config.getProfileCImage()!!, profile_image)
            } else {

            }
        }
    }

    fun init() {
        // Create persistent LocationManager reference
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PHOTOS_KEY, photosDineIn)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.iBtn_ic_delete_1 -> {
                //Set Default image
                imageView_k1.setImageResource(R.drawable.ic_upload)
                imageViewToSet_k1.setImageResource(R.drawable.transparent_drawable)
                iBtn_ic_delete_1.visibility = View.GONE
                //Add that Element that element from Array List
                if (listKitchenPics != null && listKitchenPics.size > 0) {
                    if (!(deletedPhotos.contains(listKitchenPics[0]))) {
                        deletedPhotos.add(listKitchenPics[0])
                    } else {
                        Log.e("deletedPhotos", "Already contains this item.")
                    }

                }

                if (photosKitchen != null && photosKitchen.size > 0) {
                    photosKitchen.removeAt(0)
                }


            }
            R.id.iBtn_ic_delete_2 -> {
                //Set Default image
                imageView_K2.setImageResource(R.drawable.ic_upload)
                imageViewToSet_K2.setImageResource(R.drawable.transparent_drawable)
                iBtn_ic_delete_2.visibility = View.GONE
                //Add that Element that element from Array List
                if (listKitchenPics != null && listKitchenPics.size > 1) {
                    if (!(deletedPhotos.contains(listKitchenPics[1]))) {
                        deletedPhotos.add(listKitchenPics[1])
                    } else {
                        Log.e("deletedPhotos", "Already contains this item.")
                    }
                }


                if (photosKitchen != null && photosKitchen.size > 1) {
                    photosKitchen.removeAt(1)
                }

            }

            R.id.iBtn_ic_delete_3 -> {
                //Set Default image
                imageView_K3.setImageResource(R.drawable.ic_upload)
                imageViewToSet_k3.setImageResource(R.drawable.transparent_drawable)
                iBtn_ic_delete_3.visibility = View.GONE
                //Add that Element that element from Array List
                if (listKitchenPics != null && listKitchenPics.size > 2) {
                    if (!(deletedPhotos.contains(listKitchenPics[2]))) {
                        deletedPhotos.add(listKitchenPics[2])
                    } else {
                        Log.e("deletedPhotos", "Already contains this item.")
                    }
                }
                if (photosKitchen != null && photosKitchen.size > 2) {
                    photosKitchen.removeAt(2)
                }

            }
            R.id.iBtn_ic_delete_4 -> {
                //Set Default image
                imageView_k4.setImageResource(R.drawable.ic_upload)
                imageViewToSet_k4.setImageResource(R.drawable.transparent_drawable)
                iBtn_ic_delete_4.visibility = View.GONE
                //Add that Element that element from Array List
                if (listKitchenPics != null && listKitchenPics.size > 3) {
                    if (!(deletedPhotos.contains(listKitchenPics[3]))) {
                        deletedPhotos.add(listKitchenPics[3])
                    } else {
                        Log.e("deletedPhotos", "Already contains this item.")
                    }
                }
                if (photosKitchen != null && photosKitchen.size > 3) {
                    photosKitchen.removeAt(3)
                }

            }


            R.id.profile_image -> {
                imagePickFlag = 0
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }
            R.id.btn_pure_veg -> {
                //0changeColor
                btn_non_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn_non_veg.setTextColor(resources.getColor(R.color.gray))
                btn_pure_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn_pure_veg.setTextColor(resources.getColor(R.color.white))
                veg_nonVeg = 0

            }
            R.id.btn_non_veg -> {
                btn_non_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn_non_veg.setTextColor(resources.getColor(R.color.white))
                btn_pure_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn_pure_veg.setTextColor(resources.getColor(R.color.gray))
                veg_nonVeg = 1
            }
            R.id.imageView1 -> {
                imagePickFlag = 2
                imagePickFlagLocation = 1
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }
            R.id.imageView2 -> {
                imagePickFlag = 2
                imagePickFlagLocation = 2
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }
            R.id.imageView_k1 -> {
                imagePickFlag = 1
                imagePickFlagLocation = 1
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }
            R.id.imageView_K2 -> {
                imagePickFlag = 1
                imagePickFlagLocation = 2
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }
            R.id.imageView_K3 -> {
                imagePickFlag = 1
                imagePickFlagLocation = 3
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }

            R.id.imageView_k4 -> {
                imagePickFlag = 1
                imagePickFlagLocation = 4
                if (PermissionUtils.isGranted(this, PermissionEnum.WRITE_EXTERNAL_STORAGE) && PermissionUtils.isGranted(
                        this,
                        PermissionEnum.CAMERA
                    )
                ) {
                    val ft = supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(this, R.layout.dialog_image_picker, "Title")
                    newFragment.show(ft, "dialog")
                } else {

                }
            }

            R.id.btnUpdateProfile -> {
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
                    (tlBIO.editText!!.text.toString().trim().length < 5) -> {
                        // UtilityClass.shakeItemView(tlAddressNativePlaceName, this)
                        UtilityClass.showToast(
                            this,
                            "Bio should be more than 5 characters long or It Should not be Empty"
                        )
                    }
                    else -> {
                        if (UtilityClass.isInternetAvailable(this)) {

                            updateProfileResponse(makeJsonObjectForUpdateProfile(), makeJsonObjectForDeletionPics())
                        } else {
                            UtilityClass.showToast(this, RestTags.NO_INTERNET)
                        }
                    }
                }
            }
            R.id.ibtn_location -> {
                getLastLocation()
                //Start Map Activity
                var intent = Intent(
                    this@EditProfileActivity,
                    MapActivity::class.java
                )
                //intent.putExtra(RestTags.FROM, "Splash")
                //intent.putExtra("SESSION_NOT_VALID", "SESSION_NOT_VALID")
                startActivity(intent)
                /* if (PermissionUtils.isGranted(
                         this,
                         PermissionEnum.ACCESS_FINE_LOCATION
                     ) && PermissionUtils.isGranted(
                         this,
                         PermissionEnum.ACCESS_COARSE_LOCATION
                     )
                 ) {
                     //Enter Location to edittext

                 } else {
                     UtilityClass.goToAppSetting(this)
                 }*/
            }
        }
    }

    private fun makeJsonObjectForDeletionPics(): JSONObject {
        //Deleted Pics(Profile + Kitchen + Dine_in)
        var jsonObjectPicsDeletions = JSONObject()
        var kitchenDeletedPicsJSONArray = JSONArray(deletedPhotos)
        var profileDeletedPicsJSONArray = JSONArray(profilePhotos)
        var dineInDeletedPicsJSONArray = JSONArray()
        jsonObjectPicsDeletions.put("kitchen_pics", kitchenDeletedPicsJSONArray)
        jsonObjectPicsDeletions.put("profile_pic", profileDeletedPicsJSONArray)
        jsonObjectPicsDeletions.put("dinein_pics", dineInDeletedPicsJSONArray)
        Log.e("jsonObjectPicsDeletions", jsonObjectPicsDeletions.toString())
        return jsonObjectPicsDeletions
    }

    fun providePermission() {
        PermissionManager.Builder()
            .permission(
                PermissionEnum.READ_PHONE_STATE,
                PermissionEnum.CAMERA,
                PermissionEnum.ACCESS_FINE_LOCATION,
                PermissionEnum.ACCESS_COARSE_LOCATION,
                PermissionEnum.READ_EXTERNAL_STORAGE,
                PermissionEnum.WRITE_EXTERNAL_STORAGE
            )
            .askAgain(true)
            .key(2100)
            .askAgainCallback { response -> showDialog(response) }.callback(this)
            .ask(this)
    }

    private fun showDialog(response: AskAgainCallback.UserResponse) {
        AlertDialog.Builder(this!!)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
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
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@EditProfileActivity)
                    photoFile?.delete()
                }
            }
        })
    }

    private fun onPhotosReturned(returnedPhoto: File) {
        when (imagePickFlag) {
            0 -> {
                //  profileImagePath = returnedPhoto.path
                //UtilityClass.setImageFromUrlOrFileProfile(this, returnedPhoto.path, profile_image)
                //Create ImageCompressTask and execute with Executor.
                imageCompressTask = ImageCompressTask(this, returnedPhoto.path, iImageCompressTaskListener)
                mExecutorService.execute(imageCompressTask)
                if (profilePhotosUrlFromServer != null && profilePhotosUrlFromServer.size > 0) {
                    profilePhotos.add(profilePhotosUrlFromServer[0])
                }


            }
            1 -> {
                //photosKitchen.add(returnedPhoto)
                when (imagePickFlagLocation) {
                    1 -> {
                        imageCompressTask = ImageCompressTask(this, returnedPhoto.path, iImageCompressTaskListener)
                        mExecutorService.execute(imageCompressTask)
                        //Becoz i want the url of updated image
                        if (listKitchenPics != null && listKitchenPics.size > 0) {
                            deletedPhotos.add(listKitchenPics[0])
                        }
                        // UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet_k1)
                    }
                    2 -> {
                        imageCompressTask = ImageCompressTask(this, returnedPhoto.path, iImageCompressTaskListener)
                        mExecutorService.execute(imageCompressTask)
                        if (listKitchenPics != null && listKitchenPics.size > 1) {
                            deletedPhotos.add(listKitchenPics[1])
                        }
                        //UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet_K2)
                    }
                    3 -> {
                        imageCompressTask = ImageCompressTask(this, returnedPhoto.path, iImageCompressTaskListener)
                        mExecutorService.execute(imageCompressTask)
                        if (listKitchenPics != null && listKitchenPics.size > 2) {
                            deletedPhotos.add(listKitchenPics[2])
                        }
//                        UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet_k3)
                    }
                    4 -> {
                        imageCompressTask = ImageCompressTask(this, returnedPhoto.path, iImageCompressTaskListener)
                        mExecutorService.execute(imageCompressTask)
                        if (listKitchenPics != null && listKitchenPics.size > 3) {
                            deletedPhotos.add(listKitchenPics[3])
                        }
                        // UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet_k4)
                    }
                }
            }
            2 -> {
                photosDineIn.add(returnedPhoto)
                when (imagePickFlagLocation) {
                    1 -> {
                        UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet1)
                    }
                    2 -> {
                        UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet2)
                    }
                    3 -> {
                        UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet3)
                    }
                    4 -> {
                        UtilityClass.setImageFromUrlOrFile(this, returnedPhoto.path, imageViewToSet4)
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this)
        super.onDestroy()
        //clean up!
        mExecutorService.shutdown()
        mExecutorService = null
        imageCompressTask = null
    }

    private var response_str: String? = ""
    private var message: String? = ""

    @SuppressLint("StaticFieldLeak")
    private fun getProfileDetail(url: String) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@EditProfileActivity)
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
                                                        user?.userAddress = userDetail.address
                                                        user?.profileDetails = userDetail
                                                        Config.setUserData(user!!)
                                                    }
                                                }

                                                UtilityClass.showToast(
                                                    this@EditProfileActivity,
                                                    if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                )
                                            } else {
                                                UtilityClass.showToast(
                                                    this@EditProfileActivity,
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
                                        this@EditProfileActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        this@EditProfileActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        this@EditProfileActivity,
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
                        this@EditProfileActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    private fun setDataToFields(userDetail: ProfileDetails) {
        //AlterNate Number not coming
        etYourName.setText(userDetail?.name?.fname)
        etMiddleNAme.setText(userDetail?.name?.mname)
        etSurName.setText(userDetail?.name?.lname)

        etEmail.setText(userDetail?.email)
        etMobileNumber.setText(userDetail?.mobile.toString())
        etBio.setText(userDetail?.bio.toString())
        etMobileNumberAlternate.setText(userDetail?.alternateMobNum.toString())

        etAddress.setText(userDetail?.address?.line1)
        etAddressLine2.setText(userDetail?.address?.line2)
        etAddressLocality.setText(userDetail?.address?.landmark)
        etAddressPinCode.setText(userDetail?.address?.zip)

        etLoactionOnMap.setText(userDetail?.location?.lat.plus(",").plus(userDetail?.location?.long))
        listKitchenPics = userDetail.kitchenPics
        if (listKitchenPics != null && listKitchenPics.size > 0) {
            // UtilityClass.setImageFromUrlOrFile(this, listKitchenPics[0], imageViewToSet_k1)
            listKitchenPics.forEachIndexed { index, s ->
                when (index) {
                    0 -> {
                        iBtn_ic_delete_1.visibility = View.VISIBLE
                        UtilityClass.setImageFromUrlOrFile(this, s, imageViewToSet_k1)
                    }
                    1 -> {
                        iBtn_ic_delete_2.visibility = View.VISIBLE
                        UtilityClass.setImageFromUrlOrFile(this, s, imageViewToSet_K2)
                    }
                    2 -> {
                        iBtn_ic_delete_3.visibility = View.VISIBLE
                        UtilityClass.setImageFromUrlOrFile(this, s, imageViewToSet_k3)
                    }
                    3 -> {
                        iBtn_ic_delete_4.visibility = View.VISIBLE
                        UtilityClass.setImageFromUrlOrFile(this, s, imageViewToSet_k4)
                    }
                }
            }
        }
        //Kitchen Type
        if (userDetail.kitchenType != null) {
            val kitchenType = userDetail.kitchenType
            when (kitchenType) {
                0 -> {
                    //VEG
                    btn_pure_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn_pure_veg.setTextColor(resources.getColor(R.color.white))
                    btn_non_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btn_non_veg.setTextColor(resources.getColor(R.color.gray))
                }
                1 -> {
                    //Non VEG
                    btn_non_veg.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn_non_veg.setTextColor(resources.getColor(R.color.white))
                    btn_pure_veg.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btn_pure_veg.setTextColor(resources.getColor(R.color.gray))
                }
            }
        }
        ///Native Place
        etAddressNativePlaceName.setText(userDetail?.nativePlace?.name)
        userDetail?.nativePlace?.pin?.let { etAddressNativePinCode.setText(it.toString()) }


    }

    /**
     * @param flag =1 Add
     * @param flag =2 Update
     * */
    @SuppressLint("StaticFieldLeak")
    fun updateProfileResponse(jsonObject: JSONObject, jsonObjectPicsDeletion: JSONObject) {
        object : AsyncTask<String, Void, JSONObject>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@EditProfileActivity)
            }

            override fun doInBackground(vararg params: String): JSONObject? {
                try {
                    //Todo
                    val res =
                        OkhttpRequestUtils.userProfileUpdateResponse(
                            Config.geToken(),
                            photosDineIn, photosKitchen, profileImagePath,
                            jsonObject.toString(), jsonObjectPicsDeletion.toString()
                        )
                    if (res != null) {
                        /*val stringHashMap = java.util.HashMap<String, String>()
                        stringHashMap["code"] = res!!.code().toString()
                        stringHashMap["response"] = res!!.body()!!.string()*/
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
                    if (res.optBoolean("status") && (res.optInt("statusCode") == 200)) {
                        Log.e("Update Profile", "success")
                        //Save Profile Pic in config and kitchen pics and
                        //1 Save Profile Object into User data object with Kitchen Pics and Profile pic
                        setDataToUserObject()
                        // Clear
                        deletedPhotos.clear()
                        profilePhotos.clear()
                        listKitchenPics = ArrayList<String>()
                        val url = RestUrls.GET_USER_PROFILE + Config.getUserData()?.id
                        Log.e("URL", url)
                        getProfileDetail(url)
                    }
                    UtilityClass.showToast(
                        this@EditProfileActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                } else {
                    UtilityClass.showToast(
                        this@EditProfileActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    /* //define the listener
     private val locationListener: LocationListener = object : LocationListener {
         override fun onLocationChanged(location: Location) {
             lat = location.latitude.toString()
             lng = location.longitude.toString()
             etLoactionOnMap.setText("" + location.longitude + " , " + location.latitude);
         }

         override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
         override fun onProviderEnabled(provider: String) {}
         override fun onProviderDisabled(provider: String) {}
     }*/
    var lat = "0.0"
    var lng = "0.0"
    var jsonObjectMain = JSONObject()
    fun makeJsonObjectForUpdateProfile(): JSONObject {
        val userData = Config.getUserData()
        val jsonObjectName = JSONObject()
        if (flagTitle == 0) {
            jsonObjectName.put("title", "Mr.")
        } else {
            jsonObjectName.put("title", "Mrs.")
        }
        jsonObjectName.put("fname", etYourName.text.toString().trim())
        jsonObjectName.put("mname", etMiddleNAme.text.toString().trim())
        jsonObjectName.put("lname", etSurName.text.toString().trim())
        //**********************
        val jsonObjectAddress = JSONObject()
        jsonObjectAddress.put("line1", etAddress.text.toString().trim())
        jsonObjectAddress.put("line2", etAddressLine2.text.toString().trim())
        jsonObjectAddress.put("landmark", etAddressLocality.text.toString().trim())
        jsonObjectAddress.put("state", "")
        jsonObjectAddress.put("zip", etAddressPinCode.text.toString().trim())

        //**********************
        var jsonObjectLocation = JSONObject()
        jsonObjectLocation.put("lat", lat)
        jsonObjectLocation.put("long", lng)
        //*************************************
        var jsonObjectNativePlace = JSONObject()
        jsonObjectNativePlace.put("name", etAddressNativePlaceName.text.toString().trim())
        jsonObjectNativePlace.put("state", "")
        jsonObjectNativePlace.put("city", etAddressLocality.text.toString().trim())
        jsonObjectNativePlace.put("pin", etAddressNativePinCode.text.toString().trim())
        //*************************************
        var jsonObjectKitchenType = JSONObject()
        /*if (veg_nonVeg == 0) {
            jsonObjectNativePlace.put("veg", true)
            jsonObjectNativePlace.put("nonveg", false)
        } else {
            jsonObjectNativePlace.put("veg", false)
            jsonObjectNativePlace.put("nonveg", true)
        }
*/


        // var deletedPicsJSONArray = JSONArray(deletedPhotos)

        //*************************************
        jsonObjectMain.put("mobile2", etMobileNumberAlternate.text.toString().trim())
        jsonObjectMain.put("email", etEmail.text.toString())
        jsonObjectMain.put("speciality", "Kohlapuri")
        jsonObjectMain.put("bio", etBio.text.toString().trim())
        jsonObjectMain.put("name", jsonObjectName)
        jsonObjectMain.put("address", jsonObjectAddress)
        jsonObjectMain.put("location", jsonObjectLocation)
        jsonObjectMain.put("native_place", jsonObjectNativePlace)
        jsonObjectMain.put("kitchen_type", veg_nonVeg)
        //jsonObjectMain.put("deleted_pics_urls", deletedPicsJSONArray)
        Log.e("JsonObjMain", jsonObjectMain.toString())
        return jsonObjectMain
    }

    /*  private fun getLocationFromAddress() {
          val locationAddress = GeocodingLocation()
          locationAddress.getAddressFromLocation(
              address,
              applicationContext, GeocoderHandler()
          )
      }*/
    fun setDataToUserObject() {
        val userobj = Config.getUserData()
        if (userobj != null) {
            val name = userobj?.name
            if (name != null) {
                if (flagTitle == 0) {
                    name.title = "Mr."
                } else {
                    name.title = "Mrs."
                }
                name.fname = etYourName.text.toString().trim()
                name.lname = etSurName.text.toString().trim()
                name.mname = etMiddleNAme.text.toString().trim()
                userobj.name = name
            }
            //**********************
            // Address
            val address = userobj.userAddress
            if (address != null) {
                address.landmark = etAddressLocality.text.toString().trim()
                address.line1 = etAddressLocality.text.toString().trim()
                address.line2 = etAddressLocality.text.toString().trim()
                address.state = ""
                address.zip = etAddressPinCode.text.toString().trim()
                //Native Address Updation
                address.nativeAddress = etAddressNativePlaceName.text.toString().trim()
                address.nativePinCode = etAddressNativePinCode.text.toString().trim()
                //***********
                userobj.userAddress = address

            }
            //************
            //Location Updation

            val userLocation = userobj.location
            if (userLocation != null) {
                userLocation.lat = lat
                userLocation.long = lng
                userobj.location = userLocation
            }

            //**********
            // Others Things
            try {
                if (profileImagePath != null && profileImagePath.length > 0) {
                    userobj.profilePic = profileImagePath
                    Config.setProfileImage(profileImagePath)
                }
                userobj.alternamteMobileNum = BigInteger(etMobileNumberAlternate.text.toString().trim())
                userobj.userEmail = etEmail.text.toString().trim()
                // userobj.kitchenPics = photosKitchen.toMutableList()
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }

            //Now set it to config
            Config.setUserData(userobj)

        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (PermissionUtils.isGranted(
                this,
                PermissionEnum.ACCESS_FINE_LOCATION
            ) || PermissionUtils.isGranted(
                this,
                PermissionEnum.ACCESS_COARSE_LOCATION
            )
        ) {

            fusedLocationClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        lat = (task.result?.latitude).toString()
                        lng = (task.result?.longitude).toString()
                        finalLat = task.result?.latitude!!
                        finalLng = task.result?.longitude!!
                        /* latitudeText.text = resources
                             .getString(R.string.latitude_label, task.result.latitude)*/
                    } else {
                        Log.w("Location Issue", "getLastLocation:exception", task.exception)
                        // showSnackbar(R.string.no_location_detected)
                        lat = "0.0"
                        lng = "0.0"
                    }
                }
            etLoactionOnMap.setText(finalLat.toString().plus(" , ").plus(finalLng))
        } else {
            UtilityClass.showToast(this, "Please check your gps and location permission is given")
            // UtilityClass.goToAppSetting(this)
        }
    }


/* public GeoPoint getLocationFromAddress(String strAddress){

     Geocoder coder = new Geocoder(this);
     List<Address> address;
     GeoPoint p1 = null;

     try {
         address = coder.getFromLocationName(strAddress,5);
         if (address==null) {
             return null;
         }
         Address location=address.get(0);
         location.getLatitude();
         location.getLongitude();

         p1 = new GeoPoint((double) (location.getLatitude() * 1E6),
         (double) (location.getLongitude() * 1E6));

         return p1;
     }
 }*/

    //image compress task callback
    private var iImageCompressTaskListener = object : IImageCompressTaskListener {
        override fun onComplete(compressed: List<File>) {
            //photo compressed. Yay!
            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)
            var file = compressed[0]
            if (file != null) {
                when (imagePickFlag) {
                    0 -> {
                        // Case Profile Pic Compression
                        Log.d("ImageCompressor", "New Profile photo size ==> " + file.length()) //log new file size.
                        profileImagePath = file.absolutePath
                        UtilityClass.setImageFromUrlOrFileProfile(
                            this@EditProfileActivity,
                            file.absolutePath,
                            profile_image
                        )
                    }
                    1 -> {
                        photosKitchen.add(file)
                        when (imagePickFlagLocation) {
                            1 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Kitchen 1 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet_k1
                                )
                                Handler().postDelayed({
                                    val flag = hasImage(imageViewToSet_k1)
                                    if (flag) {
                                        iBtn_ic_delete_1.visibility = View.VISIBLE
                                    } else {
                                        iBtn_ic_delete_1.visibility = View.GONE
                                    }
                                }, 1000)

                            }
                            2 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Kitchen 2 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet_K2
                                )

                                Handler().postDelayed({
                                    val flag = hasImage(imageViewToSet_K2)
                                    if (flag) {
                                        iBtn_ic_delete_2.visibility = View.VISIBLE
                                    } else {
                                        iBtn_ic_delete_2.visibility = View.GONE
                                    }
                                }, 1000)
                            }
                            3 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Kitchen 3 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet_k3
                                )
                                Handler().postDelayed({
                                    val flag = hasImage(imageViewToSet_k3)
                                    if (flag) {
                                        iBtn_ic_delete_3.visibility = View.VISIBLE
                                    } else {
                                        iBtn_ic_delete_3.visibility = View.GONE
                                    }
                                }, 1000)
                            }
                            4 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Kitchen 4 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet_k4
                                )
                                Handler().postDelayed({
                                    val flag = hasImage(imageViewToSet_k4)
                                    if (flag) {
                                        iBtn_ic_delete_4.visibility = View.VISIBLE
                                    } else {
                                        iBtn_ic_delete_4.visibility = View.GONE
                                    }
                                }, 1000)
                            }
                        }
                    }
                    2 -> {
                        photosDineIn.add(file)
                        when (imagePickFlagLocation) {
                            1 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Dinein 1 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFileProfile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet1
                                )
                            }
                            2 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Dinein 2 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFileProfile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet2
                                )
                            }
                            3 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Dinein 3 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFileProfile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet3
                                )
                            }
                            4 -> {
                                Log.d(
                                    "ImageCompressor",
                                    "New Dinein 4 photo size ==> " + file.length()
                                ) //log new file size.
                                UtilityClass.setImageFromUrlOrFileProfile(
                                    this@EditProfileActivity,
                                    file.absolutePath,
                                    imageViewToSet4
                                )
                            }
                        }
                    }
                }

            } else {
                Log.d("ImageCompressor", "New photo size ==>File null")
            }
        }

        override fun onError(error: Throwable) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error)
        }
    }

    private fun hasImage(view: ImageView): Boolean {
        val drawable = view.drawable
        var hasImage = drawable != null

        if (hasImage && drawable is BitmapDrawable) {
            hasImage = drawable.bitmap != null
        }
        return hasImage
    }

    override fun onResume() {
        super.onResume()
        etLoactionOnMap.setText(finalLat.toString().plus(",").plus(finalLng))
    }


    @Throws(IllegalArgumentException::class)
    fun getResourceNameFromClassByID(aClass: Class<*>, resourceID: Int): String {
        /* Get all Fields from the class passed. */
        val drawableFields = aClass.fields

        /* Loop through all Fields. */
        for (f in drawableFields) {
            try {
                /* All fields within the subclasses of R
             * are Integers, so we need no type-check here. */

                /* Compare to the resourceID we are searching. */
                if (resourceID == f.getInt(null))
                    return f.name // Return the name.
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        /* Throw Exception if nothing was found*/
        throw IllegalArgumentException()
    }

}
