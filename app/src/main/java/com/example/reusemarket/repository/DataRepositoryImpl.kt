package com.example.reusemarket.repository

import android.net.Uri
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.Data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage,

    ) : DataRepository {

    private val itemData = firestore.collection("items")
    private val imageRef = firebaseStorage.reference.child("images")
    override suspend fun addDataToItemData(data: Data): Result<UIState> {
        val imageUri = data.itemImage
        val name = data.name
        val type = data.type

        if (imageUri != null) {
            val imageFileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
            val imageStorageRef = imageRef.child(imageFileName)

            try {
                val uploadTask = imageStorageRef.putFile(imageUri).await()
                val downloadUrl = uploadTask.storage.downloadUrl.await()
                // Save the download URL and name in Firestore
                val newItem = hashMapOf(
                    "image_url" to downloadUrl.toString(),
                    "name" to name,
                "type" to type
                )
                itemData.add(newItem).await()

                // Save the download URL in Firestore
                // Implement your Firestore saving logic here


            } catch (e: Exception) {

            }
        }


        // val document = database.collection(FirestoreTables.NOTE).document()
        /*override suspend fun addDataToItemData(data: Data): Result<UIState> {
        return withContext(Dispatchers.IO) {
            runCatching {

                val imageUri = data.itemImage
                val name = data.name
                val type = data.type
                //itemData.add(data).await()




            }.fold(
                onSuccess = { Result.success(UIState.Success("Data Added")) },
                onFailure = { Result.failure(it) }
            )
        }
    }*/
        return TODO("Provide the return value")
    }
}











