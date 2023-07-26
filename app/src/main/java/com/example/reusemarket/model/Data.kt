package com.example.reusemarket.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    val itemImage: Uri?,
    val name: String ,
    val type: String
): Parcelable