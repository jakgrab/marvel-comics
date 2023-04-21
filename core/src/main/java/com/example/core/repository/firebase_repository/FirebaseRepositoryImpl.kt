package com.example.core.repository.firebase_repository

import android.util.Log
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
                Log.d("Firebase", "Signed up successfully")
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
                Log.d("Firebase", "Signed in successfully")
                if(task.isSuccessful) {
                    onSuccess()
                } else {
                    onError(task.exception.toString())
                }
            }
    }

}