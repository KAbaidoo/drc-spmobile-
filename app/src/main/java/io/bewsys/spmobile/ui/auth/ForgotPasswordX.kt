package io.bewsys.spmobile.ui.auth

import android.view.View
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentForgotPasswordBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest

class ForgotPasswordX :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {


    val viewModel: ForgotPasswordViewModelX by viewModels()

    override fun FragmentForgotPasswordBinding.initialize() {


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
            val action =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToNavLogin(
                    viewModel.email
                )
            navigateTo(action)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest { event ->
                when (event) {
                    is BaseViewModel.Event.Loading -> progressBar.isVisible =
                        true

                    is BaseViewModel.Event.Successful -> {
                        progressBar.isVisible = false
                        val action =
                            LoginFragmentDirections.actionGlobalLoginDialogFragment(getString(R.string.password_sent_to_email))
                        navigateTo(action)
                        btnGetPasswordd.visibility = View.GONE
                        btnGoToLogin.visibility = View.VISIBLE
                    }

                    is BaseViewModel.Event.ShowSnackBar -> {
                        showSnackBar(event.msg)
                        progressBar.isVisible = false
                    }

                    is BaseViewModel.Event.Error -> {
                        progressBar.isVisible = false
                        val action =
                            LoginFragmentDirections.actionGlobalLoginDialogFragment(event.errorMsg)
                        navigateTo(action)
                    }

                }

            }
        }

    }


}