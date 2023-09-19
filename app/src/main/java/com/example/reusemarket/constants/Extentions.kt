package com.example.reusemarket.constants

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog


object FirestoreTables{
val DATA = "data"
}

const val PREF_USER = "USER"
const val REQ_ONE_TAP = 1000

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun Context.showAlertYeNo(
    title: String, message: String, positiveClick: () -> Unit,
    negativeClick: (() -> Unit)? = null
) {
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton("Yes") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            positiveClick()
        }
        setNegativeButton("No") { dialogInterface: DialogInterface, _: Int ->
            dialogInterface.dismiss()
            negativeClick?.invoke()
        }
    }.show()
}

