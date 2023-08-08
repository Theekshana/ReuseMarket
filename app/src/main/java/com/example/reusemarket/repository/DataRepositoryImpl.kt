package com.example.reusemarket.repository

import com.example.reusemarket.model.Data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    firebaseStorage: FirebaseStorage,

    ) : DataRepository {

    private val itemData = firestore.collection("items")
    private val imageRef = firebaseStorage.reference.child("images")
    override suspend fun addDataToItemData(data: Data): Result<Unit> {
        val imageUri = data.itemImage
        val name = data.name
        val category = data.category

        return try {
            if (imageUri == null) {
                throw IllegalArgumentException(("No image URI provided"))
            }
            val imageFileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
            val imageStorageRef = imageRef.child(imageFileName)


            val uploadTask = imageStorageRef.putFile(imageUri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()
            // Save the download URL and name in Firestore
            //furniture item
            val furnitureItem = hashMapOf(
                "image_url" to downloadUrl.toString(),
                "name" to name,
                "category" to category

            )
            itemData.add(furnitureItem).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}











