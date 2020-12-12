package com.uj.myapplications.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.activities.DashboardActivity
import com.uj.myapplications.activities.EditProfileActivity
import com.uj.myapplications.interfaces.IImageCompressTaskListener
import com.uj.myapplications.utility.Config
import com.uj.myapplications.utility.CustomDialog
import com.uj.myapplications.utility.ImageCompressTask
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_only_delivery.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import rebus.permissionutils.PermissionEnum
import rebus.permissionutils.PermissionUtils
import timber.log.Timber
import java.io.File
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class OnlyDeliveryFragment : Fragment(), View.OnClickListener, CustomDialog.ChooseImageFromListener {
    companion object {
        var btnClickFlag = ""
    }

    private var mExecutorService = Executors.newFixedThreadPool(1)
    private var imageCompressTask: ImageCompressTask? = null
    private var photos = ArrayList<File>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_only_delivery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            photos = savedInstanceState.getSerializable(EditProfileActivity.PHOTOS_KEY) as ArrayList<File>
        }
        // Config
        // Image Picker Default Configuration
        var config = EasyImage.configuration(activity)
        config.saveInAppExternalFilesDir()
        config.saveInRootPicturesDirectory()
        config.setImagesFolderName(EditProfileActivity.FOLDER_NAME)
        config.setCopyExistingPicturesToPublicLocation(true)
        // Intialize UI
        intilizeUIWithPreviousConfig()
        btnBike.setOnClickListener(this)
        btnBicycle.setOnClickListener(this)
        btn0to1.setOnClickListener(this)
        btn0to3.setOnClickListener(this)
        btn0to5.setOnClickListener(this)
        btn5plus.setOnClickListener(this)
        //Dl
        btnDLDisable.setOnClickListener(this)
        btnDL.setOnClickListener(this)
        btnDLView.setOnClickListener(this)
        btnDLDelete.setOnClickListener(this)
        //RC
        btnBikeRCDisable.setOnClickListener(this)
        btnBikeRC.setOnClickListener(this)
        btnRCView.setOnClickListener(this)
        btnRCDelete.setOnClickListener(this)

        //Pan
        btnPancard.setOnClickListener(this)//Disable
        btnPanGreen.setOnClickListener(this)
        btnPanView.setOnClickListener(this)
        btnPanDelete.setOnClickListener(this)

        //Adhaar
        btnAadharCard.setOnClickListener(this)//Disable
        btnAdhaarGreen.setOnClickListener(this)
        btnaaDharView.setOnClickListener(this)
        btnaaDharDelete.setOnClickListener(this)

        btnNext_Only_Delivery.setOnClickListener {
            //Save The Details with us in temp user object
            ServicesFragment.userTempObj.areaLimit = flagKM
            ServicesFragment.userTempObj.vehicleType = flagBIKE
            if (flagBIKE == 2) {
                // Check for DL and RC
                if (Config.getDLImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert Driving License(DL) photo")
                } else if (Config.getBIKERCImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert Bike RC photo")
                } else if (Config.getPANImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert PAN Card photo")
                } else if (Config.getADHAARImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert Aadhar Card photo")
                } else {
                    // Go To Preference Activity
                    var intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                }
            } else {
                //Case Bicycle
                if (Config.getPANImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert PAN Card photo")
                } else if (Config.getADHAARImage().isNullOrEmpty()) {
                    UtilityClass.showToast(activity!!, "Please insert Aadhar Card photo")
                } else {
                    // Go To Preference Activity
                    var intent = Intent(activity, DashboardActivity::class.java)
                    startActivity(intent)
                }

            }

            /* val userData = Config.getUserData()
             if (userData != null) {
                 userData.areaLimit = flagKM
                 userData.vehicleType = flagBIKE
             }
             Config.setUserData(userData!!)*/
            // Move Prefernces Screen

            //  activity!!.finish()
        }
    }

    private fun intilizeUIWithPreviousConfig() {
        val userData = Config.getUserData()
        if (userData != null) {
            val areaLimit = userData?.areaLimit
            val vechileType = userData?.vehicleType
            when (vechileType) {
                1 -> {
                    btnBicycle.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnBicycle.setTextColor(resources.getColor(R.color.white))
                }
                2 -> {
                    btnBike.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnBike.setTextColor(resources.getColor(R.color.white))
                }
                else -> {
                    //Default Case
                    btnBicycle.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnBicycle.setTextColor(resources.getColor(R.color.white))
                }
            }
            when (areaLimit) {
                1 -> {
                    btn0to1.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn0to1.setTextColor(resources.getColor(R.color.white))
                }
                2 -> {
                    btn0to3.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn0to3.setTextColor(resources.getColor(R.color.white))
                }
                3 -> {
                    btn0to5.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn0to5.setTextColor(resources.getColor(R.color.white))
                }
                4 -> {
                    btn5plus.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn5plus.setTextColor(resources.getColor(R.color.white))
                }
                else -> {
                    //Default case
                    btn0to1.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btn0to1.setTextColor(resources.getColor(R.color.white))

                }

            }
            // Image Buttons Enabling and Disabling
            //Dl
            if (!Config.getDLImage().isNullOrEmpty()) {
                //
                llDl_green.visibility = View.VISIBLE
                btnDLDisable.visibility = View.GONE
            } else {
                llDl_green.visibility = View.GONE
                btnDLDisable.visibility = View.VISIBLE
            }
            // Bike RC
            if (!Config.getBIKERCImage().isNullOrEmpty()) {
                //
                ll_bikeRCGreen.visibility = View.VISIBLE
                btnBikeRCDisable.visibility = View.GONE
            } else {
                ll_bikeRCGreen.visibility = View.GONE
                btnBikeRCDisable.visibility = View.VISIBLE
            }
            //Pan
            if (!Config.getPANImage().isNullOrEmpty()) {
                //
                ll_PanGreen.visibility = View.VISIBLE
                btnPancard.visibility = View.GONE
            } else {
                ll_PanGreen.visibility = View.GONE
                btnPancard.visibility = View.VISIBLE
            }

            //aadhar
            if (!Config.getADHAARImage().isNullOrEmpty()) {
                //
                ll_aaDharGreen.visibility = View.VISIBLE
                btnAadharCard.visibility = View.GONE
            } else {
                ll_aaDharGreen.visibility = View.GONE
                btnAadharCard.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(EditProfileActivity.PHOTOS_KEY, photos)
    }

    private fun onPhotosReturned(returnedPhoto: File) {
        photos.add(returnedPhoto)
        photos?.forEach { System.out.print(it) }
        //Note: This work is only done for This fragment
        if (returnedPhoto != null && returnedPhoto.path.isNotEmpty()) {
            when (btnClickFlag) {
                "btnPancard" -> {
                    Config.setPANImage(returnedPhoto.path)
                    //Make Disable button Vis - Gone and ll View Vis - On
                    btnPancard.visibility = View.GONE
                    ll_PanGreen.visibility = View.VISIBLE
                    //Compress Photo with the help of Image Compresser
                    // Than save it to Config
                    imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
                    mExecutorService.execute(imageCompressTask)

                }
                "btnAadharCard" -> {
                    Config.setADHAARImage(returnedPhoto.path)
                    //Make Disable button Vis - Gone and ll View Vis - On
                    btnAadharCard.visibility = View.GONE
                    ll_aaDharGreen.visibility = View.VISIBLE
                    //Compress Photo with the help of Image Compresser
                    // Than save it to Config
                    imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
                    mExecutorService.execute(imageCompressTask)
                }
                "btnDL" -> {
                    Config.setDLImage(returnedPhoto.path)
                    //Make Disable button Vis - Gone and ll View Vis - On
                    btnDLDisable.visibility = View.GONE
                    llDl_green.visibility = View.VISIBLE
                    //Compress Photo with the help of Image Compresser
                    // Than save it to Config
                    imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
                    mExecutorService.execute(imageCompressTask)
                }
                "btnBikeRC" -> {
                    Config.setBIKERCImage(returnedPhoto.path)
                    //Make Disable button Vis - Gone and ll View Vis - On
                    btnBikeRCDisable.visibility = View.GONE
                    ll_bikeRCGreen.visibility = View.VISIBLE
                    //Compress Photo with the help of Image Compresser
                    // Than save it to Config
                    imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
                    mExecutorService.execute(imageCompressTask)
                }
            }
        }
    }

    var flagBIKE = 1 //0= NA,1=Bicycle 2= Bike
    var flagKM = 1 //0= NA,1=0t01 2= 0to3 Km 3 = 0to5 Km, 4= 5Km plus (Area Limit)

    override fun onClick(v: View?) {
        when (v!!.id) {
            //Pan
            R.id.btnPanGreen -> { //Greem Button in ll layout
                btnClickFlag = "btnPancard"
                UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnPanView -> {
                if (!Config.getDLImage().isNullOrEmpty()) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_doc_view, Config.getPANImage()!!)
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to view")
                    //Timber.e("Permission not granted")
                }
            }
            R.id.btnPanDelete -> {
                //1 : PAn Deletion
                if (!Config.getPANImage().isNullOrEmpty()) {
                    showDeleteImageDialog(3)
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to delete")
                }
            }
            //Adhaar

            R.id.btnAdhaarGreen -> { // Green btn in ll view
                btnClickFlag = "btnAadharCard"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnaaDharView -> {
                //Addhar View
                if (!Config.getADHAARImage().isNullOrEmpty()) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_doc_view, Config.getADHAARImage()!!)
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to view")
                    //Timber.e("Permission not granted")
                }
            }
            R.id.btnaaDharDelete -> {
                //1 : Adhaar Deletion
                if (!Config.getADHAARImage().isNullOrEmpty()) {
                    showDeleteImageDialog(4)
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to delete")
                }
            }
            //BikeRc
            R.id.btnBikeRCDisable -> { //Disable Situtation
                btnClickFlag = "btnBikeRC"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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

            R.id.btnDLDisable -> { // Dl Disable
                btnClickFlag = "btnDL"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnBicycle -> {
                //0changeColor
                btnBike.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnBike.setTextColor(resources.getColor(R.color.gray))
                btnBicycle.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnBicycle.setTextColor(resources.getColor(R.color.white))
                flagBIKE = 1
                // Hide the Dl view and Bike Rc View
                llDl_green.visibility = View.GONE
                btnDLDisable.visibility = View.GONE

                ll_bikeRCGreen.visibility = View.GONE
                btnBikeRCDisable.visibility = View.GONE

            }
            R.id.btnBike -> {
                btnBike.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnBike.setTextColor(resources.getColor(R.color.white))
                btnBicycle.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnBicycle.setTextColor(resources.getColor(R.color.gray))
                flagBIKE = 2
                // Show the Dl view and Bike Rc View (According to Previous Config)
                // Image Buttons Enabling and Disabling
                //Dl
                if (!Config.getDLImage().isNullOrEmpty()) {
                    //
                    llDl_green.visibility = View.VISIBLE
                    btnDLDisable.visibility = View.GONE
                } else {
                    llDl_green.visibility = View.GONE
                    btnDLDisable.visibility = View.VISIBLE
                }
                // Bike RC
                if (!Config.getBIKERCImage().isNullOrEmpty()) {
                    //
                    ll_bikeRCGreen.visibility = View.VISIBLE
                    btnBikeRCDisable.visibility = View.GONE
                } else {
                    ll_bikeRCGreen.visibility = View.GONE
                    btnBikeRCDisable.visibility = View.VISIBLE
                }
            }
            R.id.btn0to1 -> {
                btn0to1.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn0to1.setTextColor(resources.getColor(R.color.white))
                btn0to3.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to3.setTextColor(resources.getColor(R.color.gray))
                btn0to5.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to5.setTextColor(resources.getColor(R.color.gray))
                btn5plus.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn5plus.setTextColor(resources.getColor(R.color.gray))
                flagKM = 1
            }
            R.id.btn0to3 -> {
                btn0to3.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn0to3.setTextColor(resources.getColor(R.color.white))
                btn0to1.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to1.setTextColor(resources.getColor(R.color.gray))
                btn0to5.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to5.setTextColor(resources.getColor(R.color.gray))
                btn5plus.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn5plus.setTextColor(resources.getColor(R.color.gray))
                flagKM = 2
            }
            R.id.btn0to5 -> {
                btn0to5.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn0to5.setTextColor(resources.getColor(R.color.white))
                btn0to1.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to1.setTextColor(resources.getColor(R.color.gray))
                btn0to3.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to3.setTextColor(resources.getColor(R.color.gray))
                btn5plus.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn5plus.setTextColor(resources.getColor(R.color.gray))
                flagKM = 3
            }
            R.id.btn5plus -> {
                btn5plus.setBackgroundResource(R.drawable.bg_round_fill_green)
                btn5plus.setTextColor(resources.getColor(R.color.white))
                btn0to1.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to1.setTextColor(resources.getColor(R.color.gray))
                btn0to3.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to3.setTextColor(resources.getColor(R.color.gray))
                btn0to5.setBackgroundResource(R.drawable.bg_round_border_grey)
                btn0to5.setTextColor(resources.getColor(R.color.gray))
                flagKM = 4
            }
            R.id.btnPancard -> {//Disable Button Clicked
                btnClickFlag = "btnPancard"
                UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnAadharCard -> {//Disable Button Clicked
                btnClickFlag = "btnAadharCard"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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

            R.id.btnDL -> {//Green
                btnClickFlag = "btnDL"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnBikeRC -> {//Green
                btnClickFlag = "btnBikeRC"
                //  UtilityClass.showToast(activity!!, "Work in Progress")
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
            R.id.btnDLView -> {
                if (!Config.getDLImage().isNullOrEmpty()) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_doc_view, Config.getDLImage()!!)
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to view")
                    //Timber.e("Permission not granted")
                }
            }
            R.id.btnDLDelete -> {
                //1 : Dl Deletion
                if (!Config.getDLImage().isNullOrEmpty()) {
                    showDeleteImageDialog(1)
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to delete")
                }


            }
            R.id.btnRCView -> {
                if (!Config.getBIKERCImage().isNullOrEmpty()) {
                    val ft = activity!!.supportFragmentManager.beginTransaction()
                    val newFragment =
                        CustomDialog.newInstance(activity!!, R.layout.dialog_image_doc_view, Config.getBIKERCImage()!!)
                    newFragment.setTargetFragment(this, 1)
                    newFragment.show(ft, "dialog")
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to view")
                    //Timber.e("Permission not granted")
                }

            }
            R.id.btnRCDelete -> {
                //2 : Flag for Deletion
                if (!Config.getBIKERCImage().isNullOrEmpty()) {
                    showDeleteImageDialog(2)
                } else {
                    UtilityClass.showToast(activity!!, "Nothing to delete")
                }
            }

        }
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


    override fun onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(activity!!)
        super.onDestroy()
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
                //   UtilityClass.showToast(activity!!, "gallery")
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
                        Config.setDLImage("")
                        btnDLDisable.visibility = View.VISIBLE
                        llDl_green.visibility = View.GONE
                    }
                    2 -> {
                        //Clear Image and make Button Disable // TODO
                        Config.setBIKERCImage("")
                        btnBikeRCDisable.visibility = View.VISIBLE
                        ll_bikeRCGreen.visibility = View.GONE
                    }
                    3 -> {
                        //Clear Image and make Button Disable // TODO
                        Config.setPANImage("")
                        btnPancard.visibility = View.VISIBLE
                        ll_PanGreen.visibility = View.GONE
                    }
                    4 -> {
                        //Clear Image and make Button Disable // TODO
                        // Hide Three Button View
                        //Show Disable Button
                        Config.setADHAARImage("")
                        btnAadharCard.visibility = View.VISIBLE
                        ll_aaDharGreen.visibility = View.GONE
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
        override fun onError(error: Throwable) {
            Log.wtf("ImageCompressor", "Error occurred", error)
        }

        override fun onComplete(compressed: List<File>) {
            //photo compressed. Yay!
            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)
            var file = compressed[0]
            if (file != null) {
                when (btnClickFlag) {
                    "btnPancard" -> {
                        Config.setPANImage(file.path)
                        //Make Disable button Vis - Gone and ll View Vis - On
                        btnPancard.visibility = View.GONE
                        ll_PanGreen.visibility = View.VISIBLE
                        Log.d("ImageCompressor", "New Pan photo size ==> " + file.length()) //log new file size.

                    }
                    "btnAadharCard" -> {
                        Config.setADHAARImage(file.path)
                        //Make Disable button Vis - Gone and ll View Vis - On
                        btnAadharCard.visibility = View.GONE
                        ll_aaDharGreen.visibility = View.VISIBLE
                        Log.d("ImageCompressor", "New Adhaar photo size ==> " + file.length()) //log new file size.

                    }
                    "btnDL" -> {
                        Config.setDLImage(file.path)
                        //Make Disable button Vis - Gone and ll View Vis - On
                        btnDLDisable.visibility = View.GONE
                        llDl_green.visibility = View.VISIBLE
                        Log.d("ImageCompressor", "New DL photo size ==> " + file.length()) //log new file size.

                    }
                    "btnBikeRC" -> {
                        Config.setBIKERCImage(file.path)
                        //Make Disable button Vis - Gone and ll View Vis - On
                        btnBikeRCDisable.visibility = View.GONE
                        ll_bikeRCGreen.visibility = View.VISIBLE
                        Log.d("ImageCompressor", "New Motor Bike photo size ==> " + file.length()) //log new file size.
                    }
                }

            } else {
                Log.d("ImageCompressor", "New photo size ==>File null")
            }
        }
    }
}
