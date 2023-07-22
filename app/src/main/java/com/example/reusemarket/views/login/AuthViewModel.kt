package com.example.reusemarket.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.repository.AuthRepositoryImpl
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {

    //current sign-up status
    private val _signUp = MutableLiveData<UIState>()
    val signUp: LiveData<UIState>
        get() = _signUp

    /**
     * Initiates the sign-up process with the provided email and password.
     *
     * @param email The email address used for sign-up.
     * @param password The password associated with the account being created.
     */
    fun signUpResponse(email: String, password: String) {
        viewModelScope.launch {
            // Set the initial loading state
            _signUp.postValue(UIState.Loading)
            // Perform the login operation
            repository.signUp(email, password).addOnSuccessListener {
                _signUp.postValue(UIState.Success(it.user))
            }.addOnFailureListener {
                // Login failed
                _signUp.postValue(UIState.Failure("Login failed"))
            }

        }
    }

    fun signInWithGoogle(account: SignInCredential) {
        viewModelScope.launch {
            repository.signUpWithGoogle(account).addOnSuccessListener {
                _signUp.postValue(UIState.Success(it.user))

            }.addOnFailureListener {
                _signUp.postValue(UIState.Failure("Login Failed"))

            }
        }
    }

    fun isAlreadyLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }



}

