package com.example.core.repository.firebase_repository

import com.google.firebase.auth.FirebaseAuth

class FirebaseRepositoryImpl : FirebaseRepository {

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

}