package com.example.reusemarket.views.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.model.Data
import com.example.reusemarket.repository.DataRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddItemViewModel @Inject constructor(
    private val repository: DataRepositoryImpl,
) : ViewModel() {


    fun addItemToFirestore(data: Data) {
        //val category = selectedCategory.value ?: ""
        viewModelScope.launch {

            repository.addDataToItemData(data)


        }
    }


}