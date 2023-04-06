package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.view.View


import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneConsentBinding
import kotlinx.coroutines.flow.collectLatest

import org.koin.androidx.viewmodel.ext.android.activityViewModel


class FormStepOneFragment : Fragment(R.layout.fragment_add_household_one_consent) {
    private val viewModel: SharedDevelopmentalFormViewModel by activityViewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = FragmentAddHouseholdOneConsentBinding.bind(view)

        viewModel.setStartTime()

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.yesConsent.collectLatest {
                    btnNext.isEnabled =  it == "Yes"
                }
            }
            when(viewModel.consent){
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




                btnNext.setOnClickListener {
                    val action =
                        FormStepOneFragmentDirections.actionFormStepOneFragmentToFormStepTwoFragment()
                    findNavController().navigate(action)
                }

            }


        }


    }

}