package com.example.reusemarket.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val itemImage: String,
    val name: String ,
    val type: String
): Parcelable