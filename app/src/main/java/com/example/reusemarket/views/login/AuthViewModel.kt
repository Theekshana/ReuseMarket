package com.example.reusemarket.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
) : ViewModel() {

    //current sign-up status
    private val _signUp = MutableLiveData<String>(null)
    val signUp: LiveData<String>
        get() = _signUp

    /**
     * Initiates the sign-up process with the provided email and password.
     *
     * @param email The email address used for sign-up.
     * @param password The password associated with the account being created.
     */
    fun signUpInfo(email: String, password: String) {
        viewModelScope.launch {
            val result = repository.signUp(email, password)
            //_signUp.value = result.toString()

        }

    }


}