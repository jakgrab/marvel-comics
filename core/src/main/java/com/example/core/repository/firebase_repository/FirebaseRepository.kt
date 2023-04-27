package com.example.core.repository.firebase_repository

import com.example.core.data.firestore_data.ComicsData
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

    fun getUsersFavouriteComics(userId: String): Boolean

    fun deleteUsersFavouriteComics(): Boolean

    fun addOrUpdateFavouriteComics(comicsDataList: List<ComicsData>): Boolean
}
