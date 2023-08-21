package io.bewsys.spmobile.ui.auth

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
import io.bewsys.spmobile.BuildConfig


import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentLoginBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {


    val viewModel: LoginViewModel by viewModel()
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



    override fun FragmentLoginBinding.initialize()  {
        versionName.text = getString(R.string.version_name, BuildConfig.VERSION_NAME)

        textFieldEmail.editText?.setText(viewModel.email)
        textFieldPassword.editText?.setText(viewModel.password)

        textFieldEmail.editText?.addTextChangedListener {
            viewModel.email = it.toString()
        }
        textFieldPassword.editText?.addTextChangedListener {
            viewModel.password = it.toString()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        is BaseViewModel.Event.Loading ->  showProgressBar(true)

                        is BaseViewModel.Event.Successful -> {
                            showProgressBar(false)
                            setFragmentResult(
                                "user_request",
                                bundleOf("user_result" to event.result)
                            )
                            navigateTo(R.id.nav_dashboard)
                        }
                        is BaseViewModel.Event.ShowSnackBar -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                                .show()
                            showProgressBar(false)
                        }


                        is BaseViewModel.Event.Error -> {
                            showProgressBar(false)

                            val action =
                                LoginFragmentDirections.actionGlobalLoginDialogFragment(event.errorMsg)
                            navigateTo(action)

                        }

                    }
                }
            }
        }

        buttonLogin.setOnClickListener {
            viewModel.loginClicked()
        }
        btnForgotPassword.setOnClickListener {
            val action =  LoginFragmentDirections.actionNavLoginToForgotPasswordFragment(
                email = viewModel.email
            )
            navigateTo(action)
        }
        viewModel.showLoggedOutSnackMessage()

    }// end of initialize
}