package com.example.reusemarket.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllItem(
    var itemId: String? = null,
    val image_url: String? = null,
    val itemImage: Uri? = null,
    val name: String? = null,
    val category: String? = null,
    var email: String? = null,
    var location: String? = null,
    var phoneNumber: String? = null,
    var description: String? = null,

): Parcelable
