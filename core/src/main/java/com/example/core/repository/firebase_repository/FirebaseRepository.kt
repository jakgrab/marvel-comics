package com.example.core.repository.firebase_repository

import com.google.firebase.auth.FirebaseAuth

interface FirebaseRepository {
    fun signUpNewUser(email: String, password: String, auth: FirebaseAuth): Boolean
    fun signInUser(email: String, password: String, auth: FirebaseAuth): Boolean
}
