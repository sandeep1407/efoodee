package com.uj.myapplications.fragments


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

class MyEarningFragment : Fragment(), View.OnClickListener {
    lateinit var fragment: Fragment


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_earnings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnTabCurrentMonth.setOnClickListener(this)
        btnTabLifetime.setOnClickListener(this)
        tvTotalEarning.text = "Total " + activity!!.resources.getString(R.string.RS) + " 0"
        // Load Default
        loadDefaultFrag()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTabCurrentMonth -> {
                btnTabLifetime.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabCurrentMonth.setBackgroundColor(resources.getColor(R.color.green))
                btnTabLifetime.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabCurrentMonth.setTextColor(resources.getColor(R.color.colorWhite))
                loadDefaultFrag()
            }
            R.id.btnTabLifetime -> {
                btnTabLifetime.setBackgroundColor(resources.getColor(R.color.green))
                btnTabCurrentMonth.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabLifetime.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabCurrentMonth.setTextColor(resources.getColor(R.color.colorDarkGrey))
                loadLifeTimeFrag()
            }
        }
    }

    fun loadDefaultFrag() {
        fragment = CurrentMonthFragment()
        UtilityClass.switchToFragment(
            fragment,
            CurrentMonthFragment::class.java.name,
            R.id.frameEarningFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadLifeTimeFrag() {
        fragment = LifetimeEarningsFragment()
        UtilityClass.switchToFragment(
            fragment,
            LifetimeEarningsFragment::class.java.name,
            R.id.frameEarningFragment,
            activity!!.supportFragmentManager, false
        )
    }


}
