package com.uj.myapplications.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import com.uj.myapplications.R
import com.uj.myapplications.fragments.ServicesFragment
import com.uj.myapplications.utility.UtilityClass

class ServicesActivity : AppCompatActivity() {
    var fragManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        /* btnCooking.setOnClickListener {
             startActivity(Intent(this, CookingActivity::class.java))
             finish()
         }*/
        // Default Fragment To Load
        fragManager = this.supportFragmentManager
        UtilityClass.switchToFragment(ServicesFragment(), "services", R.id.fragment, fragManager!!, false)
    }

    // Load Fragment
    fun switchToFragment(fragment: Fragment?, frag_tag: String) {
        if (fragment != null) {
            val ft = this.supportFragmentManager.beginTransaction()
            ft.add(R.id.fragment, fragment, frag_tag)
            ft.addToBackStack(frag_tag)
            ft.commit()
        }
    }

    override fun onBackPressed() {
        /* if (supportFragmentManager.backStackEntryCount == 0 || supportFragmentManager.backStackEntryCount == 1) {
             super.onBackPressed()
             finish()
         } else {
             supportFragmentManager.popBackStack()
         }*/
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } /*else if (supportFragmentManager.backStackEntryCount == 0 || supportFragmentManager.backStackEntryCount == 1) {
            super.onBackPressed()
            finish()
        }*/ else {
            super.onBackPressed()
            finish()
        }

    }

}
