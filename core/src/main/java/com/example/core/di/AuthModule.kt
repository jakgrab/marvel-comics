package com.example.core.di

import android.content.Context
import com.example.core.repository.firebase_repository.FirebaseRepositoryImpl
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseRepository(): FirebaseRepositoryImpl = FirebaseRepositoryImpl()

    @Singleton
    @Provides
    fun provideOneTapClient(@ApplicationContext applicationContext: Context): SignInClient =
        Identity.getSignInClient(applicationContext)
}