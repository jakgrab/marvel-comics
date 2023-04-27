package com.example.core.repository.firebase_repository

import com.google.firebase.auth.FirebaseAuth

interface FirebaseRepository {
    suspend fun signUpNewUser(
        email: String,
        password: String,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )
    suspend fun signInUser(
        email: String,
        password: String,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    )

    suspend fun createNewDocument()

    fun getUserOrCreateNew()
}
