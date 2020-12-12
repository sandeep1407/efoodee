package com.uj.myapplications.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.uj.myapplications.R
import com.uj.myapplications.adapters.AdapterViewMenuListPreview
import com.uj.myapplications.adapters.AdapterView_Demo
import com.uj.myapplications.fragments.AddMenuFragment
import com.uj.myapplications.pojo.MenuPojo.Content
import com.uj.myapplications.pojo.UserAddressPojo
import com.uj.myapplications.utility.Config
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.activity_menu_preview.*
import org.json.JSONObject

class MenuPreviewActivity : AppCompatActivity() {
    var adapter: AdapterViewMenuListPreview? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_preview)
        ivBack.setOnClickListener {
            finish()
        }
        val intent = intent
        if (intent != null) {
            val args = intent.getBundleExtra("data")
            val list = args.getSerializable("menuList") as ArrayList<Content>
            val obj = args.getString("jsonObj")
            val jsonObject = JSONObject(obj)
            adapter = AdapterViewMenuListPreview(this, list)
            rvDishes.hasFixedSize()
            rvDishes.layoutManager = LinearLayoutManager(this)
            rvDishes.itemAnimator = DefaultItemAnimator()
            rvDishes.adapter = adapter
            if (jsonObject != null) {
                val userData = Config.getUserData()
                if (userData != null) {
                    val name = userData.name
                    if (name != null) {
                        tvPersonName.text = name.fname.plus(" " + name.lname)
                        val userAddressPojo = userData.userAddress
                        if (userAddressPojo != null) {
                            tvPersonPlace.text = userAddressPojo.line1
                            txt_address_top.text = userAddressPojo.line1
                        } else {
                            tvPersonPlace.text = "N/A"
                        }
                    }
                }
                val nameObject = jsonObject.getJSONObject("name")
                tvDishNameEnglish.text = nameObject.getString("nameEnglish")
                tvDishNameHindi.text = nameObject.getString("nameNative")
                tvDishDiscription.text = jsonObject.getString("desciption")
                tvCuisine.text = jsonObject.getString("cuisine")
                tvTotalPrice.text = jsonObject.getString("price")
                //  tvClosingTime.text = "Order Closing Time "+jsonObject.getString("price")
                UtilityClass.setImageFromUrlOrFileProfile(this, Config.getProfileCImage()!!, profile_image)

                UtilityClass.setImageFromUrlOrFile(this, AddMenuFragment.photos[0], ivDishImage)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }


}
