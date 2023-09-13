package com.example.reusemarket.repository

import com.example.reusemarket.model.AllItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface DataRepository {

    suspend fun addDataToItemData(allItem: AllItem): Result<Unit>

    suspend fun updateItemData(allItem: AllItem): Result<Unit>

    fun fetchAllItems() : Task<QuerySnapshot>

    fun fetchAllItemsForEmail(email : String): Task<QuerySnapshot>




}