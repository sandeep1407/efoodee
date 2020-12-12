package com.uj.myapplications.fragments


import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.uj.myapplications.R
import com.uj.myapplications.utility.*
import kotlinx.android.synthetic.main.fragment_only_delivery.*
import kotlinx.android.synthetic.main.fragment_schedule_menu.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.security.KeyManagementException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.cert.CertificateException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ScheduleMenuFragment : Fragment(), View.OnClickListener {
    var fragment: Fragment? = null
    var myList: MutableList<Int> = mutableListOf<Int>()
    var flagDinner: Boolean = false
    var Dinner: Int = 1 // Deafult value
    var flagLunch: Boolean = false
    var ADDUPDATEFLAG = ""
    var menuId = ""
    var closingTime = ""
    var mealServingTime = ""
    var Lunch: Int = 1 // Deafult value
    var flagPublishOrSchedule: Int = 0//0= publiesh and 1 for schedule Deafult value
    var meal_type: Int = 0//"0" = Lunch, 1 = Dinner, 2 = Breakfast
    private var photos = ArrayList<String>()
    var jsonObject: JSONObject = JSONObject() // Deafult value
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_schedule_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Load Default
        myList.add(R.id.btnMonday)
        btnSchedule.setOnClickListener(this)
        btnTime.setOnClickListener(this)
        btnPublishNow.setOnClickListener(this)
        btnMonday.setOnClickListener(this)
        btnTuesday.setOnClickListener(this)
        btnWednesday.setOnClickListener(this)
        btnThursday.setOnClickListener(this)
        btnFriday.setOnClickListener(this)
        btnSaturday.setOnClickListener(this)
        btnSunday.setOnClickListener(this)
        btnLunch.setOnClickListener(this)
        btnDinner.setOnClickListener(this)
        btnMealServeTime.setOnClickListener(this)
        btnBack.setOnClickListener(this)
        setForwardDates()
        var bundle = arguments
        if (bundle != null) {
            var args = bundle.getString("jsonObj")
            var argsFrom = bundle.getString(RestTags.FROM)
            if (argsFrom != null) {
                when (argsFrom) {
                    "Publish" -> {
                        //Hide the UI Of that 4 Days
                        //  llDays.visibility = View.GONE
                        onPublish()
                        setLunchDinnerTimings()
                        flagPublishOrSchedule = 0
                    }
                    "Schedule" -> {
                        //  llDays.visibility = View.VISIBLE
                        onSchedule()
                        flagPublishOrSchedule = 1
                    }
                }
            }
            if (args != null) {
                jsonObject = JSONObject(args)
            } else {

            }
            var argsList = bundle.getSerializable("menuPhotosList") as ArrayList<String>
            if (!argsList.isNullOrEmpty()) {
                photos = argsList
            }
            ADDUPDATEFLAG = AddMenuFragment.caller
            if (ADDUPDATEFLAG.equals("EDIT")) {
                menuId = arguments!!.getString("menuID")
                if (jsonObject != null) {
                    btnTime.text = jsonObject.optString("closing_time")
                    btnMealServeTime.text = jsonObject.optString("scheduled_to")
                    meal_type = jsonObject.optInt("meal_type")
                    if (meal_type == 0) {
                        //Make Lunch Enable
                        btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnLunch.setTextColor(resources.getColor(R.color.white))
                    } else {
                        btnDinner.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnDinner.setTextColor(resources.getColor(R.color.white))

                    }
                }
            } else {
                //Case Add
            }
            //Now if Json Object which we recive from menu detail api set that value
            // to UI and
            //  intialize on UI
            // In case of edit Please set it to previous values


        }
    }

    override fun onClick(v: View) {
        when (v!!.id) {
            R.id.btnBack -> {
                //getTime(activity!!, btnMealServeTime, 2)

            }
            R.id.btnMealServeTime -> {
                //Serve Time (Schedule Timing)
                getTime(activity!!, btnMealServeTime, 2)
            }
            R.id.btnTime -> {
                //Order Closing Timing
                getTime(activity!!, btnTime, 1)
            }
            R.id.btnSchedule -> {
                // onPublish()
                // Hit The APi After Validation For Scheduling Menu
                if (ADDUPDATEFLAG == "EDIT") {
                    //Case Edit
                    if (UtilityClass.isInternetAvailable(activity!!)) {
                        jsonObject.put("closing_time", closingTime)
                        jsonObject.put("scheduled_to", mealServingTime)
                        Log.e("Schedule Menu :Edit", jsonObject.toString())
                        getMenuResponse(jsonObject, menuId, 2)
                    } else {
                        UtilityClass.showToast(activity!!, "Please check Internet Connection.")
                    }
                } else {
                    //Case Add
                    if (mealServingTime.trim().length <= 0) {
                        UtilityClass.showToast(
                            activity!!,
                            "Meal serving time should be equal or greater than close time.a"
                        )
                    } else {
                        if (UtilityClass.isInternetAvailable(activity!!)) {
                            jsonObject.put("closing_time", closingTime)
                            jsonObject.put("scheduled_to", mealServingTime)
                            Log.e("Schedule Menu :", jsonObject.toString())
                            getMenuResponse(jsonObject, menuId, 1)
                        } else {
                            UtilityClass.showToast(activity!!, "Please check Internet Connection.")
                        }
                    }
                }
            }
            R.id.btnPublishNow -> {
                if (ADDUPDATEFLAG == "EDIT") {
                    //Case Edit
                    if (UtilityClass.isInternetAvailable(activity!!)) {
                        Log.e("Publish Menu :Edit", jsonObject.toString())
                        jsonObject.put("closing_time", closingTime)
                        jsonObject.put("scheduled_to", mealServingTime)
                        getMenuResponse(jsonObject, menuId, 2)
                    } else {
                        UtilityClass.showToast(activity!!, "Please check Internet Connection.")
                    }
                } else {
                    //Case Add
                    if (UtilityClass.isInternetAvailable(activity!!)) {
                        jsonObject.put("closing_time", closingTime)
                        jsonObject.put("scheduled_to", mealServingTime)
                        Log.e("Publish Menu", jsonObject.toString())
                        getMenuResponse(jsonObject, menuId, 1)
                    } else {
                        UtilityClass.showToast(activity!!, "Please check Internet Connection.")
                    }
                }

            }
            R.id.btnMonday -> {
                //Enable Button Monday
                btnMonday.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnMonday.setTextColor(resources.getColor(R.color.colorWhite))
                btnTuesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnTuesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnWednesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnWednesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnThursday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnThursday.setTextColor(resources.getColor(R.color.colorDarkGrey))
            }
            R.id.btnTuesday -> {
                btnTuesday.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnTuesday.setTextColor(resources.getColor(R.color.colorWhite))
                btnMonday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMonday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnWednesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnWednesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnThursday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnThursday.setTextColor(resources.getColor(R.color.colorDarkGrey))
            }
            R.id.btnWednesday -> {
                btnWednesday.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnWednesday.setTextColor(resources.getColor(R.color.colorWhite))
                btnMonday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMonday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTuesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnTuesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnThursday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnThursday.setTextColor(resources.getColor(R.color.colorDarkGrey))
            }
            R.id.btnThursday -> {
                btnThursday.setBackgroundResource(R.drawable.bg_round_fill_green)
                btnThursday.setTextColor(resources.getColor(R.color.colorWhite))
                btnMonday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnMonday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnTuesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnTuesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                btnWednesday.setBackgroundResource(R.drawable.bg_round_border_grey)
                btnWednesday.setTextColor(resources.getColor(R.color.colorDarkGrey))
            }


            R.id.btnFriday -> {
                if (myList.size == 1 && myList.contains(R.id.btnFriday))
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note2)) }
                else if (myList.size < 4 || myList.contains(R.id.btnFriday)) {
                    if (myList.contains(R.id.btnFriday)) {
                        myList.remove(R.id.btnFriday)
                        btnFriday.setBackgroundResource(R.drawable.bg_round_border_grey)
                        btnFriday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                    } else {
                        myList.add(R.id.btnFriday)
                        btnFriday.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnFriday.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                } else
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note)) }
                Log.e("Array List Size ", "" + myList.size)

            }
            R.id.btnSaturday -> {
                if (myList.size == 1 && myList.contains(R.id.btnSaturday))
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note2)) }
                else if (myList.size < 4 || myList.contains(R.id.btnSaturday)) {
                    if (myList.contains(R.id.btnSaturday)) {
                        myList.remove(R.id.btnSaturday)
                        btnSaturday.setBackgroundResource(R.drawable.bg_round_border_grey)
                        btnSaturday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                    } else {
                        myList.add(R.id.btnSaturday)
                        btnSaturday.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnSaturday.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                } else
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note)) }
                Log.e("Array List Size ", "" + myList.size)

            }
            R.id.btnSunday -> {
                if (myList.size == 1 && myList.contains(R.id.btnSunday))
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note2)) }
                else if (myList.size < 4 || myList.contains(R.id.btnSunday)) {
                    if (myList.contains(R.id.btnSunday)) {
                        myList.remove(R.id.btnSunday)
                        btnSunday.setBackgroundResource(R.drawable.bg_round_border_grey)
                        btnSunday.setTextColor(resources.getColor(R.color.colorDarkGrey))
                    } else {
                        myList.add(R.id.btnSunday)
                        btnSunday.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnSunday.setTextColor(resources.getColor(R.color.colorWhite))
                    }
                } else
                    context?.let { UtilityClass.showToast(it, resources.getString(R.string.selector_note)) }
                Log.e("Array List Size ", "" + myList.size)

            }

            R.id.btnLunch -> {
                if (flagPublishOrSchedule == 0) {
                    //Case Publish
                    if (flagLunch) {
                        btnLunch.setBackgroundResource(R.drawable.bg_round_border_grey)
                        btnLunch.setTextColor(resources.getColor(R.color.gray))
                        flagLunch = false
                        Lunch = 0
                    } else {
                        btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnLunch.setTextColor(resources.getColor(R.color.white))
                        flagLunch = true
                        Lunch = 1
                    }

                } else {
                    //Case Schedule
                    btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnLunch.setTextColor(resources.getColor(R.color.white))
                    btnDinner.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnDinner.setTextColor(resources.getColor(R.color.gray))
                    meal_type = 0 // Lunch
                }


                /* if (flagLunch) {
                     btnLunch.setBackgroundResource(R.drawable.bg_round_border_grey)
                     btnLunch.setTextColor(resources.getColor(R.color.gray))
                     flagLunch = false
                     Lunch = 0
                 } else {
                     btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
                     btnLunch.setTextColor(resources.getColor(R.color.white))
                     flagLunch = true
                     Lunch = 1
                 }*/
            }

            R.id.btnDinner -> {
                if (flagPublishOrSchedule == 0) {
                    //Case Publish
                    if (flagDinner) {
                        btnDinner.setBackgroundResource(R.drawable.bg_round_border_grey)
                        btnDinner.setTextColor(resources.getColor(R.color.gray))
                        flagDinner = false
                        Dinner = 0
                    } else {
                        btnDinner.setBackgroundResource(R.drawable.bg_round_fill_green)
                        btnDinner.setTextColor(resources.getColor(R.color.white))
                        flagDinner = true
                        Dinner = 1
                    }
                } else {
                    //Case Schedule
                    btnDinner.setBackgroundResource(R.drawable.bg_round_fill_green)
                    btnDinner.setTextColor(resources.getColor(R.color.white))
                    btnLunch.setBackgroundResource(R.drawable.bg_round_border_grey)
                    btnLunch.setTextColor(resources.getColor(R.color.gray))
                    meal_type = 1 // Dinner
                }
            }
        }
    }

    fun onPublish() {
        tvScheduleMenuOn.text = resources.getString(R.string.publish_menu_for)
        llDays.visibility = View.GONE
        btnSchedule.visibility = View.GONE
        btnPublishNow.visibility = View.VISIBLE
    }

    fun onSchedule() {
        tvScheduleMenuOn.text = resources.getString(R.string.schedule_menu_on)
        llDays.visibility = View.VISIBLE
        btnSchedule.visibility = View.VISIBLE
        btnPublishNow.visibility = View.GONE
    }

    private fun getTime(context: Context, btn: Button, flag: Int) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val todayAsString = dateFormat.format(cal.time)
            if (flag == 1) {
                closingTime = todayAsString
            } else {
                /*  val dateFormat = SimpleDateFormat("HH:mm")
                  dateFormat.format(cal.time)*/
                if (cal.time.after(dateFormat.parse(closingTime))) {
                    Log.e("CurrentTime", "Greater")
                    mealServingTime = todayAsString
                } else {
                    UtilityClass.showToast(
                        activity!!,
                        "Meal serving time should be equal or greater than close time.a"
                    )
                    Log.e("CurrentTime", "less")
                    mealServingTime = ""
                    //System.out.println("Current time is less than 12.07");
                }
            }
            if (hour > 12) {
                btn.text = (Integer.parseInt(hour.toString()) - 12).toString() + ":" + minute + " PM"
            } else if (hour == 12) {
                btn.text = "12" + ":" + minute + " PM"
            } else {
                btn.text = SimpleDateFormat("HH:mm").format(cal.time) + " AM"
            }
        }
        TimePickerDialog(
            context,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()
    }

    private var message: String? = ""
    /**
     * @param flag =1 Add
     * @param flag =2 Update
     * */
    @SuppressLint("StaticFieldLeak")
    fun getMenuResponse(jsonObject: JSONObject, menuID: String, flag: Int) {
        object : AsyncTask<String, Void, JSONObject>() {
            override fun onPreExecute() {
                super.onPreExecute()
                UtilityClass.showDialog(activity!!)
            }

            override fun doInBackground(vararg params: String): JSONObject? {
                try {

                    val res =
                        OkhttpRequestUtils.menuResponse(
                            flag,
                            Config.geToken(),
                            photos,
                            jsonObject.toString(), menuID
                        )
                    if (res != null) {
                        /*val stringHashMap = java.util.HashMap<String, String>()
                        stringHashMap["code"] = res!!.code().toString()
                        stringHashMap["response"] = res!!.body()!!.string()*/
                        return res
                    } else {
                        return null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(res: JSONObject?) {
                super.onPostExecute(res)
                UtilityClass.hideDialog()
                if (res != null) {
                    message = res.optString("msg")
                    if (res.optBoolean("success") && res.optInt("statusCode") == 200) {
                        Log.e("Menu", "success")
                        fragment = AddMenuSuccessFragment()
                        UtilityClass.switchToFragment(
                            fragment,
                            AddMenuSuccessFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }
                    UtilityClass.showToast(
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                } else {
                    UtilityClass.showToast(
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }

    /* @SuppressLint("StaticFieldLeak")
     fun getMenuResponse(jsonObject: JSONObject) {
         object : AsyncTask<String, Void, JSONObject>() {
             override fun onPreExecute() {
                 super.onPreExecute()
                 UtilityClass.showDialog(activity!!)
             }

             override fun doInBackground(vararg params: String): JSONObject? {
                 try {
                     val res =
                         OkhttpRequestUtils.menuResponse(
                             Config.geToken(),
                             photos,
                             jsonObject.toString()
                         )
                     if (res != null) {
                         *//*val stringHashMap = java.util.HashMap<String, String>()
                        stringHashMap["code"] = res!!.code().toString()
                        stringHashMap["response"] = res!!.body()!!.string()*//*
                        return res
                    } else {
                        return null
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                } catch (e: CertificateException) {
                    e.printStackTrace()
                } catch (e: NoSuchAlgorithmException) {
                    e.printStackTrace()
                } catch (e: KeyStoreException) {
                    e.printStackTrace()
                } catch (e: KeyManagementException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(res: JSONObject?) {
                super.onPostExecute(res)
                UtilityClass.hideDialog()
                if (res != null) {
                    message = res.optString("msg")
                    if (res.optBoolean("success") && res.optInt("statusCode") == 200) {
                        Log.e("MenuAdd", "success")
                        fragment = AddMenuSuccessFragment()
                        UtilityClass.switchToFragment(
                            fragment,
                            AddMenuSuccessFragment::class.java.name,
                            R.id.fragment_container,
                            activity!!.supportFragmentManager, true
                        )
                    }
                    UtilityClass.showToast(
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                } else {
                    UtilityClass.showToast(
                        activity!!,
                        if (message.isNullOrEmpty()) Constants.OOPS_STR else message!!
                    )
                    UtilityClass.hideDialog()
                }
            }
        }.execute()
    }*/

    fun setLunchDinnerTimings() {
        val Date = Date()
        val Cal = GregorianCalendar()
        Cal.time = Date
        val hr = Cal.get(GregorianCalendar.HOUR_OF_DAY)
        if (hr < 11 && hr > 9) {
            // Set Dinner and Lunch Enable
            Log.e("Date", "Dinner and Lunch Enable")
            btnLunch.isEnabled = true
            btnDinner.isEnabled = true
            //Default
            btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnLunch.setTextColor(resources.getColor(R.color.white))
        } else if (hr > 11 && hr < 16) {
            //OnLy Show Lunch Enabled and Dinner is disable
            Log.e("Date", "Lunch Enabled and Dinner is disable")
            btnLunch.isEnabled = false
            btnDinner.isEnabled = false
            btnLunch.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnLunch.setTextColor(resources.getColor(R.color.white))
            btnDinner.setBackgroundResource(R.drawable.bg_round_fill_grey)
            btnDinner.setTextColor(resources.getColor(R.color.colorGrey))
        } else if (hr > 16 && hr < 24) {
            //OnLy Show Dinner Enabled and Lunch is disable
            Log.e("Date", "Dinner Enabled and Lunch is disable")
            btnLunch.isEnabled = false
            btnDinner.isEnabled = false
            btnDinner.setBackgroundResource(R.drawable.bg_round_fill_green)
            btnDinner.setTextColor(resources.getColor(R.color.white))
            btnLunch.setBackgroundResource(R.drawable.bg_round_fill_grey)
            btnLunch.setTextColor(resources.getColor(R.color.colorGrey))

        } else {
            // Set Dinner and Lunch Disable
            Log.e("Date", "Dinner and Lunch Enable")
            btnLunch.isEnabled = false
            btnDinner.isEnabled = false
            btnDinner.setBackgroundResource(R.drawable.bg_round_fill_grey)
            btnDinner.setTextColor(resources.getColor(R.color.colorGrey))
            btnLunch.setBackgroundResource(R.drawable.bg_round_fill_grey)
            btnLunch.setTextColor(resources.getColor(R.color.colorGrey))
        }
    }

    fun setForwardDates() {
        for (i in 1..4) {
            println(i)
            val gc = GregorianCalendar()
            // Add Date
            gc.add(Calendar.DATE, i)
            val dateFormat = SimpleDateFormat("dd-MMM-yyyy")
            val todayAsString = dateFormat.format(gc.time)
            gc.get(GregorianCalendar.DAY_OF_MONTH)
            val outFormat = SimpleDateFormat("EEEE")
            val goal = outFormat.format(gc.time)
            //val day = array[gc.get(Calendar.DAY_OF_WEEK)]
            Log.e("Day and Date", "Day is $goal and $todayAsString")
            when (i) {
                1 -> {
                    txt_day1.text = goal
                    val arr = todayAsString.split("-")
                    btnMonday.text = arr[0]
                }
                2 -> {
                    txt_day2.text = goal
                    val arr = todayAsString.split("-")
                    btnTuesday.text = arr[0]
                }

                3 -> {
                    txt_day3.text = goal
                    val arr = todayAsString.split("-")
                    btnWednesday.text = arr[0]
                }

                4 -> {
                    txt_day4.text = goal
                    val arr = todayAsString.split("-")
                    btnThursday.text = arr[0]
                }

            }
        }
    }
}
