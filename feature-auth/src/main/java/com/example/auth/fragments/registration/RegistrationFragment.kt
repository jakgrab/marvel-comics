package com.example.auth.fragments.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.auth.R
import com.example.auth.databinding.FragmentRegistrationBinding
import com.example.auth.utils.navigateToActivity
import com.example.core.constants.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!

    private val registrationViewModel by viewModels<RegistrationViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btCreateAccount.setOnClickListener {
            if(registrationViewModel.validateUserData()) {
                registrationViewModel.signUpNewUser(
                    onSuccess = {
                        Toast.makeText(this.context, "Account created", Toast.LENGTH_SHORT).show()
                        context?.navigateToActivity(Constants.MAIN_ACTIVITY_PATH)
                    },
                    onError = {
                        Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
                    }
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    "Email or password contain errors",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.arrowBack.setOnClickListener {
            findNavController().navigate(R.id.action_RegistrationFragment_to_LoginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}