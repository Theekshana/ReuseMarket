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

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: DataRepositoryImpl,
    private val repositoryAuth: AuthRepositoryImpl,
) : ViewModel() {


    private val _dataListState = MutableLiveData<UIState>()
    val marketItemList = MutableLiveData<List<AllItem>>()
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

    fun fetchAllItems() {

        viewModelScope.launch {
            _dataListState.postValue(UIState.Loading)
            repository.fetchAllItems().addOnSuccessListener { it ->
                val userList = ArrayList<AllItem>()
                for (data in it.documents) {
                    val item: AllItem? = data.toObject(AllItem::class.java)
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