package com.example.auth.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.auth.R
import com.example.auth.databinding.FragmentLoginBinding
import com.example.auth.utils.navigateToActivity
import com.example.core.constants.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tietEmail.doOnTextChanged { text, _, _, _ ->
            viewModel.setUserEmail(inputEmail = text.toString())
        }

        binding.tietPassword.doOnTextChanged { text, _, _, _ ->
            viewModel.setUserPassword(inputPassword = text.toString())
        }

        binding.btSignIn.setOnClickListener {
            viewModel.signInUser(
                onSuccess = {
                    Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}