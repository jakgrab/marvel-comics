package com.example.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.auth.BR
import com.example.auth.R
import com.example.auth.databinding.FragmentLoginBinding
import com.example.auth.utils.navigateToActivity
import com.example.core.constants.Constants
import com.example.core.sign_in.GoogleAuthUiClient
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    @Inject
    lateinit var oneTapClient: SignInClient

    private lateinit var googleLogInLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.setVariable(BR.loginViewModel, loginViewModel)

        googleAuthUiClient = GoogleAuthUiClient(oneTapClient)

        googleLogInLauncher = getGoogleSignInLauncher(
            googleAuthUiClient = googleAuthUiClient,
            onSuccess = { user ->
                requireContext().navigateToActivity(Constants.MAIN_ACTIVITY_PATH)
            },
            onError = {
            }
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tietEmail.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setUserEmail(inputEmail = text.toString())
        }

        binding.tietPassword.doOnTextChanged { text, _, _, _ ->
            loginViewModel.setUserPassword(inputPassword = text.toString())
        }

        binding.btGoogleSignIn.setOnClickListener {
            signInWithGoogle(googleLogInLauncher, googleAuthUiClient)
        }

        binding.btSignIn.setOnClickListener {
            loginViewModel.signInUser(
                onSuccess = {
                    Toast.makeText(
                        requireContext(),
                        "Logged in",
                        Toast.LENGTH_SHORT
                    ).show()

                    requireContext().navigateToActivity(Constants.MAIN_ACTIVITY_PATH)
                },
                onError = {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.btCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegistrationFragment)
        }
    }

    private fun signInWithGoogle(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        googleAuthUiClient: GoogleAuthUiClient
    ) {
        lifecycleScope.launch(Dispatchers.Main) {
            val signInIntent = googleAuthUiClient.signIn()

            launcher.launch(
                IntentSenderRequest.Builder(
                    signInIntent ?: return@launch
                ).build()
            )
        }
    }

    private fun getGoogleSignInLauncher(
        googleAuthUiClient: GoogleAuthUiClient,
        onSuccess: (FirebaseUser?) -> Unit,
        onError: () -> Unit
    ): ActivityResultLauncher<IntentSenderRequest> {

        return registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { activityResult ->
            if (activityResult.resultCode == AppCompatActivity.RESULT_OK) {
                lifecycleScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = activityResult.data ?: return@launch,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                    loginViewModel.onSignInResult(signInResult)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}