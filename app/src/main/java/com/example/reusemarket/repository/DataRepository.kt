package com.example.reusemarket.repository

import com.example.reusemarket.model.AllItem
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

/**
 * Repository interface for handling operations related to item data.
 */
interface DataRepository {

    /**
     * Asynchronously adds data to an item represented by [allItem].
     *
     * @param allItem The [AllItem] object containing the data to be added.
     * @return A [Result] representing the outcome of the operation.
     */
    suspend fun addDataToItemData(allItem: AllItem): Result<Unit>

    /**
     * Asynchronously updates the data of an item represented by [allItem].
     *
     * @param allItem The [AllItem] object containing the updated data.
     * @return A [Result] representing the outcome of the operation.
     */
    suspend fun updateItemData(allItem: AllItem): Result<Unit>

    /**
     * Retrieves all items from the data source.
     *
     * @return A [Task] containing the query result in the form of a [QuerySnapshot].
     */
    fun fetchAllItems(): Task<QuerySnapshot>

    /**
     * Retrieves all items associated with the specified [email] from the data source.
     *
     * @param email The email for which items are to be fetched.
     * @return A [Task] containing the query result in the form of a [QuerySnapshot].
     */
    fun fetchAllItemsForEmail(email: String): Task<QuerySnapshot>

    /**
     * Asynchronously deletes an item represented by [allItem].
     *
     * @param allItem The [AllItem] object to be deleted.
     */
    suspend fun deleteItem(allItem: AllItem)

    /**
     * Searches for items in the data source based on a search query [searchItem].
     *
     * @param searchItem The search query.
     * @return A [Task] containing the query result in the form of a [QuerySnapshot].
     */
    fun searchItems(searchItem: String): Task<QuerySnapshot>

}