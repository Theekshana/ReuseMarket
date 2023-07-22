package com.example.reusemarket.views.home

import androidx.lifecycle.ViewModel
import com.example.reusemarket.repository.AuthRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {


    fun signOut() {
        //repository.deleteUser()
        repository.signOut()

    }

}