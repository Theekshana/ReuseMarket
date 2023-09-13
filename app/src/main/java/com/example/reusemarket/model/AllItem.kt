package com.example.reusemarket.model

import android.net.Uri


data class AllItem(
    var itemId: String? = null,
    val image_url: String? = null,
    val itemImage: Uri? = null,
    val name: String? = null,
    val category: String? = null,
    var email: String? = null

)
