package com.example.reusemarket.constants

import com.example.reusemarket.R
import java.lang.Exception

sealed class UIState {

    //loading, success, failure
    object Loading: UIState()
    data class Success<out R>(val result: R) : UIState()
    data class Failure(val error: String): UIState()

}