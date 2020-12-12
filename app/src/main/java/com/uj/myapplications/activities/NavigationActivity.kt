package com.uj.myapplications.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.fcm.MyFirebaseMessagingService
import com.uj.myapplications.fragments.*
import com.uj.myapplications.pojo.LoginResponse
import com.uj.myapplications.pojo.UserDataPojo
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.activity_dummy_navigation.*
import kotlinx.android.synthetic.main.app_bar_dummy_navigation.*
import kotlinx.android.synthetic.main.content_dummy_navigation.*
import kotlinx.android.synthetic.main.layout_nav_menu.*
import kotlinx.android.synthetic.main.nav_header_dummy_navigation.*
import org.json.JSONException
import org.json.JSONObject
import timber.log.Timber
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.util.*


class NavigationActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var fragment: Fragment? = null
    }

    private var response_str: String? = ""
    private var message: String? = ""
    var fragManager: FragmentManager? = null

    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy_navigation)
        setSupportActionBar(toolbar)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.gray)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        // Load Default Fragment
        fragManager = this.supportFragmentManager
        loadDefaultFrag()
        // nav_view.setNavigationItemSelectedListener(this)
        //OnClick Listenterns on All Widgets
        btn_about_myma.setOnClickListener(this)
        btn_how_to_vedios.setOnClickListener(this)
        btn_language.setOnClickListener(this)
        btn_suggestions_to_myma.setOnClickListener(this)
        btn_TandC.setOnClickListener(this)
        btn_contact_myma.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        txt_user_name.setOnClickListener(this)
        imageViewProfile.setOnClickListener(this)
        // Check for session valid if not than clear pref and send it to login again
        val jsonObject = JSONObject()
        try {
            jsonObject.put(RestTags.ACCESS_TOKEN, RestTags.PUBLIC_KEY)
            jsonObject.put(RestTags.USER_TYPE, RestTags.S_FLAG)
            jsonObject.put(RestTags.DEVICE_TOKEN, MyFirebaseMessagingService.D_TOKEN)
            jsonObject.put(RestTags.DEVICE_TYPE, RestTags.PLATFORM)
            Timber.e(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (UtilityClass.isInternetAvailable(this)) {
            //  getResponse(RestUrls.LOGIN_URL, jsonObject)
            getResponseSessionValid(RestUrls.IS_SEESION_VALID, jsonObject)

        } else {
            UtilityClass.showToast(this, getString(R.string.device_offline_message))
        }

        val userDataPojo = Config.getUserData()
        try {
            if (userDataPojo != null) {
                val name = userDataPojo.name
                if (name != null) {
                    txt_user_name.text = "${name?.title} ${name?.fname} ${name.lname}"
                } else {
                    txt_user_name.text = "Mr.User"
                }
            } else {
                txt_user_name.text = "Mr.User"
            }
            //Profile Image
            if (Config?.getProfileCImage() != null) {
                UtilityClass.setImageFromUrlOrFileProfile(this, Config.getProfileCImage()!!, imageViewProfile)
            } else {

            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_add_menu -> {
                // message.setText(R.string.add_menu)
                if (!(fragment is MyProfileFragment)) {
                    fragment = MyProfileFragment()
                    // AddMenuFragment.caller = "ADD"
                    // UtilityClass.showToast(this, "AddMenu")
                    UtilityClass.switchToFragment(
                        fragment,
                        "AddMenuFrag",
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                } else {
                    //    UtilityClass.showToast(this, "AddMenu: Already On Screen")
                }
            }

            R.id.navigation_my_orders -> {
                // message.setText(R.string.my_orders)
                if (!(fragment is MyOrdersFragment)) {
                    fragment = MyOrdersFragment()
                    // UtilityClass.showToast(this, "My Orders")
                    UtilityClass.switchToFragment(
                        fragment,
                        "Home",
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                } else {
                    //   UtilityClass.showToast(this, "My Orders: Already On Screen")
                }
            }

            R.id.navigation_inbox -> {
                if (!(fragment is InboxFragment)) {
                    fragment = InboxFragment()
                    // UtilityClass.showToast(this, "Inbox")
                    UtilityClass.switchToFragment(
                        fragment,
                        "InboxFrag",
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                } else {
                    //  UtilityClass.showToast(this, "Inbox: Already On Screen")
                }
            }

            R.id.navigation_account -> {
                if (!(fragment is ViewProfileFragment)) {
                    fragment = ViewProfileFragment()
                    //  UtilityClass.showToast(this, "My Profile")
                    UtilityClass.switchToFragment(
                        fragment,
                        "MyProfileFrag",
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                } else {
                    //  UtilityClass.showToast(this, "My Profile: Already On Screen")
                }
            }
        }
        return@OnNavigationItemSelectedListener true
    }

    override fun onResume() {
        super.onResume()
        val userDataPojo = Config.getUserData()
        try {
            if (userDataPojo != null) {
                val name = userDataPojo.name
                if (name != null) {
                    txt_user_name.text = "${name?.title} ${name?.fname} ${name.lname}"
                } else {
                    txt_user_name.text = "Mr.User"
                }
            } else {
                txt_user_name.text = "Mr.User"
            }
            //Profile Image
            if (Config?.getProfileCImage() != null) {
                UtilityClass.setImageFromUrlOrFileProfile(this, Config.getProfileCImage()!!, imageViewProfile)
            } else {

            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            return
        }

        drawer_layout?.let {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                if (supportFragmentManager.backStackEntryCount == 1) {
                    doubleBackToExitPressedOnce = true
                    Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 5000)
                    Toast.makeText(this, getString(R.string.press_back_again), Toast.LENGTH_SHORT).show()
                } else {
                    val seletedItemId = navigation.selectedItemId
                    if (R.id.home != seletedItemId) {
                        setHomeItem()
                    } else {
                        return
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dummy_navigation, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showLogoutAlertDialog() {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setTitle(getString(R.string.logout))
            .setCancelable(false)
            .setIcon(resources.getDrawable(R.mipmap.ic_launcher))
            .setMessage(getString(R.string.are_yout_logout))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                Config.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, which ->
                // user doesn't want to logout
                dialog.dismiss()
            }
            .show()
    }

    // close drawer any time using this
    fun closeDrawer() {

        drawer_layout.closeDrawer(Gravity.LEFT)
    }

    //ShowSession is Invalid Dialog
    private fun showSessionInvalidAlertDialog() {
        AlertDialog.Builder(ContextThemeWrapper(this, R.style.AlertDialogCustom))
            .setTitle(getString(R.string.session_invalid))
            .setCancelable(false)
            .setIcon(resources.getDrawable(R.mipmap.ic_launcher))
            .setMessage(getString(R.string.session_invalid_string))
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                Config.clear()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .show()
    }

    override fun onClick(view: View) {
        closeDrawer()
        when (view?.id) {

            R.id.btn_about_myma -> {
                //UtilityClass.showToast(this, "About Myma Screen Will open")
                fragment = AboutUsFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    AboutUsFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }

            R.id.btn_how_to_vedios -> {
                //startActivity(Intent(this, RegistrationActivity::class.java))
                UtilityClass.showToast(this, "How to videos Screen Will open")

            }
            R.id.btn_language -> {
                // UtilityClass.showToast(this, "Language change Screen Will open")
                fragment = LanguageChangeFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    LanguageChangeFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }
            R.id.btn_suggestions_to_myma -> {
                // startActivity(Intent(this, ForgotActivity::class.java))
                //  UtilityClass.showToast(this, "Suggestion change Screen Will open")
                fragment = SuggestionsFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    SuggestionsFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }

            R.id.btn_TandC -> {
                //startActivity(Intent(this, RegistrationActivity::class.java))
                //  UtilityClass.showToast(this, "Term and conditions Screen Will open")
                fragment = TermAndConditionsFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    TermAndConditionsFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }
            R.id.btn_contact_myma -> {
                // UtilityClass.showToast(this, "Contact Screen Will open")
                fragment = ContactUsFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    ContactUsFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }
            R.id.btn_logout -> {
                showLogoutAlertDialog()
            }

            R.id.imageViewProfile -> {
                fragment = ViewProfileFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    ViewProfileFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }

            R.id.txt_user_name -> {
                fragment = ViewProfileFragment()
                UtilityClass.switchToFragment(
                    fragment,
                    ViewProfileFragment::class.java.name,
                    R.id.fragment_container,
                    fragManager!!, false
                )
            }

        }
    }

    fun setHomeItem() {
        navigation.selectedItemId = R.id.navigation_add_menu
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        loadDefaultFrag()
    }

    private fun loadDefaultFrag() {
        navigation.selectedItemId = R.id.navigation_add_menu
        fragment = MyProfileFragment()
        UtilityClass.switchToFragment(fragment, "MyProfileFrag", R.id.fragment_container, fragManager!!, true)
    }

    @SuppressLint("StaticFieldLeak")
    fun getResponseSessionValid(url: String, jsonObject: JSONObject) {
        object : AsyncTask<String, Void, HashMap<String, String>>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(this@NavigationActivity)
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
                                                && jsonObject.optBoolean("isValidSession")
                                            ) {
                                                //Save User Object again
                                                val jsonObjectData = jsonObject.optJSONObject("data")
                                                val jsonObjectUser = jsonObjectData.optJSONObject("user")
                                                if (jsonObjectUser != null) {
                                                    val userResponse = Gson().fromJson<UserDataPojo>(
                                                        jsonObjectUser.toString(),
                                                        UserDataPojo::class.java
                                                    )
                                                    if (userResponse != null) {
                                                        if (userResponse.isTermAccepted) {
                                                            Log.e("Hup", "Hup ${userResponse.toString()}")
                                                            Config.setUserData(userResponse)
                                                            /*Now get User Object*/
                                                            val user = Config.getUserData()
                                                            if (user != null) {
                                                                var fssai = user?.fssai
                                                                if (fssai != null) {
                                                                    val documents = userResponse?.documents
                                                                    if (documents != null) {
                                                                        if (documents.fssaiDoc != null) {
                                                                            fssai.userFssaiImagepath =
                                                                                    documents.fssaiDoc
                                                                        }
                                                                    }
                                                                }
                                                                //fssai.userFssaiImagepath = data.user?.documents?.fssaiDoc
                                                                val documents = userResponse?.documents
                                                                if (documents != null) {
                                                                    if (documents.pan != null) {
                                                                        Config.setPANImage(documents.pan!!)
                                                                    }
                                                                    if (documents.fssaiDoc != null) {
                                                                        Config.setFSSAIImage(documents.fssaiDoc!!)
                                                                    }
                                                                    if (documents.adhaar != null) {
                                                                        Config.setADHAARImage(documents.adhaar!!)
                                                                    }
                                                                    if (documents.bikeRc != null) {
                                                                        Config.setBIKERCImage(documents.bikeRc!!)
                                                                    }
                                                                    if (documents.drivingLicense != null) {
                                                                        Config.setDLImage(documents.drivingLicense!!)
                                                                    }
                                                                }
                                                                val profImage = userResponse?.profilePic!!
                                                                if (profImage != null) {
                                                                    Config.setProfileImage(userResponse?.profilePic!!)
                                                                }
                                                            }

                                                        } else {
                                                            //Got To Register Fill the entries
                                                            // GO to Registration Activity
                                                            var intent = Intent(
                                                                this@NavigationActivity,
                                                                RegistrationActivity::class.java
                                                            )
                                                            intent.putExtra(RestTags.FROM, "Splash")
                                                            intent.putExtra("SESSION_NOT_VALID", "SESSION_NOT_VALID")
                                                            startActivity(intent)
                                                            finish()
                                                        }
                                                    }
                                                }
                                            } else {
                                                UtilityClass.showToast(
                                                    this@NavigationActivity,
                                                    if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                                )
                                                showSessionInvalidAlertDialog()
                                            }
                                        } catch (e: JSONException) {
                                            e.printStackTrace()
                                        }

                                    }
                                }
                                400 -> {
                                    UtilityClass.showToast(
                                        this@NavigationActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                500 -> {
                                    UtilityClass.showToast(
                                        this@NavigationActivity,
                                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                                    )
                                    UtilityClass.hideDialog()
                                }
                                //Default case of when
                                else -> {
                                    UtilityClass.showToast(
                                        this@NavigationActivity,
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
                        this@NavigationActivity,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }


}
