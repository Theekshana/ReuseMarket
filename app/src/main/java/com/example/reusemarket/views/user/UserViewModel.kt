package com.example.reusemarket.views.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.repository.AuthRepositoryImpl
import com.example.reusemarket.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing user-related data and actions.
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: DataRepositoryImpl,
    private val repositoryAuth: AuthRepositoryImpl,
) : ViewModel() {

    private val _dataListState = MutableLiveData<UIState>()
    val marketItemList = MutableLiveData<List<AllItem>>()
    private val _dataDeleteState = MutableLiveData<UIState>()
    val dateListState: LiveData<UIState>
        get() = _dataListState

    fun fetchPostedItems() {
        viewModelScope.launch {
            _dataListState.postValue(UIState.Loading)
            repositoryAuth.getCurrentUser()?.email?.let { email ->
                repository.fetchAllItemsForEmail(email).addOnSuccessListener { it ->
                    val userList = ArrayList<AllItem>()
                    for (data in it.documents) {
                        val item: AllItem? = data.toObject(AllItem::class.java)
                        if (item != null) {
                            item.itemId = data.id
                        }
                        item?.let { userList.add(it) }
                    }
                    marketItemList.postValue(userList)
                    _dataListState.postValue(UIState.Success<List<AllItem>>(userList))

                }.addOnFailureListener {
                    _dataListState.postValue(UIState.Failure("Failed getting data"))
                }
            }

        }


    }

    fun deleteItem(allItem: AllItem) {

        viewModelScope.launch {
            _dataListState.postValue(UIState.Loading)
            try {
                repository.deleteItem(allItem)
                (marketItemList.value as ArrayList).remove(allItem)
                _dataListState.postValue(UIState.Success<List<AllItem>>(marketItemList.value as ArrayList<AllItem>))
                _dataDeleteState.postValue(UIState.Success<String?>(null))


            }catch (ex: Exception){
                _dataDeleteState.postValue(UIState.Failure("Failed delete item"))
            }
        }

    }

    fun logout() {
        repositoryAuth.signOut()
    }


}