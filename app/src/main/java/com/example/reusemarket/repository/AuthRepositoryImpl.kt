package com.example.reusemarket.repository

import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import javax.inject.Inject

/**
 * Implementation of the [AuthRepository] interface for handling authentication operations.
 *
 * @param firebaseAuth An instance of [FirebaseAuth] used for authentication operations.
 */
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {
    override suspend fun signIn(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password)

    }

    override suspend fun signUpWithGoogle(account: SignInCredential): Task<AuthResult> {

        val credential = GoogleAuthProvider.getCredential(account.googleIdToken, null)
        return firebaseAuth.signInWithCredential(credential)

    }

    override fun getCurrentUser() = firebaseAuth.currentUser
    override fun signOut() {
        firebaseAuth.signOut()
    }


}