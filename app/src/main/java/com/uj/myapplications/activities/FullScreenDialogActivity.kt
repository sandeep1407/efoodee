package com.uj.myapplications.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.uj.myapplications.R
import com.uj.myapplications.utility.RestTags
import kotlinx.android.synthetic.main.fragment_term_and_conditions.*

class FullScreenDialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_term_and_conditions)
        ibtnClose.visibility = View.VISIBLE
        val intent = intent
        if(intent!=null){
            val str =intent.getStringExtra(RestTags.FROM)
            if(str.equals("TANDC")){
                txt_label.setText(getString(R.string.termandcondition))
            }else{
                txt_label.setText(getString(R.string.privacy_policy))
            }
        }
        ibtnClose.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
