package com.example.reusemarket.di

import com.example.reusemarket.repository.AuthRepository
import com.example.reusemarket.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    /**
     * Provides a singleton instance of [AuthRepository] using the provided [FirebaseAuth] dependency.
     *
     * @param auth The [FirebaseAuth] instance used by the [AuthRepository].
     * @return The singleton instance of [AuthRepository].
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
    ): AuthRepository {
        return AuthRepositoryImpl(auth)

    }
}