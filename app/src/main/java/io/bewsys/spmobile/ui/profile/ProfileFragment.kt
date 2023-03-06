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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentProfileBinding.bind(view)
        val viewModel: ProfileViewModel by viewModel()

        binding.apply {
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
                    viewModel.userProfileEvent.collectLatest { event ->
                        when (event) {
                            is ProfileViewModel.UserProfileEvent.Loading -> progressBar.isVisible =
                                true

                            is ProfileViewModel.UserProfileEvent.Successful -> {
                                progressBar.isVisible = false
                                setFragmentResult(
                                    "user_request",
                                    bundleOf("user_result" to event.results)
                                )
                                findNavController().navigate(R.id.nav_dashboard)
                            }
                            is ProfileViewModel.UserProfileEvent.ShowMessage ->
                                Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG)
                                    .show()

                            is ProfileViewModel.UserProfileEvent.Exception -> {
                                progressBar.isVisible = false
                                Snackbar.make(
                                    requireView(),
                                    event.errorMsg,
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                            is ProfileViewModel.UserProfileEvent.Failure -> {
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
            textFieldPhoneNumber.editText?.addTextChangedListener {
                viewModel.phoneNumber = it.toString()
            }

            buttonUpdateUserDetails.setOnClickListener {
                viewModel.updateButtonClicked()
            }
        }

    }

}