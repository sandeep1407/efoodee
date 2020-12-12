package com.uj.myapplications.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.uj.myapplications.R
import com.uj.myapplications.fragments.TabsFragment.EditCurrentMenuFragment
import com.uj.myapplications.fragments.TabsFragment.EditRecentMenuFragment
import com.uj.myapplications.fragments.TabsFragment.EditScheduledMenuFragment
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_edit_menu.*


/**
 * A simple [Fragment] subclass.
 *
 */
class EditMenuFragment : Fragment(), View.OnClickListener {
    lateinit var fragment: Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Load Default
        loadDefaultFrag()
        btnTabCurrent.setOnClickListener(this)
        btnTabRecent.setOnClickListener(this)
        btnTabScheduled.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTabCurrent -> {
                btnTabRecent.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabScheduled.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabCurrent.setBackgroundColor(resources.getColor(R.color.green))

                btnTabRecent.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabCurrent.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabScheduled.setTextColor(resources.getColor(R.color.colorDarkGrey))
                loadDefaultFrag()

            }
            R.id.btnTabRecent -> {
                btnTabRecent.setBackgroundColor(resources.getColor(R.color.green))
                btnTabScheduled.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabCurrent.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabRecent.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabCurrent.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabScheduled.setTextColor(resources.getColor(R.color.colorDarkGrey))
                loadRecentFrag()

            }
            R.id.btnTabScheduled -> {
                btnTabRecent.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabScheduled.setBackgroundColor(resources.getColor(R.color.green))
                btnTabCurrent.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabRecent.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabCurrent.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabScheduled.setTextColor(resources.getColor(R.color.colorWhite))
                loadScheduledFrag()

            }
        }
    }


    fun loadDefaultFrag() {
        fragment = EditCurrentMenuFragment()
        UtilityClass.switchToFragment(
            fragment,
            EditCurrentMenuFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadRecentFrag() {
        fragment = EditRecentMenuFragment()
        UtilityClass.switchToFragment(
            fragment,
            EditRecentMenuFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadScheduledFrag() {
        fragment = EditScheduledMenuFragment()
        UtilityClass.switchToFragment(
            fragment,
            EditScheduledMenuFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }

}
