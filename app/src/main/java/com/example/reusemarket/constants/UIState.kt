package com.example.reusemarket.constants

import com.example.reusemarket.R
import java.lang.Exception

sealed class UIState<out R> {

    //loading, success, failure
    object Loading: UIState<Nothing>()
    data class Success<out R>(val result: R) : UIState<R>()
    data class Failure(val exception: Exception): UIState<Nothing>()

}