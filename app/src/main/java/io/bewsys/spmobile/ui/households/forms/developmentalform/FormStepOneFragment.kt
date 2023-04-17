package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider


import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.FormNavigationArgs
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneConsentBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FormStepOneFragment : Fragment(R.layout.fragment_add_household_one_consent) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)
    private var _binding : FragmentAddHouseholdOneConsentBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         _binding = FragmentAddHouseholdOneConsentBinding.bind(view)



        viewModel.setStartTime()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addDevelopmentalHouseholdEvent.collect { event ->
                when (event) {
                    is SharedDevelopmentalFormViewModel.AddDevelopmentalHouseholdEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is SharedDevelopmentalFormViewModel.AddDevelopmentalHouseholdEvent.NavigateBackWithResults -> {

                        setFragmentResult(
                            "add_household_request",
                            bundleOf("add_household_result" to event.results)
                        )
//                            findNavController().popBackStack()
                        findNavController().navigate(R.id.action_global_nav_household)

                    }
                }

            }

        }


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.yesConsent.collectLatest {
                    btnNext.isEnabled = it == getString(R.string.yes)
                }
            }
            when (viewModel.consent) {
                rbYes.text -> rgConsent.check(rbYes.id)
                rbNo.text -> rgConsent.check(rbNo.id)
            }

            rgConsent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYes.id -> {
                        viewModel.consent = rbYes.text.toString()
                    }
                    else -> {
                        viewModel.consent = rbNo.text.toString()
                    }
                }
            }

            btnNext.setOnClickListener {
                val title = if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)
                val action =
                    FormStepOneFragmentDirections.actionFormStepOneFragmentToFormStepTwoFragment(
                        title = title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}