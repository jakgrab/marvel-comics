package com.example.auth.fragments.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.auth.R
import com.example.auth.databinding.FragmentRegistrationBinding
import com.example.auth.utils.navigateToActivity
import com.example.feature_main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.etEmail.doOnTextChanged { text, _, _, _ ->
            registrationViewModel.setUserEmail(inputEmail = text.toString())
        }

        binding.etPassword.doOnTextChanged { text, _, _, _ ->
            registrationViewModel.setUserPassword(inputPassword = text.toString())
        }

        binding.etRepeatedPassword.doOnTextChanged { text, _, _, _ ->
            registrationViewModel.setUserRepeatedPassword(inputRepeatedPassword = text.toString())
        }

        binding.btCreateAccount.setOnClickListener {
            registrationViewModel.signUpNewUser(
                onSuccess = {
                    Toast.makeText(this.context, "Account created", Toast.LENGTH_SHORT).show()
                    context?.navigateToActivity(MainActivity::class.java)
                },
                onError = {
                    Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
                }
            )
        }

        binding.btGoBack.setOnClickListener {
            findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}