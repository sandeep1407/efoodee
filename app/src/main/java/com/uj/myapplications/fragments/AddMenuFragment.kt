package com.uj.myapplications.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.activities.EditProfileActivity
import com.uj.myapplications.activities.MenuPreviewActivity
import com.uj.myapplications.adapters.AdapterMenuExtraItems
import com.uj.myapplications.adapters.AdapterView_Demo
import com.uj.myapplications.adapters.ImageUploadAdpater
import com.uj.myapplications.interfaces.IImageCompressTaskListener
import com.uj.myapplications.pojo.MenuPojo.Content
import com.uj.myapplications.pojo.MenuPojo.Extra
import com.uj.myapplications.pojo.MenuPojo.MenuDetailPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.custom_add_menu.*
import kotlinx.android.synthetic.main.customadd_menu_extra.*
import kotlinx.android.synthetic.main.fragment_add_menu.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import rebus.permissionutils.*
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.io.Serializable
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.concurrent.Executors


// TODO: Rename parameter arguments, choose names that match
/**
 * A simple [Fragment] subclass.
 *
 */
class AddMenuFragment : Fragment(), View.OnClickListener, ImageUploadAdpater.ItemClickListener, FullCallback,
    CustomDialog.ChooseImageFromListener {
    companion object {
        @JvmField
        var map: HashMap<String, Int> = HashMap()
        @JvmField
        var mapExtraItems: HashMap<String, Int> = HashMap()
        var mealType: Int = 0 // 0=Lunch 1= Dinner and 2 = Breakfast 3= other
        var caller = ""
        var photos = ArrayList<String>()
        var deletedPhotos = ArrayList<String>()
        var menuPhotosFromServer = ArrayList<String>()
    }

    //create a single thread pool to our image compression class.
    private var mExecutorService = Executors.newFixedThreadPool(1)
    private var imageCompressTask: ImageCompressTask? = null
    var menuId = ""
    var value = 0
    var flagIsSchedule = false
    var flagPublish = false
    var position: Int = 0
    val jsonObjectMain = JSONObject()
    private var response_str: String? = ""
    private var message: String? = ""
    var adapter: AdapterView_Demo? = null
    var adapterMEnuExtraItems: AdapterMenuExtraItems? = null
    var menuDetailPojo: MenuDetailPojo? = null
    var mThumbIds = arrayOf<String>(
        "",
        "",
        "",
        ""
    )
    var fragment: Fragment? = null

    private var mPermissionFlag = false
    var listContents: ArrayList<Content> = ArrayList<Content>()
    var listExtraItems: ArrayList<Extra> = ArrayList<Extra>()

    override fun onItemClick(position: Int) {
        Log.e("onItemClick : ", position.toString())
        this.position = position
        UtilityClass.showToast(activity!!, "Upload Image")
        // Image Picker Code
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
            Timber.e("Permission not granted")
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // Image Picker Default Configuration
        var config = EasyImage.configuration(activity)
        config.saveInAppExternalFilesDir()
        /*config.saveInRootPicturesDirectory()
        config.setImagesFolderName(EditProfileActivity.FOLDER_NAME)
        config.setCopyExistingPicturesToPublicLocation(true)*/
        photos.clear()
        deletedPhotos.clear()
        menuPhotosFromServer.clear()
        return inflater.inflate(R.layout.fragment_add_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnVeg.setOnClickListener(this)
        btnNonVeg.setOnClickListener(this)
        iBtnUploadImage.setOnClickListener(this)
        btnDineIn.setOnClickListener(this)
        btnSelfDelivery.setOnClickListener(this)
        btnMymaDelivery.setOnClickListener(this)
        btnSelfPickup.setOnClickListener(this)
        btnMild.setOnClickListener(this)
        btnMedium.setOnClickListener(this)
        btnHot.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        ivAddView.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnSaveExtra.setOnClickListener(this)
        ivAddViewExtra.setOnClickListener(this)
        btnPreviewMenu.setOnClickListener(this)
        btnSchedule.setOnClickListener(this)
        btnPublishNow.setOnClickListener(this)
        ivAdd.setOnClickListener(this)
        ivminus.setOnClickListener(this)
        ivBack.setOnClickListener(this)
        providePermission()
        if (savedInstanceState != null) {
            photos = savedInstanceState.getSerializable(EditProfileActivity.PHOTOS_KEY) as ArrayList<String>
        }

        // Intialize Recyler view android
        recyclerView_imagesupload.layoutManager = GridLayoutManager(activity, 2)
        recyclerView_imagesupload.hasFixedSize()
        // recyclerView_imagesupload.isNestedScrollingEnabled = true


        // Menu List Part 2
        adapter = AdapterView_Demo(activity!!, listContents)
        recyclerView_menuItems.layoutManager = LinearLayoutManager(activity!!)
        recyclerView_menuItems.itemAnimator = DefaultItemAnimator()
        recyclerView_menuItems.adapter = adapter

        // Extras Recycler View
        adapterMEnuExtraItems = AdapterMenuExtraItems(activity!!, listExtraItems)
        recyclerView_menuItems_extra.layoutManager = LinearLayoutManager(activity!!)
        recyclerView_menuItems_extra.itemAnimator = DefaultItemAnimator()
        recyclerView_menuItems_extra.adapter = adapterMEnuExtraItems
        val user = Config.getUserData()
        if (user != null) {
            val serviceType = user.serviceType
            when (serviceType) {
                0 -> {
                    btnSelfDelivery.isEnabled = false
                    btnSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_grey)
                    btnSelfDelivery.setTextColor(resources.getColor(R.color.gray))
                }
                1 -> {
                    //All Will be active
                }
                2 -> {
                    //Only No Addtion of Menu
                }
            }
        }

        when (caller) {
            "EDIT" -> {
                //1. Change Title
                textViewTitle.text = "Edit Menu"
                // Hit URL for Menu detail
                // getMenuDetail(menuID)
                // Hit Url to Update.
                val arguments = arguments
                if (arguments != null) {
                    //get Menu Id
                    imageAdapter = ImageUploadAdpater(mThumbIds, activity!!, 1)
                    imageAdapter!!.setClickListener(this)
                    recyclerView_imagesupload.adapter = imageAdapter
                    imageAdapter!!.notifyDataSetChanged()
                    menuId = arguments.getString("menuID")
                    if (UtilityClass.isInternetAvailable(activity!!)) {
                        val url = RestUrls.GET_MENU_Detail + menuId
                        Log.e("URL", url)
                        getMenuDetail(url)
                    } else {
                        UtilityClass.showToast(activity!!, "Please check internet connection")
                    }
                } else {
                    textViewTitle.text = "Add Menu"
                }
            }
            else -> {
                imageAdapter = ImageUploadAdpater(mThumbIds, activity!!, 0)
                //Case Add
                imageAdapter!!.setClickListener(this)
                recyclerView_imagesupload.adapter = imageAdapter
                imageAdapter!!.notifyDataSetChanged()
            }
        }
        // Ends Here
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(EditProfileActivity.PHOTOS_KEY, photos)
    }

    var imageAdapter: ImageUploadAdpater? = null
    var flagSelfDel: Boolean = false
    var flagSelfPick: Boolean = false
    var flagMymaDel: Boolean = false
    var flagDinein: Boolean = false
    var DineIn: Int = 1 // Deafult value
    var mymaDel: Int = 0 // Deafult value
    var selfDel: Int = 0 // Deafult value
    var selfPick: Int = 0 // Deafult value
    var veg_nonVeg: Int = 0 // 0 -means Veg and 1 - means non veg
    var spicyIndex: Int = 0 // 0 -means mild and 1 - means medium and 2- means HOT

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ivBack -> {
                if (ivBack.visibility == View.VISIBLE) {
                    ivBack.visibility = View.GONE
                    // Load Previous State
                    ll_menu_1.visibility = View.VISIBLE
                    ll_menu_part2.visibility = View.GONE
                }
            }

            R.id.btnVeg -> {
                //0changeColor
                btnNonVeg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnNonVeg.setTextColor(resources.getColor(R.color.gray))
                btnVeg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnVeg.setTextColor(resources.getColor(R.color.white))
                veg_nonVeg = 0

            }
            R.id.btnNonVeg -> {
                btnNonVeg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnNonVeg.setTextColor(resources.getColor(R.color.white))
                btnVeg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnVeg.setTextColor(resources.getColor(R.color.gray))
                veg_nonVeg = 1
            }
            R.id.btnSelfDelivery -> {
                if (flagSelfDel) {
                    btnSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnSelfDelivery.setTextColor(resources.getColor(R.color.gray))
                    flagSelfDel = false
                    selfDel = 0
                } else {
                    btnSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnSelfDelivery.setTextColor(resources.getColor(R.color.white))
                    flagSelfDel = true
                    selfDel = 1
                }
            }
            R.id.btnSelfPickup -> {
                if (flagSelfPick) {
                    btnSelfPickup.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnSelfPickup.setTextColor(resources.getColor(R.color.gray))
                    flagSelfPick = false
                    selfPick = 0
                } else {
                    btnSelfPickup.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnSelfPickup.setTextColor(resources.getColor(R.color.white))
                    flagSelfPick = true
                    selfPick = 1
                }
            }
            R.id.btnMymaDelivery -> {
                if (flagMymaDel) {
                    btnMymaDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnMymaDelivery.setTextColor(resources.getColor(R.color.gray))
                    flagMymaDel = false
                    mymaDel = 0
                } else {
                    btnMymaDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnMymaDelivery.setTextColor(resources.getColor(R.color.white))
                    flagMymaDel = true
                    mymaDel = 1
                }
            }
            R.id.btnDineIn -> {
                if (flagDinein) {
                    btnDineIn.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnDineIn.setTextColor(resources.getColor(R.color.gray))
                    flagDinein = false
                    DineIn = 0
                } else {
                    btnDineIn.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnDineIn.setTextColor(resources.getColor(R.color.white))
                    flagDinein = true
                    DineIn = 1
                }
            }
            R.id.iBtnUploadImage -> {

            }
            R.id.btnSave -> {
                if (listContents.size < 20) {
                    if (tvItem.text.toString().isEmpty()) {
                        UtilityClass.showToast(activity!!, "Can't be blank")
                    } else if (tvItem.text.toString().trim().length < 2) {
                        UtilityClass.showToast(activity!!, "Content name should be  more than 2 charcters")
                    } else {
                        val content = Content()
                        content.name = tvItem.text.toString()
                        listContents.add(content)
                        adapter!!.notifyDataSetChanged()
                        // Add value to Hashmap
                        map[tvItem.text.toString().trim()] = 1
                        // 1 Will be Default value
                        custom_layout.visibility = View.GONE
                        tvItem.setText("")
                    }
                } else {
                    UtilityClass.showToast(
                        activity!!, "20 items allowed to add in menu list."
                    )
                }
            }

            R.id.ivAddView -> {
                custom_layout.visibility = View.VISIBLE
                tvItem.isFocusable = true
                tvItem.isCursorVisible = true
            }
            R.id.ivAddViewExtra -> {
                custom_layout_extra.visibility = View.VISIBLE
                //tvItemExtra.isFocusable = true
                // tvItemExtra.isCursorVisible = true
            }
            R.id.ivAdd -> {
                value = Integer.parseInt(tvMealCountValue.getText().toString())
                if (value <= 29) {
                    value += 1
                    tvMealCountValue.text = value.toString() + ""
                } else {
                    UtilityClass.shakeItemView(tvMealCountValue, activity!!)
                    UtilityClass.showToast(
                        activity!!, "Max value reached"
                    )
                }
            }
            R.id.ivminus -> {
                if (tvMealCountValue.text.toString() != "1") {
                    value = Integer.parseInt(tvMealCountValue.getText().toString())
                    value -= 1
                    tvMealCountValue.text = value.toString() + ""
                } else {
                    UtilityClass.showToast(activity!!, "Minimum cart value required")
                }
            }

            R.id.btnSaveExtra -> {
                if (listExtraItems.size < 15) {
                    if (tvItemExtra.text.toString().isEmpty() && !etRupeesExtra.text.toString().isEmpty()) {
                        UtilityClass.showToast(activity!!, "Item name and Price can't be blank")
                    } else if (tvItemExtra.text.toString().trim().length < 2) {
                        UtilityClass.showToast(activity!!, "Extra Content name should be  more than 2 charcters")
                    } else if (Integer.parseInt(etRupeesExtra.text.toString().trim()) < 5) {
                        UtilityClass.showToast(activity!!, "Extra Item cost should be more than Rs.5/-")
                    } else {
                        val content = Extra()
                        content.name = tvItemExtra.text.toString()
                        content.price = Integer.parseInt(etRupeesExtra.text.toString().trim())
                        listExtraItems.add(content)
                        adapterMEnuExtraItems!!.notifyDataSetChanged()
                        // Add value to Hashmap
                        mapExtraItems[tvItemExtra.text.toString().trim()] =
                                Integer.parseInt(etRupeesExtra.text.toString().trim())
                        // 1 Will be Default value
                        custom_layout_extra.visibility = View.GONE
                        tvItemExtra.setText("")
                    }

                } else {
                    UtilityClass.showToast(
                        activity!!, "15 extra items allowed to add in menu list."
                    )
                }
            }
            R.id.btnNext -> {
                //  UtilityClass.showToast(activity!!, "Work in progress.")
                if (etMenuNameEnglish.text.isNullOrEmpty()) {
                    // UtilityClass.shakeItemView(tlMobileNumber, activity!!)
                    UtilityClass.showToast(activity!!, "Menu Name " + Constants.ERR_CANT_BE_EMPTY)
                } else if (etMenuNameEnglish.text.toString().trim().length < 2) {
                    // UtilityClass.shakeItemView(tlMobileNumber, activity!!)
                    UtilityClass.showToast(activity!!, "Menu name should be more than 2 character long.")
                } else if (etMenuNameHindi.text.isNullOrEmpty()) {
                    // UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(activity!!, "Native Menu Name " + Constants.ERR_CANT_BE_EMPTY)
                } else if (etMenuNameHindi.text.toString().trim().length < 2) {
                    // UtilityClass.shakeItemView(tlMobileNumber, activity!!)
                    UtilityClass.showToast(activity!!, "Menu Native name should be more than 2 character long.")
                } else if (etMenuDescription.text.isNullOrEmpty()) {
                    // UtilityClass.shakeItemView(tlPassword, this)
                    UtilityClass.showToast(activity!!, "Menu Description " + Constants.ERR_CANT_BE_EMPTY)
                } else if (etMenuDescription.text.toString().trim().length < 2) {
                    // UtilityClass.shakeItemView(tlMobileNumber, activity!!)
                    UtilityClass.showToast(activity!!, "Menu Description should be more than 2 character long.")
                } else {
                    if (caller == "EDIT") {
                        if (menuPhotosFromServer.size > 0) {
                            if (deletedPhotos.size == 4 && photos.size == 0) {
                                UtilityClass.showToast(activity!!, "Pick a image to update menu")
                            } else if ((deletedPhotos.size == menuPhotosFromServer.size) && photos.size == 0) {
                                UtilityClass.showToast(activity!!, "Pick a image to update menu")
                            } else {
                                // Hide First view
                                // Show Second Menu View
                                ivBack.visibility = View.VISIBLE
                                ll_menu_1.visibility = View.GONE
                                ll_menu_part2.visibility = View.VISIBLE
                            }
                        } else {
                            UtilityClass.showToast(activity!!, "Pick a image to update menu")
                        }
                    } else {
                        if (photos.size == 0) {
                            UtilityClass.showToast(activity!!, "Pick a image to add menu")
                        } else {
                            // Hide First view
                            // Show Second Menu View
                            ivBack.visibility = View.VISIBLE
                            ll_menu_1.visibility = View.GONE
                            ll_menu_part2.visibility = View.VISIBLE
                        }
                    }
                }

            }
            R.id.btnMild -> {
                btnMedium.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnHot.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMedium.setTextColor(resources.getColor(R.color.gray))
                btnHot.setTextColor(resources.getColor(R.color.gray))
                btnMild.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnMild.setTextColor(resources.getColor(R.color.white))
                spicyIndex = 0
            }
            R.id.btnMedium -> {
                btnMild.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMild.setTextColor(resources.getColor(R.color.gray))
                btnHot.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnHot.setTextColor(resources.getColor(R.color.gray))
                btnMedium.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnMedium.setTextColor(resources.getColor(R.color.white))
                spicyIndex = 1
            }
            R.id.btnHot -> {
                btnMild.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMild.setTextColor(resources.getColor(R.color.gray))
                btnMedium.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMedium.setTextColor(resources.getColor(R.color.gray))
                btnHot.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnHot.setTextColor(resources.getColor(R.color.white))
                spicyIndex = 2
            }

            R.id.btnPreviewMenu -> {
                if (listContents.size == 0) {
                    UtilityClass.showToast(activity!!, "Please enter menu contents")
                } else if (!etTotalPrice.text.isNotEmpty()) {
                    UtilityClass.showToast(activity!!, "Enter Total Price")
                } else {
                    UtilityClass.showToast(activity!!, "Work in Progress")
                    if (caller == "EDIT") {
                        val intent = Intent(activity, MenuPreviewActivity::class.java)
                        val args = Bundle()
                        args.putSerializable("menuList", listContents as Serializable)
                        args.putString("jsonObj", makeJsonObjectForEditMenu(menuDetailPojo!!).toString())
                        intent.putExtra("data", args)
                        startActivity(intent)
                    } else {
                        val intent = Intent(activity, MenuPreviewActivity::class.java)
                        val args = Bundle()
                        args.putSerializable("menuList", listContents as Serializable)
                        args.putString("jsonObj", makeJsonObjectForAddMenu().toString())
                        intent.putExtra("data", args)
                        startActivity(intent)
                    }
                }
            }
            R.id.btnPublishNow -> {
                // Publish Directly without Schedule
                if (caller == "EDIT") {
                    // Publish with Schedule
                    if (listContents.size == 0) {
                        UtilityClass.showToast(activity!!, "Please enter menu contents")
                    } else if (!etTotalPrice.text.isNotEmpty()) {
                        UtilityClass.showToast(activity!!, "Enter Total Price")
                    } else {
                        flagPublish = true
                        fragment = ScheduleMenuFragment()
                        val args = Bundle()
                        args.putSerializable("menuPhotosList", photos as Serializable)
                        args.putSerializable("jsonObj", makeJsonObjectForEditMenu(menuDetailPojo!!).toString())
                        args.putString("menuID", menuId)
                        args.putString(RestTags.FROM, "Publish")
                        fragment?.arguments = args
                        UtilityClass.switchToFragment(
                            fragment,
                            ScheduleMenuFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }

                } else {
                    if (listContents.size == 0) {
                        UtilityClass.showToast(activity!!, "Please enter menu contents")
                    } else if (!etTotalPrice.text.isNotEmpty()) {
                        UtilityClass.showToast(activity!!, "Enter Total Price")
                    } else {
                        flagPublish = true
                        fragment = ScheduleMenuFragment()
                        val args = Bundle()
                        args.putSerializable("menuPhotosList", photos as Serializable)
                        args.putSerializable("jsonObj", makeJsonObjectForAddMenu().toString())
                        args.putString(RestTags.FROM, "Publish")
                        /*args.putString("menuID", menuId)
                        //args.putString("menuID", menuId)*/
                        fragment?.arguments = args
                        UtilityClass.switchToFragment(
                            fragment,
                            ScheduleMenuFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }
                }
            }

            R.id.btnSchedule -> {
                // Publish with Schedule
                if (caller == "EDIT") {
                    if (listContents.size == 0) {
                        UtilityClass.showToast(activity!!, "Please enter menu contents")
                    } else if (!etTotalPrice.text.isNotEmpty()) {
                        UtilityClass.showToast(activity!!, "Enter Total Price")
                    } else {
                        flagIsSchedule = true
                        fragment = ScheduleMenuFragment()
                        val args = Bundle()
                        args.putSerializable("menuPhotosList", photos as Serializable)
                        args.putSerializable("jsonObj", makeJsonObjectForEditMenu(menuDetailPojo!!).toString())
                        args.putString("menuID", menuId)
                        args.putString(RestTags.FROM, "Schedule")
                        //args.putString("menuID", menuId)
                        fragment?.arguments = args
                        UtilityClass.switchToFragment(
                            fragment,
                            ScheduleMenuFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }
                } else {
                    if (listContents.size == 0) {
                        UtilityClass.showToast(activity!!, "Please enter menu contents")
                    } else if (!etTotalPrice.text.isNotEmpty()) {
                        UtilityClass.showToast(activity!!, "Enter Total Price")
                    } else {
                        flagIsSchedule = true
                        fragment = ScheduleMenuFragment()
                        val args = Bundle()
                        args.putSerializable("menuPhotosList", photos as Serializable)
                        args.putSerializable("jsonObj", makeJsonObjectForAddMenu().toString())
                        args.putString("menuID", menuId)
                        args.putString(RestTags.FROM, "Schedule")
                        //args.putString("menuID", menuId)
                        fragment?.arguments = args
                        UtilityClass.switchToFragment(
                            fragment,
                            ScheduleMenuFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }
                }

            }
        }
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
        AlertDialog.Builder(activity!!)
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

    private fun onPhotosReturned(returnedPhoto: File) {
        /* photos.add(returnedPhoto)
         mThumbIds[position] = returnedPhoto.path
         imageAdapter?.notifyDataSetChanged()*/
        imageCompressTask = ImageCompressTask(activity!!, returnedPhoto.path, iImageCompressTaskListener)
        mExecutorService.execute(imageCompressTask)
        // UtilityClass.setImageFromUrlOrFile(activity!!, returnedPhoto.path, profile_image)
        photos?.forEach { System.out.print(it) }

    }

    override fun onDestroy() {
        // Clear any configuration that was done!
        // EasyImage.clearConfiguration(activity!!)
        super.onDestroy()
    }

    override fun selectedImageFrom(from: Int) {
        when (from) {
            1 -> {
                // UtilityClass.showToast(activity!!, "camera")
                EasyImage.openCamera(this, 0)
            }
            2 -> {
                //  UtilityClass.showToast(activity!!, "gallery")
                EasyImage.openGallery(this, 0)
            }
        }
    }


    /**
     * @param flag =1 Add
     * @param flag =2 Update
     * */
    @SuppressLint("StaticFieldLeak")
    fun getMenuResponse(jsonObject: JSONObject, menuID: String, flag: Int) {
        object : AsyncTask<String, Void, JSONObject>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): JSONObject? {
                try {

                    val res =
                        OkhttpRequestUtils.menuResponse(
                            flag,
                            Config.geToken(),
                            photos,
                            jsonObject.toString(), menuID
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
                    if (res.optBoolean("success") && res.optInt("statusCode") == 200) {
                        Log.e("Menu", "success")
                        fragment = AddMenuSuccessFragment()
                        UtilityClass.switchToFragment(
                            fragment,
                            AddMenuSuccessFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
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


    fun makeJsonObjectForAddMenu(): JSONObject {
        val userData = Config.getUserData()
        val jsonObjectName = JSONObject()
        val jsonArrayDeletedPhotos = JSONArray(deletedPhotos)
        jsonObjectName.put("nameEnglish", etMenuNameEnglish.text.toString().trim())
        jsonObjectName.put("nameNative", etMenuNameHindi.text.toString().trim())

        //**********************
        jsonObjectMain.put("supplier", userData?.id)
        jsonObjectMain.put("desciption", etMenuDescription.text.toString())
        jsonObjectMain.put("cuisine", "Maharastrian")
        jsonObjectMain.put("price", Integer.parseInt(etTotalPrice.text.toString().trim()))
        jsonObjectMain.put("menu_type", veg_nonVeg.toString())
        jsonObjectMain.put("service_type", "1")//Set Dynamically but right now its not clear from client side.
        jsonObjectMain.put("spice_index", spicyIndex)
        jsonObjectMain.put("closing_time", UtilityClass.getCurrentTimeInFormat())
        jsonObjectMain.put("scheduled_to", UtilityClass.getCurrentTimeInFormat())
        jsonObjectMain.put("meal_type", mealType)
        jsonObjectMain.put("is_scheduled", flagIsSchedule)
        jsonObjectMain.put("is_published", flagPublish)
        //jsonObjectMain.put("deleted_pics", jsonArrayDeletedPhotos)
        jsonObjectMain.put(
            "extras",
            UtilityClass.makeJsonArrayofHashMapExtraMenuItems(mapExtraItems)
        )
        jsonObjectMain.put("contents", UtilityClass.makeJsonArrayofHashMapMenuItems(map))
        jsonObjectMain.put("name", jsonObjectName)
        Log.e("JsonObjMain", jsonObjectMain.toString())
        return jsonObjectMain
    }

    fun makeJsonObjectForEditMenu(menuDetailPojo: MenuDetailPojo): JSONObject {
        //  fun makeJsonObjectForEditMenu(): JSONObject {
        if (menuDetailPojo != null) {
            val userData = Config.getUserData()
            val jsonObjectName = JSONObject()
            val jsonArrayDeletedPhotos = JSONArray(deletedPhotos)
            jsonObjectName.put("nameEnglish", etMenuNameEnglish.text.toString().trim())
            jsonObjectName.put("nameNative", etMenuNameHindi.text.toString().trim())
            //**********************
            jsonObjectMain.put("supplier", userData?.id)
            jsonObjectMain.put("desciption", etMenuDescription.text.toString())
            jsonObjectMain.put("cuisine", "Maharastrian")
            jsonObjectMain.put("price", Integer.parseInt(etTotalPrice.text.toString().trim()))
            jsonObjectMain.put("menu_type", veg_nonVeg.toString())
            jsonObjectMain.put("service_type", userData?.serviceType)
            jsonObjectMain.put("spice_index", spicyIndex)
            jsonObjectMain.put("closing_time", menuDetailPojo?.closingTime)
            jsonObjectMain.put("scheduled_to", menuDetailPojo?.scheduledTo)
            jsonObjectMain.put("meal_type", mealType)
            jsonObjectMain.put("is_scheduled", flagIsSchedule)
            jsonObjectMain.put("is_published", flagPublish)
            jsonObjectMain.put("deleted_pics", jsonArrayDeletedPhotos)
            jsonObjectMain.put(
                "extras",
                UtilityClass.makeJsonArrayofListExtraMenuItems(listExtraItems)
            )
            jsonObjectMain.put("contents", UtilityClass.makeJsonArrayofListMapMenuItems(listContents))
            jsonObjectMain.put("name", jsonObjectName)
            Log.e("JsonObjMainEdit MEnu", jsonObjectMain.toString())

        }
        return jsonObjectMain
    }


    @SuppressLint("StaticFieldLeak")
    private fun getMenuDetail(url: String) {
        object : AsyncTask<String, Void, java.util.HashMap<String, String>>() {
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
                                                menuDetailPojo = Gson().fromJson<MenuDetailPojo>(
                                                    data.toString(),
                                                    MenuDetailPojo::class.java
                                                )
                                                if (menuDetailPojo != null) {
                                                    //SetData in to UI
                                                    setDataToFields(menuDetailPojo!!)
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

    private fun setDataToFields(menuDetailPojo: MenuDetailPojo) {
        var name = menuDetailPojo.name
        etMenuNameEnglish.setText(name.nameEnglish)
        etMenuNameHindi.setText(name.nameNative)
        etMenuDescription.setText(menuDetailPojo.desciption)
        val menuType = menuDetailPojo.menuType
        flagIsSchedule = menuDetailPojo.scheduled
        flagPublish = menuDetailPojo.published
        spicyIndex = menuDetailPojo.spiceIndex
        mealType = menuDetailPojo.mealType
        veg_nonVeg = menuDetailPojo.menuType

        val pics = menuDetailPojo.pics
        if (pics != null) {
            // pics.toTypedArray()

            pics.forEachIndexed { index, element ->
                mThumbIds[index] = element
                menuPhotosFromServer.add(element)
                //photos.add(element)
            }
            imageAdapter?.notifyDataSetChanged()
        }
        //VEG-NONVEG
        when (menuType) {
            1 -> {
                btnNonVeg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnNonVeg.setTextColor(resources.getColor(R.color.white))
                btnVeg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnVeg.setTextColor(resources.getColor(R.color.gray))
                //veg_nonVeg = 1
            }
            0 -> {
                btnNonVeg.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnNonVeg.setTextColor(resources.getColor(R.color.gray))
                btnVeg.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnVeg.setTextColor(resources.getColor(R.color.white))
                // veg_nonVeg = 0
            }
        }
        //Service Type
        val serviceType = menuDetailPojo.serviceType
        /* "service_type must be  0 = DINE-IN, 1 = SELF-PICKUP, 2 = SELF-DELIVERY, 3 = MYMA-DELIVERY
         vehicle_type  must be --- 0 = NA ,1 = bicycle,  2=bike
         area_limit  must be --- 0 = NA ,1 = 0-1km,  2=0-3km, 3=0-5km, 4=5+km
         eg.
             ""vehicle_type"": 1,
         ""area_limit"":1"*/
        if (serviceType.contains("1")) {
            //SelfPickup
            btnSelfPickup.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnSelfPickup.setTextColor(resources.getColor(R.color.white))
        }
        if (serviceType.contains("2")) {
            //SELF-DELIVERY
            btnSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnSelfDelivery.setTextColor(resources.getColor(R.color.white))
        }
        if (serviceType.contains("3")) {
            //3 = MYMA-DELIVERY
            btnMymaDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnMymaDelivery.setTextColor(resources.getColor(R.color.white))
        }
        if (serviceType.contains("0")) {
            //Dine In
            btnDineIn.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnDineIn.setTextColor(resources.getColor(R.color.white))
        }
        //Spicy Index
        val spicyIndex = menuDetailPojo.spiceIndex
        when (spicyIndex) {
            0 -> {
                //Mild
                btnMedium.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnHot.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMedium.setTextColor(resources.getColor(R.color.gray))
                btnHot.setTextColor(resources.getColor(R.color.gray))
                btnMild.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnMild.setTextColor(resources.getColor(R.color.white))
            }

            1 -> {
                //Medium
                btnMild.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMild.setTextColor(resources.getColor(R.color.gray))
                btnHot.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnHot.setTextColor(resources.getColor(R.color.gray))
                btnMedium.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnMedium.setTextColor(resources.getColor(R.color.white))
            }
            2 -> {
                //Hot
                btnMild.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMild.setTextColor(resources.getColor(R.color.gray))
                btnMedium.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMedium.setTextColor(resources.getColor(R.color.gray))
                btnHot.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnHot.setTextColor(resources.getColor(R.color.white))
            }
        }
        //Case contents
        if (!menuDetailPojo.contents.isNullOrEmpty()) {
            listContents.addAll(menuDetailPojo.contents)
            adapter!!.notifyDataSetChanged()
            //recyclerView_menuItems.adapter(this, contentsArray)
        }
        /*val list: MutableList<Content>? = menuDetailPojo.contents
        if (!menuDetailPojo.contents.isNullOrEmpty()) {
            listContents.addAll(list)
            //recyclerView_menuItems.adapter(this, contentsArray)
        }*/
        // Case Extra
        if (!menuDetailPojo.extras.isNullOrEmpty()) {
            listExtraItems.addAll(menuDetailPojo.extras)
            adapterMEnuExtraItems!!.notifyDataSetChanged()
            //recyclerView_menuItems.adapter(this, contentsArray)
        }
        etTotalPrice.setText(menuDetailPojo.price.toString())
    }

    //image compress task callback
    private var iImageCompressTaskListener = object : IImageCompressTaskListener {
        override fun onError(error: Throwable) {
            //very unlikely, but it might happen on a device with extremely low storage.
            //log it, log.WhatTheFuck?, or show a dialog asking the user to delete some files....etc, etc
            Log.wtf("ImageCompressor", "Error occurred", error)
        }

        override fun onComplete(compressed: List<File>) {
            //photo compressed. Yay!
            //prepare for uploads. Use an Http library like Retrofit, Volley or async-http-client (My favourite)
            var file = compressed[0]
            if (file != null) {
                //Set Image to menu and add in array also
                if (caller == "EDIT") {
                    //
                    if (menuPhotosFromServer.size > 0) {
                        when (position) {
                            0 -> {
                                if (!deletedPhotos.contains(menuPhotosFromServer[0]))
                                    deletedPhotos.add(menuPhotosFromServer[0])
                            }
                            1 -> {
                                if (menuPhotosFromServer.size > 1) {
                                    if (!deletedPhotos.contains(menuPhotosFromServer[1]))
                                        deletedPhotos.add(menuPhotosFromServer[1])
                                }
                            }
                            2 -> {
                                if (menuPhotosFromServer.size > 2) {
                                    if (!deletedPhotos.contains(menuPhotosFromServer[2]))
                                        deletedPhotos.add(menuPhotosFromServer[2])
                                }
                            }
                            3 -> {
                                if (menuPhotosFromServer.size > 3) {
                                    if (!deletedPhotos.contains(menuPhotosFromServer[3]))
                                        deletedPhotos.add(menuPhotosFromServer[3])
                                }
                            }
                        }
                    }
                }
                photos.add(file.path)
                mThumbIds[position] = file.path
                imageAdapter?.notifyDataSetChanged()
            }
        }
    }

}
