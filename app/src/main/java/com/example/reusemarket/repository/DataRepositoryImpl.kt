package com.example.reusemarket.repository

import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.Data
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
    ): DataRepository{

    private val itemData = firestore.collection("items")
    override suspend fun addData(data: Data): Result<UIState> {
        return withContext(Dispatchers.IO){
            try {
                itemData.add(data).await()
                Result.success(UIState.Success("Data Added"))

            }catch (e: Exception){
                Result.failure(e)

            }
        }
    }
    

}