package io.bewsys.spmobile.ui.auth

import io.bewsys.spmobile.databinding.FragmentForgotPasswordBinding
import io.bewsys.spmobile.ui.common.BaseFragment

class ForgotPasswordBaseFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {
   /* val viewModel: ForgotPasswordViewModel by viewModel()
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
            viewModel.goToLoginClicked()
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.PasswordEventChannel.collectLatest { event ->
                when (event) {
                    is ForgotPasswordViewModel.ForgotPassword.Loading -> progressBar.isVisible =
                        true

                    is ForgotPasswordViewModel.ForgotPassword.Successful -> {
                        progressBar.isVisible = false
                        val action =
                            LoginFragmentDirections.actionGlobalLoginDialogFragment(event.msg)
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
                        val action =
                            ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToNavLogin(
                                event.email
                            )
                        findNavController().navigate(action)
                    }
                }


            }

        }

    }*/
}