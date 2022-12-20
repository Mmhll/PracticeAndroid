package com.mhl.practice.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mhl.practice.R
import com.mhl.practice.databinding.FragmentAuthorizationBinding
import com.mhl.practice.model.LocalRepository
import com.mhl.practice.viewmodel.AuthorizationViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthorizationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.isAuthorized.observe(viewLifecycleOwner){
            Log.d("TAG", it.toString())
            if (it){
                findNavController().navigate(R.id.auth_to_game)
            }
        }


        binding.authButton.setOnClickListener {
            val email = binding.authorizationInputLogin.text.toString()
            val password = binding.authorizationInputPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.authorize(email, password)
                viewModel.isSuccessful.observe(viewLifecycleOwner) {
                    if (it) {
                        findNavController().navigate(R.id.auth_to_game)
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
            findNavController().navigate(R.id.auth_to_reg)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}