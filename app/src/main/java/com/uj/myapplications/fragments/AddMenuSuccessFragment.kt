package com.uj.myapplications.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.uj.myapplications.R
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.fragment_add_menu.*
import kotlinx.android.synthetic.main.menu_added_sucessfully.*


// TODO: Rename parameter arguments, choose names that match
/**
 * A simple [Fragment] subclass.
 *
 */
class AddMenuSuccessFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.menu_added_sucessfully, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnBackToHome.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            if (v.id == R.id.btnBackToHome) {
                activity!!.onBackPressed()
            }
        }
    }
}
