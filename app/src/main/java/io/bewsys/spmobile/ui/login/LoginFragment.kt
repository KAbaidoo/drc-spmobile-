package io.bewsys.spmobile.ui.login

import android.os.Bundle

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.navigation.fragment.findNavController


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentLoginBinding
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment(R.layout.fragment_login) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: LoginViewModel by viewModel()
        val binding = FragmentLoginBinding.bind(view)



        binding.apply {
            textFieldEmail.editText?.setText(viewModel.email)
            textFieldPassword.editText?.setText(viewModel.password)

            textFieldEmail.editText?.addTextChangedListener {
                viewModel.email = it.toString()
            }
            textFieldPassword.editText?.addTextChangedListener {
                viewModel.password = it.toString()
            }

            binding.apply {
                buttonLogin.setOnClickListener {
                    viewModel.login()
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.loginEvent.collectLatest { event ->
                            when (event) {
                                is LoginViewModel.LoginEvent.Loading -> progressBar.isVisible = true
                                is LoginViewModel.LoginEvent.LoginSuccessful -> {
                                    progressBar.isVisible = false
                                    gotoHomeScreen()
                                }

                                else -> {
                                    TODO()
                                }
                            }
                        }
                    }
                }



                requireActivity().onBackPressedDispatcher.addCallback(object :
                    OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        requireActivity().finish()
                    }
                })
            }

        }


    }


    fun gotoHomeScreen() {
        val action = LoginFragmentDirections.actionLoginFragmentToNavDashboard()
        findNavController().navigate(action)
    }

}