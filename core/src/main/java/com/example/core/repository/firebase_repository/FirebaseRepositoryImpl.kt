package com.example.core.repository.firebase_repository

import android.util.Log
import com.example.core.data.firestore_data.ComicsData
import com.example.core.data.firestore_data.UserComicsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepositoryImpl : FirebaseRepository {

    private val COLLECTION_NAME = "MARVEL_COMICS"

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


    override fun getUsersFavouriteComics(userId: String): Boolean {
        var result = false
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener {
                Log.d("FireStore", "Successfully got users data")
                result = true
            }.addOnFailureListener {
                Log.d("FireStore", "Successfully got users data")
                result = false
            }
        return result
    }

    override fun addOrUpdateFavouriteComics(comicsDataList: List<ComicsData>): Boolean {
        var result = false
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .add(
                UserComicsData(
                    userId = FirebaseAuth.getInstance().currentUser?.uid ?: "-1",
                    comicsList = comicsDataList
                )
            )
            .addOnSuccessListener { result = true }
            .addOnFailureListener { result = false }
        return result
    }

    override fun deleteUsersFavouriteComics(): Boolean {
        var result = false
        val toDelete = hashMapOf<String, Any>(
            "comicsList" to FieldValue.delete()
        )
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_NAME)
            .document(FirebaseAuth.getInstance().currentUser?.uid ?: "-1")
            .update(toDelete)
            .addOnSuccessListener { result = true }
            .addOnFailureListener { result = false }
        return result
    }
}