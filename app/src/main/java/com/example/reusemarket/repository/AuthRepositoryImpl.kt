package com.example.reusemarket.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun signUp(
        email: String,
        password: String,
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }


}