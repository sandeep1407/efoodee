package com.uj.myapplications.utility

import android.content.Context
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import com.uj.myapplications.R

/**
 * Created by Umesh Jangid on 2/26/2018.
 */
fun String.isTrimmedEmpty(): Boolean {
    return TextUtils.isEmpty(this.trim())
}

fun String.trimEquals(other: String): Boolean {
    return this.trim() == other.trim()
}

fun String.showAlert(context: Context, action: () -> Unit) {
    AlertDialog.Builder(context, R.style.AlertDialogCustom)
            .setTitle(context.getString(R.string.alert))
            .setCancelable(false)
            .setIcon(R.mipmap.ic_launcher)
            .setMessage(this).setPositiveButton(context.getString(R.string.ok)) { dialog, which ->
                dialog.dismiss()
                action()
            }.show()


}