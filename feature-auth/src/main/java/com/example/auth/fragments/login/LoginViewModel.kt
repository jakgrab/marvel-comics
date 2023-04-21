package com.example.auth.fragments.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel : ViewModel() {

    private val password: MutableStateFlow<String> = MutableStateFlow("")
    private val email: MutableStateFlow<String> = MutableStateFlow("")

    fun setUserEmail(inputEmail: String) {
        email.value = inputEmail
    }
    fun setUserPassword(inputPassword: String) {
        password.value = inputPassword
    }

    fun createNewUser(email: String,password: String, auth: FirebaseAuth) {

    }
}

