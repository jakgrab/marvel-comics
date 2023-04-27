package com.example.core.repository.firebase_repository

import android.util.Log
import com.example.core.data.firestore_data.UserComicsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryImpl : FirebaseRepository {

    private val COLLECTION_NAME = "MARVEL_COMICS"
    private val DOCUMENT_NAME = "userComics"

    override suspend fun signUpNewUser(
        email: String,
        password: String,
        auth: FirebaseAuth,
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
    ){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful) {
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
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception.toString())
                }
            }
    }


    override fun getUsersFavouriteComics(userId: String) {
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener {
                Log.d("FireStore", "Successfully got users data")
            }.addOnFailureListener {
                Log.d("FireStore", "Successfully got users data")
            }

    }

    override fun addOrUpdateFavouriteComics() {
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .add(UserComicsData(

            ))
    }

    override fun deleteUsersFavouriteComics() {
        TODO("Not yet implemented")
    }
}