package com.uj.myapplications.fragments


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import com.google.gson.Gson
import com.uj.myapplications.R
import com.uj.myapplications.adapters.DashboardAdapter
import com.uj.myapplications.pojo.UserDataPojo
import com.uj.myapplications.utility.Config
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_my_account.*
import java.util.*
import android.provider.Contacts.People
import android.support.v4.app.FragmentManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Toast
import com.uj.myapplications.activities.NavigationActivity
import com.uj.myapplications.activities.ServicesActivity
import com.uj.myapplications.utility.AlertDialogCallback
import com.uj.myapplications.utility.NotificationUtil
import kotlinx.android.synthetic.main.nav_header_dummy_navigation.*
import java.lang.Exception


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 *
 */
class MyProfileFragment : Fragment() {
    var fragManager: FragmentManager? = null
    // var fragment: Fragment? = null
    var flagBankDetailGiven = false
    var flagFssaiDetailGiven = false
    var flagTermAccepeted = false
    var flagLocationGiven = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Get User from Pref
        var userDataPojo = Config.getUserData()
        fragManager = activity!!.supportFragmentManager
        try {
            if (userDataPojo != null) {
                val name = userDataPojo.name
                if (name != null) {
                    txt_welcome_user.text = "Hi ${name?.fname} Lets rock today"
                } else {
                    txt_welcome_user.text = "Hi User Lets rock today"
                }
                if (userDataPojo?.isTermAccepted) {
                    flagTermAccepeted = true
                }
                if (userDataPojo?.bankDetailsEntered) {
                    flagBankDetailGiven = true
                }
                if (userDataPojo?.fssai?.isRegistered!!) {
                    flagFssaiDetailGiven = true
                }
            } else {
                txt_welcome_user.text = "Hi User Lets rock today"
            }


        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // txt_welcome_user.text = "Hi User Lets rock today"
        }
        var menuArray = Arrays.asList(resources.getStringArray(R.array.menu_string))
        val listOfStrings = ArrayList<Any?>()
        listOfStrings.addAll(menuArray.get(0))
        grid_view.adapter = DashboardAdapter(activity!!.applicationContext, listOfStrings)
        /**
         * On Click event for Single Gridview Item
         * */
        grid_view.onItemClickListener = OnItemClickListener { parent, v, position, id ->
            //UtilityClass.showToast(activity!!.applicationContext, "" + (listOfStrings[position]))
           /* val spannable = SpannableString("Text styling")
            spannable.setSpan(
                ForegroundColorSpan(Color.RED),
                0, 4,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )*/
            var msg = "To add,update and see all menus one must first provide all mandatory details to be true: \n" +
                    "Bank Details Given : $flagBankDetailGiven ,\n" +
                    "Fssai Detail Given : $flagFssaiDetailGiven ,\n" +
                    "Myma terms Accepted : $flagTermAccepeted .\n "
            when (listOfStrings[position]) {
                "Bank Details" -> {
                    NavigationActivity.fragment = BankDetailsFragment()
                    UtilityClass.switchToFragment(
                        NavigationActivity.fragment,
                        LanguageChangeFragment::class.java.name,
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                }
                "Add Menu" -> {
                    val user = Config.getUserData()
                    if (user != null) {
                        if (user?.isTermAccepted && user?.bankDetailsEntered && user?.fssai?.isRegistered!!) {
                            if (user.serviceType == 1 || user.serviceType == 0) {
                                // User is Only Allowed to add Menu in Rest cases it is not allowed
                                NavigationActivity.fragment = AddMenuFragment()
                                UtilityClass.switchToFragment(
                                    NavigationActivity.fragment,
                                    AddMenuFragment::class.java.name,
                                    R.id.fragment_container,
                                    fragManager!!, true
                                )
                            } else {
                                UtilityClass.showToast(
                                    activity!!,
                                    "You are not allowed to adding menu.",
                                    true
                                )
                            }
                        } else {

                            NotificationUtil.askForInput(activity!!,
                                "Menu Alert!", msg,
                                //"Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                "Ok",
                                "cancel",
                                true,
                                {

                                })
                            /* UtilityClass.showToast(
                                 activity!!,
                                 "Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                 true
                             )*/

                        }
                    }

                }
                "Edit Menu" -> {
                    val user = Config.getUserData()
                    if (user != null) {
                        if (user?.isTermAccepted && user?.bankDetailsEntered && user?.fssai?.isRegistered!!) {
                            if (user.serviceType == 1 || user.serviceType == 0) {
                                // User is Only Allowed to add Menu in Rest cases it is not allowed
                                NavigationActivity.fragment = EditMenuFragment()
                                UtilityClass.switchToFragment(
                                    NavigationActivity.fragment,
                                    EditMenuFragment::class.java.name,
                                    R.id.fragment_container,
                                    fragManager!!, false
                                )
                            } else {
                                UtilityClass.showToast(
                                    activity!!,
                                    "You are not allowed to adding menu.",
                                    true
                                )
                            }
                        } else {
                            /*UtilityClass.showToast(
                                activity!!,
                                "Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                true
                            )*/
                            NotificationUtil.askForInput(activity!!,
                                "Menu Alert!", msg,
                                //"Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                "Ok",
                                "cancel",
                                true,
                                {

                                })

                        }
                    }
                }
                "All Menu" -> {
                    val user = Config.getUserData()
                    if (user != null) {
                        if (user?.isTermAccepted && user?.bankDetailsEntered && user?.fssai?.isRegistered!!) {
                            if (user.serviceType == 1 || user.serviceType == 0) {
                                // User is Only Allowed to add Menu in Rest cases it is not allowed
                                NavigationActivity.fragment = AllMenuFragment()
                                UtilityClass.switchToFragment(
                                    NavigationActivity.fragment,
                                    AllMenuFragment::class.java.name,
                                    R.id.fragment_container,
                                    fragManager!!, false
                                )
                            } else {
                                UtilityClass.showToast(
                                    activity!!,
                                    "You are not allowed to adding menu.",
                                    true
                                )
                            }
                        } else {
                            /* UtilityClass.showToast(
                                 activity!!,
                                 "Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                 true
                             )*/
                            NotificationUtil.askForInput(activity!!,
                                "Menu Alert!", msg,
                                //"Be sure to insert Bank, FSSAI details and Myma Terms to be accepted before adding menu.",
                                "Ok",
                                "cancel",
                                true,
                                {

                                })

                        }
                    }
                }
                "My Orders" -> {
                    NavigationActivity.fragment = MyOrdersFragment()
                    UtilityClass.switchToFragment(
                        NavigationActivity.fragment,
                        MyOrdersFragment::class.java.name,
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                }
                "My Earnings" -> {
                    NavigationActivity.fragment = MyEarningFragment()
                    UtilityClass.switchToFragment(
                        NavigationActivity.fragment,
                        MyEarningFragment::class.java.name,
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                }
                "Payments" -> {
                    NavigationActivity.fragment = PaymentFragment()
                    UtilityClass.switchToFragment(
                        NavigationActivity.fragment,
                        PaymentFragment::class.java.name,
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                }
                "Services Offered" -> {
                    //Save MenuDataPojo User model and then proceed further
                    val intent = Intent(activity, ServicesActivity::class.java)
                    /*  intent.flags =
                          Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                 */     startActivity(intent)

                    // activity!!.finish()
                }

                "Reviews" -> {
                    NavigationActivity.fragment = ReviewsFragment()
                    UtilityClass.switchToFragment(
                        NavigationActivity.fragment,
                        ReviewsFragment::class.java.name,
                        R.id.fragment_container,
                        fragManager!!, false
                    )
                }
            }
        }
    }


}
