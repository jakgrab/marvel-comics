package com.example.core.repository.firebase_repository

import com.example.core.data.firestore_data.UserComicsData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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

    override suspend fun createNewDocument() {
        FirebaseFirestore.getInstance().apply {
            this.collection(COLLECTION_NAME)
                .document(DOCUMENT_NAME)
                .update("userComics", UserComicsData())

        }
    }

    override fun getUserOrCreateNew() {
        TODO("Not yet implemented")
    }
}