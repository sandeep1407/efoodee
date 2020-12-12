package com.uj.myapplications.utility

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * Created by Vineet on 3/9/2018.
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}