package io.bewsys.spmobile.ui.profile

import android.os.Bundle
import android.view.View

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

import io.bewsys.spmobile.databinding.FragmentProfileBinding
import io.bewsys.spmobile.ui.common.BaseFragment
import io.bewsys.spmobile.ui.common.BaseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate) {

    val viewModel: ProfileViewModel by viewModel()

    override fun FragmentProfileBinding.initialize() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.userProfile.collect {
                    textFieldName.editText?.setText(it.name)
                    textFieldEmail.editText?.setText(it.email)
                    textFieldPhoneNumber.editText?.setText(it.phoneNumber)
                    textFieldSupervisor.editText?.setText(it.supervisorName)
                    textFieldTeamLeader.editText?.setText(it.teamLeader)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.events.collectLatest { event ->
                    when (event) {
                        is BaseViewModel.Event.Loading -> showProgressBar(true)

                        is BaseViewModel.Event.Successful -> {
                            showProgressBar(false)
                            setFragmentResult(
                                "user_request",
                                bundleOf("user_result" to event.result)
                            )
                            navigateTo(R.id.nav_dashboard)
                        }

                        is BaseViewModel.Event.ShowSnackBar ->

                            showSnackBar(event.msg)

                        is BaseViewModel.Event.Error -> {
                            showProgressBar(false)
                            showSnackBar(event.errorMsg)
                        }


                    }
                }
            }
        }
        textFieldPhoneNumber.editText?.addTextChangedListener {
            viewModel.phoneNumber = it.toString()
        }

        buttonUpdateUserDetails.setOnClickListener {
            viewModel.updateButtonClicked()
        }
    }
}