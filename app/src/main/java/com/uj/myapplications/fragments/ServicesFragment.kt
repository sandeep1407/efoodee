package com.uj.myapplications.fragments


import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.uj.myapplications.R
import com.uj.myapplications.activities.ForgotActivity
import com.uj.myapplications.activities.RegistrationActivity
import com.uj.myapplications.activities.ServicesActivity
import com.uj.myapplications.pojo.UserDataPojo
import com.uj.myapplications.utility.Config
import com.uj.myapplications.utility.RestTags
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_only_delivery.*
import kotlinx.android.synthetic.main.fragment_services.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ServicesFragment : Fragment(), View.OnClickListener {
    companion object {
        var serviceType: Int = 0//0= Cooking, 1=Cook+Delivery, 2= Only Delivery 4=DineIn
        var caller = "0"
        var userTempObj: UserDataPojo = UserDataPojo()
    }

    var fragment: Fragment? = null
    var fragManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_services, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragManager = activity?.supportFragmentManager
        btnCooking.setOnClickListener(this)
        btnCookingSelfDelivery.setOnClickListener(this)
        btnOnlyDelivery.setOnClickListener(this)
        btnDineIn.setOnClickListener(this)
        btnSubmitService.setOnClickListener(this)
        // First of all fill the temp user object to previous configuration details which comes from server
        val userDataPojo = Config.getUserData()
        if (userDataPojo != null) {
            userTempObj = userDataPojo
        }
        intializePreviousconfig()

    }

    private fun intializePreviousconfig() {
        //Set Buttons into the
        val userDataPojo = Config.getUserData()
        if (userDataPojo != null) {
            val serviceType = userDataPojo?.serviceType
            when (serviceType) {
                0 -> {
                    //Cooking
                    btnCooking.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnCooking.setTextColor(resources.getColor(R.color.white))

                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.gray))

                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.gray))
                }
                1 -> {
                    //Cooking+ Selfdelivery
                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.white))

                    btnCooking.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCooking.setTextColor(resources.getColor(R.color.gray))


                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.gray))

                }
                2 -> {
                    // Only Delivery
                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.white))

                    btnCooking.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCooking.setTextColor(resources.getColor(R.color.gray))

                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.gray))


                }
                3 -> {
                    // Dine In
                }
                else -> {
                    btnCooking.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCooking.setTextColor(resources.getColor(R.color.gray))

                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.gray))

                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.gray))
                }

            }

            /* if (serviceType.contains(0)) {
                 btnCooking.setBackgroundResource(R.drawable.bg_round_fill_green)
                 btnCooking.setTextColor(resources.getColor(R.color.white))
             } else {
                 btnCooking.setBackgroundResource(R.drawable.bg_round_border_grey)
                 btnCooking.setTextColor(resources.getColor(R.color.gray))
             }
             //Self-Pickup and OnlyDelivery
             if (serviceType.contains(2)) {
                 btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                 btnOnlyDelivery.setTextColor(resources.getColor(R.color.white))
             } else {
                 btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                 btnOnlyDelivery.setTextColor(resources.getColor(R.color.gray))
             }
             //Self-Delivery
             if (serviceType.contains(1)) {
                 btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                 btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.white))
             } else {
                 btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                 btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.gray))
             }*/
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    var clickbtnCooking = false
    var clickbtnSelfDelivery = false
    var clickbtnOnLyDelivery = false
    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnCooking -> {
                if (clickbtnCooking) {
                    btnCooking.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCooking.setTextColor(resources.getColor(R.color.gray))
                    clickbtnCooking = false
                } else {
                    btnCooking.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnCooking.setTextColor(resources.getColor(R.color.white))
                    clickbtnCooking = true
                }
                // In both case We will check it should be fssai registered
                serviceType = 0
                fragment = CookingFssaiRegFragment()
                caller = "0"
                var bundle = Bundle()
                bundle.putString(RestTags.FROM, caller)
                fragment?.arguments = bundle
                UtilityClass.switchToFragment(
                    fragment,
                    "cookingFssai",
                    R.id.fragment,
                    fragManager!!,
                    true
                )
            }

            R.id.btnCookingSelfDelivery -> {
                if (clickbtnSelfDelivery) {
                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.gray))
                    clickbtnSelfDelivery = false
                } else {
                    btnCookingSelfDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnCookingSelfDelivery.setTextColor(resources.getColor(R.color.white))
                    clickbtnSelfDelivery = true
                }

                serviceType = 1
                // In both case We will check it should be fssai registered
                fragment = CookingFssaiRegFragment()
                caller = "1"
                var bundle = Bundle()
                bundle.putString(RestTags.FROM, caller)
                fragment?.arguments = bundle
                UtilityClass.switchToFragment(
                    fragment,
                    "cookingFssai",
                    R.id.fragment,
                    fragManager!!,
                    true
                )
            }
            R.id.btnOnlyDelivery -> {
                if (clickbtnOnLyDelivery) {
                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.gray))
                    clickbtnOnLyDelivery = false
                } else {
                    btnOnlyDelivery.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnOnlyDelivery.setTextColor(resources.getColor(R.color.white))
                    clickbtnOnLyDelivery = true
                }
                serviceType = 2
                //Set the value in service type if u are only for delivery boy
                ServicesFragment.userTempObj.serviceType = serviceType
                UtilityClass.switchToFragment(
                    OnlyDeliveryFragment(),
                    "cookingOnlyDelivery",
                    R.id.fragment,
                    fragManager!!, true
                )
            }
            R.id.btnDineIn -> {
                //serviceType = 3
                UtilityClass.showToast(
                    activity!!,
                    "Right now DineIn is on progress, We will come back with this shortly."
                )
                // UtilityClass.switchToFragment(CookingFssaiRegFragment(), "cooking", fragManager!!)
                /*  UtilityClass.switchToFragment(
                      MymaGuideLinesFragment(),
                      "mymaGuidelines",
                      R.id.fragment,
                      fragManager!!, true
                  )*/
            }
            R.id.btnSubmitService -> {
                //Check at least one service Selected From the Buttons
                // Load The relative fragment according to th
            }
        }
    }


}
