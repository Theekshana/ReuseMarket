package com.example.reusemarket.repository

import com.example.reusemarket.model.AllItem
import com.example.reusemarket.model.Data
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,

    ) : DataRepository {

    private val itemData = firestore.collection("items")
    private val imageRef = firebaseStorage.reference.child("images")
    override suspend fun addDataToItemData(allItem: AllItem): Result<Unit> {
        val imageUri = allItem.itemImage
        val name = allItem.name
        val category = allItem.category


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
                "category" to category,

            )
            itemData.add(furnitureItem).await()
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


    /*override suspend fun fetchDataFromDatabase(data: Data): Result<List<AllItem>> {
        return try {
            val querySnapshot = firestore.collection("items").get().await()
            val dataList = querySnapshot.documents.mapNotNull { document ->
                document.toObject(AllItem::class.java)
            }
            Result.success(dataList)
        }catch (e: Exception){
            Result.failure(e)
        }
    }*/
}











