package com.uj.myapplications.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.uj.myapplications.R
import com.uj.myapplications.adapters.InboxMessageAdpater
import com.uj.myapplications.fragments.TabsFragment.MyOrderPendingFragment
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_inbox.*
import kotlinx.android.synthetic.main.fragment_my_orders.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class InboxFragment : Fragment(), View.OnClickListener {

    lateinit var fragment: Fragment
    val message: ArrayList<String> = ArrayList()
    val date: ArrayList<String> = ArrayList()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Load Default
//        fragment = MyOrderPendingFragment()
//        UtilityClass.switchToFragment(
//                fragment,
//                MyOrderPendingFragment::class.java.name,
//                R.id.frameFragment,
//                activity!!.supportFragmentManager, false
//        )
        btnTabNotification.setOnClickListener(this)
        btnTabMessage.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnTabNotification -> {
//                llBottomView.visibility = View.VISIBLE
                btnTabNotification.setBackgroundColor(resources.getColor(R.color.green))
                btnTabMessage.setBackgroundColor(resources.getColor(R.color.colorLightGrey))
                btnTabNotification.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabMessage.setTextColor(resources.getColor(R.color.colorDarkGrey))
//                fragment = MyOrderPendingFragment()
//                UtilityClass.switchToFragment(
//                        fragment,
//                        MyOrderPendingFragment::class.java.name,
//                        R.id.frameFragment,
//                        activity!!.supportFragmentManager, false
//                )

            }
            R.id.btnTabMessage -> {
//                llBottomView.visibility = View.GONE
                btnTabMessage.setBackgroundColor(resources.getColor(R.color.green))
                btnTabMessage.setTextColor(resources.getColor(R.color.colorWhite))
                btnTabNotification.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTabNotification.setBackgroundColor(resources.getColor(R.color.colorLightGrey))


                addMessages()
                addDate()
                rvInbox.layoutManager = LinearLayoutManager(this.context)
                rvInbox.adapter = InboxMessageAdpater(message ,date , this.requireContext())

            }
        }
    }

    fun addMessages() {
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
        message.add("Lorem ipsum dolor sit amet, consectetur adipiscing")
    }
    fun addDate() {
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
        date.add("05/02/2019 3.27 pm")
    }
}




