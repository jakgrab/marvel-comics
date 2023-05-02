package com.example.core.repository.firebase_repository

import com.example.core.data.firestore_data.ComicsData
import com.example.core.data.firestore_data.UserComicsData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.Flow

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

    fun getUsersFavouriteComics(onSuccess: () -> Unit,  onFailure: () -> Unit): Flow<UserComicsData?>

    fun deleteUsersFavouriteComics(): Flow<Boolean>

    fun updateFavouriteComics(comicsDataList: List<ComicsData>): Flow<Boolean>

    fun addUsersFirstFavouriteComic(comicsData: ComicsData): Flow<Boolean>
}
