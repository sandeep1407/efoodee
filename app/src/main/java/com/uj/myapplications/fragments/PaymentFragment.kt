package com.uj.myapplications.fragments


import android.opengl.Visibility
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.uj.myapplications.R
import com.uj.myapplications.fragments.TabsFragment.*
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_my_earnings.*
import kotlinx.android.synthetic.main.fragment_my_orders.*
import kotlinx.android.synthetic.main.fragment_payment_detail.*

class PaymentFragment : Fragment(), View.OnClickListener {
    lateinit var fragment: Fragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_payment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Load Default
        loadDefaultFrag()
        btnTabPaymentPending.setOnClickListener(this)
        btnTabPaymentLifetime.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTabPaymentPending -> {
                rlPaymentInfo.visibility = View.VISIBLE
                tvLifetimeAmount.visibility = View.GONE
                btnTabPaymentLifetime.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabPaymentPending.setBackgroundColor(resources.getColor(R.color.green))
                btnTabPaymentLifetime.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabPaymentPending.setTextColor(resources.getColor(R.color.colorWhite))
                loadDefaultFrag()
//
            }
            R.id.btnTabPaymentLifetime -> {
                rlPaymentInfo.visibility = View.GONE
                tvLifetimeAmount.visibility = View.VISIBLE
                btnTabPaymentLifetime.setBackgroundColor(resources.getColor(R.color.green))
                btnTabPaymentPending.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabPaymentLifetime.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabPaymentPending.setTextColor(resources.getColor(R.color.colorDarkGrey))
                loadRecentFrag()
//                fragment = EditCurrentMenuFragment()
//                UtilityClass.switchToFragment(
//                    fragment,
//                        EditCurrentMenuFragment::class.java.name,
//                    R.id.frameEarningFragment,
//                    activity!!.supportFragmentManager, false
//                )
            }
        }
    }


    fun loadDefaultFrag() {
        fragment = PendingPaymentFragment()
        UtilityClass.switchToFragment(
            fragment,
            PendingPaymentFragment::class.java.name,
            R.id.framePaymentFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadRecentFrag() {
        fragment = LifetimePaymentFragment()
        UtilityClass.switchToFragment(
            fragment,
            LifetimePaymentFragment::class.java.name,
            R.id.framePaymentFragment,
            activity!!.supportFragmentManager, false
        )
    }

}
