package com.uj.myapplications.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments.SubMymaDeliveryFragment
import com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments.SubSelfPickupFragment
import com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments.Sub_All_Fragment
import com.uj.myapplications.fragments.TabsFragment.myorders_sub_fragments.Sub_SelfDelivery_Fragment
import com.uj.myapplications.utility.RestTags

class MyOrdersTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        if (position == 0) {
            fragment = Sub_All_Fragment(RestTags.DINE_IN)
        } else if (position == 1) {
            fragment = SubSelfPickupFragment(RestTags.SELF_PICKUP)
        } else if (position == 2) {
            fragment = Sub_SelfDelivery_Fragment(RestTags.SELF_DELIVERY)
        } else if (position == 3) {
            fragment = SubMymaDeliveryFragment(RestTags.MYMA_DELIVERY)
        }
        return fragment!!
    }

    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if (position == 0) {
            title = "All"
        } else if (position == 1) {
            title = "Self-Pickup"
        } else if (position == 2) {
            title = "Self-Delivery"
        } else if (position == 3) {
            title = "Myma Delivery"
        }
        return title
    }
}