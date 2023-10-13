package com.example.reusemarket.repository

import com.example.reusemarket.model.AllItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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
    override suspend fun addDataToItemData(allItem: AllItem): Result<Unit> {
          val imageUri = allItem.itemImage

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
            val usedItem = hashMapOf(
                "image_url" to downloadUrl.toString(),
                "name" to allItem.name,
                "category" to allItem.category,
                "email" to allItem.email,
                "location" to allItem.location,
                "phoneNumber" to allItem.phoneNumber,
                "description" to allItem.description,

                )
            itemData.add(usedItem).await()
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }

    }

    override suspend fun updateItemData(allItem: AllItem): Result<Unit> {
        val imageUri = allItem.itemImage

        return try {

            var downloadUrl = allItem.image_url.orEmpty()
            if (imageUri != null) {
                val imageFileName = "${System.currentTimeMillis()}_${UUID.randomUUID()}.jpg"
                val imageStorageRef = imageRef.child(imageFileName)

                val uploadTask = imageStorageRef.putFile(imageUri).await()
                downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            }


            val usedItem = mapOf(
                "imageUrl" to downloadUrl,
                "name" to allItem.name,
                "category" to allItem.category,
                "email" to allItem.email,
                "location" to allItem.location,
                "phoneNumber" to allItem.phoneNumber,
                "description" to allItem.description,

            )
            allItem.itemId?.let { itemData.document(it).update(usedItem).await() }
            Result.success(Unit)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun fetchAllItems(): Task<QuerySnapshot> {
        return itemData.get()
    }

    override fun fetchAllItemsForEmail(email: String): Task<QuerySnapshot> {
        return itemData.whereEqualTo("email", email).get()
    }

    override suspend fun deleteItem(allItem: AllItem) {

        allItem.itemId?.let {
            FirebaseFirestore.getInstance().collection("items").document(it).delete().await()
        }
    }

    override fun searchItems(searchItem: String): Task<QuerySnapshot> {
        return itemData.whereGreaterThanOrEqualTo("name", searchItem)
            .whereLessThanOrEqualTo("name", searchItem + "\uf8ff").get()
    }


}











