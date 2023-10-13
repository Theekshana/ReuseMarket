package com.example.reusemarket.constants

/**
 * Sealed class representing different states of a user interface operation.
 */
sealed class UIState {

    object Loading : UIState()
    data class Success<out R>(val result: R) : UIState()
    data class Failure(val error: String) : UIState()

}