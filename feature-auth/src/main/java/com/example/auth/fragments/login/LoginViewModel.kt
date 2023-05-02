package com.example.auth.fragments.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.repository.firebase_repository.FirebaseRepository
import com.example.core.repository.firebase_repository.FirebaseRepositoryImpl
import com.example.core.sign_in.SignInResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: FirebaseRepository,
    private val auth: FirebaseAuth,
) : ViewModel() {

    val password: MutableStateFlow<String> = MutableStateFlow("")
    val email: MutableStateFlow<String> = MutableStateFlow("")

    val isSignInWithGoogleSuccessful = MutableStateFlow(false)

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


    fun onSignInResult(result: SignInResult) {
        isSignInWithGoogleSuccessful.value = result.data != null
    }

    fun resetSignInWithGoogleState() {
        isSignInWithGoogleSuccessful.value = false
    }

    private fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

