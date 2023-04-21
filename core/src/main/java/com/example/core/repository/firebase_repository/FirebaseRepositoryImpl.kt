package com.example.core.repository.firebase_repository

import com.google.firebase.auth.FirebaseAuth

class FirebaseRepositoryImpl : FirebaseRepository {

    override fun signUpNewUser(email: String, password: String, auth: FirebaseAuth): Boolean {
        var result = false
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result = task.isSuccessful
            }
        return result
    }

    override fun signInUser(email: String, password: String, auth: FirebaseAuth): Boolean {
        var result = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                result = task.isSuccessful
            }
        return result
    }

}