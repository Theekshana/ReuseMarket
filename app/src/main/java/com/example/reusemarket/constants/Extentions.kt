package com.example.reusemarket.constants

import android.view.View
import com.example.reusemarket.model.Data


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
