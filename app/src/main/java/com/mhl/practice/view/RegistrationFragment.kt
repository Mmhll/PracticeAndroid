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
import com.mhl.practice.databinding.FragmentRegistrationBinding
import com.mhl.practice.viewmodel.RegistrationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()
    private var success = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationButton.setOnClickListener {
            val email = binding.registrationEmailInput.text.toString()
            val password = binding.registrationPasswordInput.text.toString()
            val repassword = binding.registrationRePasswordInput.text.toString()
            val nickname = binding.registrationNicknameInput.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty() && repassword.isNotEmpty() && nickname.isNotEmpty()) {
                lifecycleScope.launch {
                    success = viewModel.register(nickname, email, password, repassword)
                }
                if (success) {

                    findNavController().navigate(R.id.action_registrationFragment_to_authorizationFragment)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Что-то пошло не так или вы ввели некорректные данные",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Вы не заполнили поле(-я) логина и(или) пароля",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

}