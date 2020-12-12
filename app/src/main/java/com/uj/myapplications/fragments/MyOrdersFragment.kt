package com.uj.myapplications.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.uj.myapplications.R
import com.uj.myapplications.fragments.TabsFragment.EditCurrentMenuFragment
import com.uj.myapplications.fragments.TabsFragment.MyOrderCompletedFragment
import com.uj.myapplications.fragments.TabsFragment.MyOrderMissedFragment
import com.uj.myapplications.fragments.TabsFragment.MyOrderPendingFragment
import com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments.Sub_All_Fragment
import com.uj.myapplications.utility.RestTags
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_edit_menu.*
import kotlinx.android.synthetic.main.fragment_my_orders.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyOrdersFragment : Fragment(), View.OnClickListener {
    companion object {
        var ORDERPROGRESSSTATUS = 2 // Default Value
    }

    lateinit var fragment: Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Load Default
        fragment = MyOrderPendingFragment()
        UtilityClass.switchToFragment(
            fragment,
            MyOrderPendingFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
        btnTabMissed.setOnClickListener(this)
        btnTabCompleted.setOnClickListener(this)
        btnTabPending.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        //1= Completed ,2= Pending and 3 = Missed Fragment
        when (v.id) {
            R.id.btnTabPending -> {
                //  Clear All List Before Going Next Frgament
                Sub_All_Fragment.listAll.clear()
                Sub_All_Fragment.listSelfDelivery.clear()
                Sub_All_Fragment.listSelfPickup.clear()
                Sub_All_Fragment.listMymaDelivery.clear()
                ORDERPROGRESSSTATUS = 2
                btnTabMissed.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabCompleted.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabPending.setBackgroundColor(resources.getColor(R.color.green))
                btnTabMissed.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabPending.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabCompleted.setTextColor(resources.getColor(R.color.colorDarkGrey))
                fragment = MyOrderPendingFragment()
                val bundle = Bundle()
                bundle.putString(RestTags.FROM, "2")
                fragment.arguments = bundle
                UtilityClass.switchToFragment(
                    fragment,
                    MyOrderPendingFragment::class.java.name,
                    R.id.frameFragment,
                    activity!!.supportFragmentManager, false
                )

            }
            R.id.btnTabMissed -> {
                //  Clear All List Before Going Next Frgament
                Sub_All_Fragment.listAll.clear()
                Sub_All_Fragment.listSelfDelivery.clear()
                Sub_All_Fragment.listSelfPickup.clear()
                Sub_All_Fragment.listMymaDelivery.clear()
                ORDERPROGRESSSTATUS = 3
                btnTabMissed.setBackgroundColor(resources.getColor(R.color.green))
                btnTabCompleted.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabPending.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabMissed.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabPending.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabCompleted.setTextColor(resources.getColor(R.color.colorDarkGrey))
                /* fragment = EditRecentMenuFragment()
                 UtilityClass.switchToFragment(
                     fragment,
                     EditRecentMenuFragment::class.java.name,
                     R.id.frameFragment,
                     activity!!.supportFragmentManager, false
                 )*/
                fragment = MyOrderMissedFragment()
                val bundle = Bundle()
                bundle.putString(RestTags.FROM, "3")
                fragment.arguments = bundle
                UtilityClass.switchToFragment(
                    fragment,
                    MyOrderMissedFragment::class.java.name,
                    R.id.frameFragment,
                    activity!!.supportFragmentManager, false
                )
            }
            R.id.btnTabCompleted -> {
                //  Clear All List Before Going Next Frgament
                Sub_All_Fragment.listAll.clear()
                Sub_All_Fragment.listSelfDelivery.clear()
                Sub_All_Fragment.listSelfPickup.clear()
                Sub_All_Fragment.listMymaDelivery.clear()
                ORDERPROGRESSSTATUS = 1
                btnTabMissed.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabCompleted.setBackgroundColor(resources.getColor(R.color.green))
                btnTabPending.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabMissed.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabPending.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabCompleted.setTextColor(resources.getColor(R.color.colorWhite))
                /*  fragment = EditScheduledMenuFragment()
                  UtilityClass.switchToFragment(
                      fragment,
                      EditScheduledMenuFragment::class.java.name,
                      R.id.frameFragment,
                      activity!!.supportFragmentManager, false
                  )*/
                fragment = MyOrderCompletedFragment()
                val bundle = Bundle()
                bundle.putString(RestTags.FROM, "1")
                fragment.arguments = bundle
                UtilityClass.switchToFragment(
                    fragment,
                    MyOrderCompletedFragment::class.java.name,
                    R.id.frameFragment,
                    activity!!.supportFragmentManager, false
                )
            }
        }
    }


}
