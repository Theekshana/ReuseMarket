package com.example.reusemarket.repository

import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser


interface AuthRepository {
    suspend fun signUp(email: String, password: String): Task<AuthResult>

    suspend fun signUpWithGoogle(account: SignInCredential): Task<AuthResult>

    fun getCurrentUser(): FirebaseUser?

    fun signOut()

    fun deleteUser()


}

