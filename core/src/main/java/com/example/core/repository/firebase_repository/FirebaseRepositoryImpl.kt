package com.example.core.repository.firebase_repository

import android.util.Log
import com.example.core.data.firestore_data.ComicsData
import com.example.core.data.firestore_data.UserComicsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseRepositoryImpl : FirebaseRepository {

    private val COLLECTION_NAME = "MARVEL_COMICS"
    private val COMICS_LIST_FIELD = "comicsList"

    override suspend fun signUpNewUser(
        email: String,
        password: String,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception.toString())
                }
            }
    }

    override suspend fun signInUser(
        email: String,
        password: String,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception.toString())
                }
            }
    }


    override fun getUsersFavouriteComics(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ): Flow<UserComicsData?> =
        callbackFlow {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "-1"

            FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .document(userId)
                .get()
                .addOnSuccessListener {
                    trySend(it.toObject(UserComicsData::class.java))
                    onSuccess()

                }.addOnFailureListener {
                    trySend(null)
                    onFailure()
                }
            awaitClose { }
        }

    override fun addUsersFirstFavouriteComic(comicsData: ComicsData): Flow<Boolean> = callbackFlow {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "-1"

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .document(userId)
            .set(UserComicsData(userId, listOf(comicsData)))
            .addOnSuccessListener {
                trySend(true)
            }
            .addOnFailureListener {
                trySend(false)
            }
        awaitClose { }

    }


    override fun updateFavouriteComics(comicsDataList: List<ComicsData>): Flow<Boolean> =
        callbackFlow {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "-1"

            FirebaseFirestore.getInstance()
                .collection(COLLECTION_NAME)
                .document(userId)
                .update(COMICS_LIST_FIELD, comicsDataList)
                .addOnSuccessListener {
                    Log.d("Firestore", "Successfully updated user's data")
                    trySend(true)
                }
                .addOnFailureListener {
                    Log.d("Firestore", "Failure during updating user's data")
                    trySend(false)
                }
            awaitClose { }

        }


    override fun deleteUsersFavouriteComics(): Flow<Boolean> = callbackFlow {
        val toDelete = hashMapOf<String, Any>(
            COMICS_LIST_FIELD to FieldValue.delete()
        )
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "-1"

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .document(userId)
            .update(toDelete)
            .addOnSuccessListener { trySend(true) }
            .addOnFailureListener { trySend(false) }

        awaitClose { }

    }
}