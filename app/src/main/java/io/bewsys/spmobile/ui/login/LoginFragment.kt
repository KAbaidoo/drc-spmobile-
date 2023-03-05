package io.bewsys.spmobile.ui.login

import android.os.Bundle

import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle

import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : Fragment(R.layout.fragment_login) {

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
                requireActivity().actionBar?.hide()
                buttonLogin.setOnClickListener {
                    viewModel.login()
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.loginEvent.collectLatest { event ->
                            when (event) {
                                is LoginViewModel.LoginEvent.Loading -> progressBar.isVisible = true

                                is LoginViewModel.LoginEvent.Successful -> {
                                    progressBar.isVisible = false
                                    setFragmentResult(
                                        "login_request",
                                        bundleOf("login_result" to event.results)
                                    )
                                    findNavController().navigate(R.id.nav_dashboard)
                                }
                                is LoginViewModel.LoginEvent.ShowMessage ->
                                    Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                                        .show()

                                is LoginViewModel.LoginEvent.Exception -> {
                                    progressBar.isVisible = false
                                    Snackbar.make(
                                        requireView(),
                                        event.errorMsg,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                                is LoginViewModel.LoginEvent.Failure -> {
                                    progressBar.isVisible = false
                                    Snackbar.make(
                                        requireView(),
                                        event.errorMsg,
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
      viewModel.showLoggedOutMessage()

    }


}