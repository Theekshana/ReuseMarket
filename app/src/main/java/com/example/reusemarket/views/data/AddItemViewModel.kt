package com.example.reusemarket.views.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.model.Data
import com.example.reusemarket.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: DataRepositoryImpl,
) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private val _toastMessage = MutableLiveData<String?>()
    val toastMessage: LiveData<String?>
        get() = _toastMessage

    fun addItemToFirestore(allItem: AllItem) {
        _loadingState.value = true
        viewModelScope.launch {

            val response = repository.addDataToItemData(allItem)
            _loadingState.value = false

            if(response.isSuccess){
                _toastMessage.value = "Data added successfully!"

            }else{

                _toastMessage.value = "Error adding data!"

            }


        }
    }

    fun clearToastMessage() {
        _toastMessage.value = null
    }


}