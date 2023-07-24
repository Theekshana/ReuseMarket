package com.example.reusemarket.views.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.model.Data
import com.example.reusemarket.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: DataRepositoryImpl
    ): ViewModel() {

    private val _addItemResult = MutableLiveData<String>()
    val addItemResult: LiveData<String>
        get() = _addItemResult

    fun addItemToFirestore(data: Data){
        viewModelScope.launch {

            repository.addDataToItemData(data)


        }
    }
}