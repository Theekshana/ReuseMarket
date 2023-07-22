package com.example.reusemarket.repository

import com.example.reusemarket.constants.UIState
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.Token
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import javax.inject.Inject


interface AuthRepository {
    suspend fun signUp(email: String, password: String)
}