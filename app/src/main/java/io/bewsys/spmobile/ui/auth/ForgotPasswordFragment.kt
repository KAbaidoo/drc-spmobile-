package io.bewsys.spmobile.ui.auth

import android.view.View
import androidx.core.view.isEmpty
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentForgotPasswordBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordFragment :
    BaseFragment<FragmentForgotPasswordBinding>(FragmentForgotPasswordBinding::inflate) {

    val viewModel: ForgotPasswordViewModel by viewModel()

    override fun FragmentForgotPasswordBinding.initialize() {


        textFieldEmail.editText?.setText(viewModel.email)

        textFieldEmail.editText?.addTextChangedListener {
            viewModel.email = it.toString()
        }


        btnGetPasswordd.setOnClickListener {

            hideKeyboard()

            textFieldEmail.editText?.clearFocus()
            viewModel.apply {
                if (textFieldEmail.editText?.text.isNullOrEmpty()) {
                    showSnackBar(getString(R.string.enter_email))

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
                    is BaseViewModel.Event.Loading ->

                        showProgressBar(true)


                    is BaseViewModel.Event.Successful -> {
                        showProgressBar(false)


                        val action =
                            LoginFragmentDirections.actionGlobalLoginDialogFragment(getString(R.string.password_sent_to_email))
                        navigateTo(action)
                        btnGetPasswordd.visibility = View.GONE
                        btnGoToLogin.visibility = View.VISIBLE
                    }

                    is BaseViewModel.Event.ShowSnackBar -> {
                        showSnackBar(event.msg)
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


}