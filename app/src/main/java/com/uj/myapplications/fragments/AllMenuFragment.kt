package com.uj.myapplications.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.fragments.TabsFragment.AllMenuCurrentFragment
import com.uj.myapplications.fragments.TabsFragment.AllMenuRecentFragment
import com.uj.myapplications.fragments.TabsFragment.AllMenuScheduledFragment
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_edit_menu.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 *
 * A simple [Fragment] subclass.
 *
 */
class AllMenuFragment : Fragment(), View.OnClickListener {

    lateinit var fragment: Fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_menu, container, false)
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
        fragment = AllMenuCurrentFragment()
        UtilityClass.switchToFragment(
            fragment,
            AllMenuCurrentFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadRecentFrag() {
        fragment = AllMenuRecentFragment()
        UtilityClass.switchToFragment(
            fragment,
            AllMenuRecentFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }

    fun loadScheduledFrag() {
        fragment = AllMenuScheduledFragment()
        UtilityClass.switchToFragment(
            fragment,
            AllMenuScheduledFragment::class.java.name,
            R.id.frameFragment,
            activity!!.supportFragmentManager, false
        )
    }


}
