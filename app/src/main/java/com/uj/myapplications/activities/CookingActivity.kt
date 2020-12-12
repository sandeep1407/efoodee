package com.uj.myapplications.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.uj.myapplications.R
import kotlinx.android.synthetic.main.activity_cooking.*

class CookingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cooking)
        btnCookingSelfDelivery.setOnClickListener {
            scCookingDes.visibility = View.GONE
            rlRegForm.visibility = View.VISIBLE
            scFoodBusiness.visibility = View.GONE
        }
        btnNext.setOnClickListener {
            scCookingDes.visibility = View.GONE
            rlRegForm.visibility = View.GONE
            scFoodBusiness.visibility = View.VISIBLE
        }

    }
}
