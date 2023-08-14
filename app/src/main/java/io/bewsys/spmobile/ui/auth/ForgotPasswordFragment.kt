package io.bewsys.spmobile.ui.auth

import android.os.Bundle

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentForgotPasswordBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel


class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                remove()
                requireActivity().finish()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: ForgotPasswordViewModel by viewModel()
        val binding = FragmentForgotPasswordBinding.bind(view)



        binding.apply {
            textFieldEmail.editText?.setText(viewModel.email)

            textFieldEmail.editText?.addTextChangedListener {
                viewModel.email = it.toString()
            }


            btnGetPasswordd.setOnClickListener {
                viewModel.apply {
                    if (textFieldEmail.isEmpty()) {
                        showInvalidInputMessage(getString(R.string.enter_email))
                    } else {
                        btnGetPasswordClicked()
                    }
                }
            }
            btnGoToLogin.setOnClickListener {
                viewModel.goToLoginClicked()
            }

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.PasswordEventChannel.collectLatest { event ->
                    when (event) {
                        is ForgotPasswordViewModel.ForgotPassword.Loading -> progressBar.isVisible =
                            true

                        is ForgotPasswordViewModel.ForgotPassword.Successful -> {
                            progressBar.isVisible = false
                           val action =  LoginFragmentDirections.actionGlobalLoginDialogFragment(event.msg)
                            findNavController().navigate(action)
                            btnGetPasswordd.visibility = View.GONE
                            btnGoToLogin.visibility = View.VISIBLE
                        }

                        is ForgotPasswordViewModel.ForgotPassword.ShowSnackMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                                .show()
                            progressBar.isVisible = false
                        }

                        is ForgotPasswordViewModel.ForgotPassword.Exception -> {
                            progressBar.isVisible = false
                            val action =
                                LoginFragmentDirections.actionGlobalLoginDialogFragment(event.errorMsg)
                            findNavController().navigate(action)
                        }

                        is ForgotPasswordViewModel.ForgotPassword.Failure -> {
                            progressBar.isVisible = false
                            val action =
                                LoginFragmentDirections.actionGlobalLoginDialogFragment(event.errorMsg)
                            findNavController().navigate(action)
                        }

                        is ForgotPasswordViewModel.ForgotPassword.NavigateToLoginFragment -> {
                            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToNavLogin(event.email)
                            findNavController().navigate(action)
                        }
                }
            }

        }

    }
}}