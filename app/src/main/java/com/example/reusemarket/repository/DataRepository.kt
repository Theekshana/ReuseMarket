package com.example.reusemarket.repository

import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.Data
import com.google.android.gms.tasks.Task

interface DataRepository {

    suspend fun addDataToItemData(data: Data): Result<UIState>


}