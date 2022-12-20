package com.mhl.practice.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mhl.practice.R
import com.mhl.practice.databinding.FragmentAuthorizationBinding
import com.mhl.practice.databinding.FragmentRegistrationBinding
import com.mhl.practice.viewmodel.AuthorizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAuthorizationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.authButton.setOnClickListener {
            val email = binding.authorizationInputLogin.text.toString()
            val password = binding.authorizationInputPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val success = viewModel.authorize(email, password)
                    if (success) {
                        savedInstanceState?.putStringArrayList(
                            "userData",
                            arrayListOf(
                                viewModel.remoteData.value!!.email,
                                viewModel.remoteData.value!!.password,
                                viewModel.remoteData.value!!.fullName
                            )
                        )
                        findNavController().navigate(R.id.action_authorizationFragment_to_gameFragment)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Что-то пошло не так или вы ввели некорректные данные",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Вы не заполнили поле(-я) логина и(или) пароля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.toRegButton.setOnClickListener {
            findNavController().navigate(R.id.action_authorizationFragment_to_registrationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}