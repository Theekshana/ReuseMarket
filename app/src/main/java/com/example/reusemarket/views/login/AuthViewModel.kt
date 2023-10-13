package com.example.reusemarket.views.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.repository.AuthRepositoryImpl
import com.google.android.gms.auth.api.identity.SignInCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepositoryImpl,
) : ViewModel() {

    //current sign-up status
    private val _signIn = MutableLiveData<UIState>()
    val signIn: LiveData<UIState>
        get() = _signIn

    /**
     * Initiates the sign-up process with the provided email and password.
     *
     * @param email The email address used for sign-up.
     * @param password The password associated with the account being created.
     */
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            // Set the initial loading state
            _signIn.postValue(UIState.Loading)
            // Perform the login operation
            repository.signIn(email, password).addOnSuccessListener {
                _signIn.postValue(UIState.Success(it.user))
            }.addOnFailureListener {
                // Login failed
                _signIn.postValue(UIState.Failure("Login failed"))
            }

        }
    }

    fun signInWithGoogle(account: SignInCredential) {
        viewModelScope.launch {
            repository.signUpWithGoogle(account).addOnSuccessListener {
                _signIn.postValue(UIState.Success(it.user))

            }.addOnFailureListener {
                _signIn.postValue(UIState.Failure("Login Failed"))

            }
        }
    }

    fun isAlreadyLoggedIn(): Boolean {
        return repository.getCurrentUser() != null
    }


}

