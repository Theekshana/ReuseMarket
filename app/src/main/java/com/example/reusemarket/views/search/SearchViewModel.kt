package com.example.reusemarket.views.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: DataRepositoryImpl,

    ) : ViewModel() {

    private val _dataListState = MutableLiveData<UIState>()
    val marketItemList = MutableLiveData<List<AllItem>>()
    val dateListState: LiveData<UIState>
        get() = _dataListState


    fun searchItems(searchItem: String) {
        viewModelScope.launch {
            _dataListState.postValue(UIState.Loading)
            marketItemList.postValue(emptyList())

            if (searchItem.isEmpty()) {
                _dataListState.postValue(UIState.Success<List<AllItem>>(emptyList()))
                return@launch
            }

            val searchItems = searchItem.lowercase(Locale.ROOT) // Convert search text to lowercase

            repository.searchItems(searchItems).addOnSuccessListener {
                val itemList = ArrayList<AllItem>()

                for (data in it.documents) {
                    val item: AllItem? = data.toObject(AllItem::class.java)
                    if (item != null) {
                        item.itemId = data.id
                        val itemName =
                            item.name?.lowercase(Locale.ROOT) // Convert item name to lowercase
                        // Check if the lowercase item name contains the lowercase search text
                        if (itemName != null && itemName.contains(searchItems)) {
                            itemList.add(item)
                        }
                    }
                }

                marketItemList.postValue(itemList)
                _dataListState.postValue(UIState.Success<List<AllItem>>(itemList))
            }.addOnFailureListener {
                _dataListState.postValue(UIState.Failure("Failed getting data"))
            }
        }
    }

}