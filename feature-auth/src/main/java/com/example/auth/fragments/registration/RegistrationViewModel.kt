package com.example.auth.fragments.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.firebase_repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val loginRepository: FirebaseRepositoryImpl,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val password: MutableStateFlow<String> = MutableStateFlow("")
    private val repeatedPassword: MutableStateFlow<String> = MutableStateFlow("")
    private val email: MutableStateFlow<String> = MutableStateFlow("")

    var authenticated = mutableStateOf(false)
        private set

    fun setUserEmail(inputEmail: String) {
        email.value = inputEmail
    }

    fun setUserRepeatedPassword(inputRepeatedPassword: String) {
        repeatedPassword.value = inputRepeatedPassword
    }

    fun setUserPassword(inputPassword: String) {
        password.value = inputPassword
    }

    fun signUpNewUser(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.signUpNewUser(
                email = email.value,
                password = password.value,
                auth = auth,
                onSuccess = onSuccess,
                onError = {
                    onError(it)
                }
            )
        }
    }

    fun validateUserData(): Boolean {
        return validateEmail() && validatePassword() && doPasswordsMatch()
    }

    private fun validateEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()
    }

    private fun validatePassword(): Boolean {
        return password.value.isNotEmpty() && password.value.count() >= 6
    }

    private fun doPasswordsMatch(): Boolean {
        return password.value == repeatedPassword.value
    }
}

