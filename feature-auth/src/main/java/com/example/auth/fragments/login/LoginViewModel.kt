package com.example.auth.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.firebase_repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: FirebaseRepositoryImpl,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val password: MutableStateFlow<String> = MutableStateFlow("")
    private val email: MutableStateFlow<String> = MutableStateFlow("")

    fun setUserEmail(inputEmail: String) {
        email.value = inputEmail
    }

    fun setUserPassword(inputPassword: String) {
        password.value = inputPassword
    }

    fun signInUser(
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            loginRepository.signInUser(
                email.value,
                password.value,
                auth,
                onSuccess = onSuccess,
                onError = {
                 onError(it)
                }
            )
        }
    }
}

