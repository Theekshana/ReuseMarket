package com.example.reusemarket.repository

import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.Data
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
     firestore: FirebaseFirestore
    ): DataRepository{

    private val itemData = firestore.collection("items")
   // val document = database.collection(FirestoreTables.NOTE).document()
    override suspend fun addDataToItemData(data: Data): Result<UIState> {
        return withContext(Dispatchers.IO){
            runCatching {
                itemData.add(data).await()


            }.fold(
                onSuccess = { Result.success(UIState.Success("Data Added")) },
                onFailure = { Result.failure(it) }
            )
        }
    }


}