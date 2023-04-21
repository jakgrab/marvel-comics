package com.example.auth.fragments.registration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class RegistrationViewModel : ViewModel() {

    private val password: MutableStateFlow<String> = MutableStateFlow("")
    private val repeatedPassword: MutableStateFlow<String> = MutableStateFlow("")
    private val email: MutableStateFlow<String> = MutableStateFlow("")

    fun setUserEmail(inputEmail: String) {
        email.value = inputEmail
    }

    fun setUserRepeatedPassword(inputRepeatedPassword: String) {
        repeatedPassword.value = inputRepeatedPassword
    }

    fun setUserPassword(inputPassword: String) {
        password.value = inputPassword
    }


}

