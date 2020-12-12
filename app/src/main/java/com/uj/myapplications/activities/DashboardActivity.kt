package com.uj.myapplications.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.uj.myapplications.R
import com.uj.myapplications.fragments.MymaGuideLinesFragment
import com.uj.myapplications.fragments.ServicesFragment
import com.uj.myapplications.pojo.UserCommunicationPreferences
import com.uj.myapplications.utility.Config
import com.uj.myapplications.utility.RestTags
import com.uj.myapplications.utility.UtilityClass
import kotlinx.android.synthetic.main.layout_communication_pref.*

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_communication_pref)
        var fragManager: FragmentManager? = null
        fragManager = supportFragmentManager
        // Intialize UI
        intilizeUIWithPreviousConfig()
        val normalLinkClickSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@DashboardActivity, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "TANDC")
                startActivity(intent)
            }
        }
        val normalLinkClickSpan1 = object : ClickableSpan() {
            override fun onClick(view: View) {
                // Toast.makeText(applicationContext, "Normal Link", Toast.LENGTH_SHORT).show()
                var intent = Intent(this@DashboardActivity, FullScreenDialogActivity::class.java)
                intent.putExtra(RestTags.FROM, "PP")
                startActivity(intent)
            }
        }


        iBtn_CommuncationBack.setOnClickListener {
            //Back
            onBackPressed()
        }
        makeLinks(
            txt_tandc,
            arrayOf("Terms and Conditions", "Privacy Policy"),
            arrayOf(normalLinkClickSpan, normalLinkClickSpan1)
        )

        btnNext.setOnClickListener {
            /* if (!chk_email.isChecked || !chk_phone.isChecked || !chk_push_notification.isChecked || !chk_sms.isChecked) {
                 UtilityClass.showToast(this, "Please select alteast 1 preference")
             } else {*/
            //Show Myma GuideLine Fragment
            //Save Data Into Temp user data PReferences
            //val userData = Config.getUserData()
            val preferences = UserCommunicationPreferences()
            preferences.email = chk_email.isChecked
            preferences.phone = chk_phone.isChecked
            preferences.sms = chk_sms.isChecked
            preferences.push = chk_push_notification.isChecked
            ServicesFragment.userTempObj.userCommunication = preferences
            // userData!!.userCommunication = preferences
            // Config.setUserData(userData)
            UtilityClass.switchToFragment(
                MymaGuideLinesFragment(),
                MymaGuideLinesFragment::class.java.name,
                R.id.fragment_container,
                fragManager!!, false
            )


            /*  var intent = Intent(this@DashboardActivity, NavigationActivity::class.java)
              intent.flags =
                      Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
              startActivity(intent)
              finish()*/
        }

    }

    fun makeLinks(textView: TextView, links: Array<String>, clickableSpans: Array<ClickableSpan>) {
        var spannableString = SpannableString(textView.text)
        links.forEachIndexed { index, element ->
            var clickableSpan = clickableSpans[index]
            var link = links[index]
            var startIndexOfLink = textView.text.toString().indexOf(link)
            spannableString.setSpan(
                clickableSpan, startIndexOfLink,
                startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.highlightColor = Color.TRANSPARENT// prevent TextView change background when highlight
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)

    }

    private fun intilizeUIWithPreviousConfig() {
        val userData = Config.getUserData()
        val preferences = UserCommunicationPreferences()
        if (preferences != null) {
            chk_email.isChecked = preferences?.email
            chk_phone.isChecked = preferences?.phone
            chk_push_notification.isChecked = preferences?.push
            chk_sms.isChecked = preferences?.sms
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
