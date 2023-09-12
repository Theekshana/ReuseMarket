package com.example.reusemarket.repository

import android.net.Uri
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.model.Data
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface DataRepository {

    suspend fun addDataToItemData(data: Data): Result<Unit>

    fun fetchAllItems() : Task<QuerySnapshot>




}