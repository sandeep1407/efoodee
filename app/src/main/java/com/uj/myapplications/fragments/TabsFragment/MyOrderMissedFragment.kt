package com.uj.myapplications.fragments.TabsFragment


import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.uj.myapplications.R
import com.uj.myapplications.adapters.MyOrdersTabAdapter
import kotlinx.android.synthetic.main.fragment_my_order_pending.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MyOrderMissedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //Same Layout using
        return inflater.inflate(R.layout.fragment_my_order_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewPager.adapter = MyOrdersTabAdapter(activity!!.supportFragmentManager)
        tabs.setupWithViewPager(viewPager)
        changeTabsFont()
    }

    private fun changeTabsFont() {
        val vg = tabs.getChildAt(0) as ViewGroup
        val fontTypeFace = Typeface.createFromAsset(
            context!!.assets,
            "Montserrat_SemiBold.ttf"
        )
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            val tabChildsCount = vgTab.childCount
            for (i in 0 until tabChildsCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    tabViewChild.setTextSize(TypedValue.COMPLEX_UNIT_SP,10f)
                    tabViewChild.typeface = fontTypeFace

                }
            }
        }
    }


}
