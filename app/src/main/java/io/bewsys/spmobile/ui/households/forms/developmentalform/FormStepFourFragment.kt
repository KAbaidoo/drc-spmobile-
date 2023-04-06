package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdFourHomeBinding
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepFourFragment : Fragment(R.layout.fragment_add_household_four_home) {
    private val viewModel: SharedDevelopmentalFormViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdFourHomeBinding.bind(view)

        binding.apply {


            when (viewModel.isIncomeRegular) {
                rbYesIncomeRegular.text -> rgIsIncomeRegular.check(rbYesIncomeRegular.id)
                rbNoIncomeRegular.text -> rgIsIncomeRegular.check(rbNoIncomeRegular.id)
            }
            when (viewModel.bankCardOrAccount) {
                rbYesAccountOrBankCard.text -> rgBankAccountOrBankCard.check(
                    rbYesAccountOrBankCard.id
                )
                rbNoAccountOrBankCard.text -> rgBankAccountOrBankCard.check(
                    rbNoAccountOrBankCard.id
                )
            }
            when (viewModel.benefitFromSocialAssistanceProgram) {
                rbYesSocialAssistanceProgram.text -> rgSocialAssistanceProgram.check(
                    rbYesSocialAssistanceProgram.id
                )
                rbNoSocialAssistanceProgram.text -> rgSocialAssistanceProgram.check(
                    rbNoSocialAssistanceProgram.id
                )
            }

            //set text fields
            val tils = listOf(
                tilNameOfSocialAssistanceProgram,
                tilMigrationStatus,
                tilOtherMigrationStatus,
                tilDurationDisplacedReturnedRepatriatedRefugee,
                tilUnitOfMigrationDuration,
                tilHouseholdMonthlyIncome,
                tilMinimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
                tilMobileMoneyUsername,
                tilMobileMoneyPhoneNumber
            )
            viewModel.apply {
                tils.forEachIndexed { index, til ->
                    til.editText?.setText(stepFourFields[index])
                }
            }
            //            setup radio buttons listeners
            rgIsIncomeRegular.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesIncomeRegular.id -> {
                        viewModel.isIncomeRegular = rbYesIncomeRegular.text.toString()
                    }
                    else -> {
                        viewModel.isIncomeRegular = rbNoIncomeRegular.text.toString()
                    }
                }
            }
            rgBankAccountOrBankCard.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAccountOrBankCard.id -> {
                        viewModel.bankCardOrAccount = rbYesAccountOrBankCard.text.toString()
                    }
                    else -> {
                        viewModel.bankCardOrAccount = rbNoAccountOrBankCard.text.toString()
                    }
                }
            }
            rgSocialAssistanceProgram.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesSocialAssistanceProgram.id -> {
                        viewModel.benefitFromSocialAssistanceProgram =
                            rbYesSocialAssistanceProgram.text.toString()
                        tvSocialAssistanceProgram.isEnabled = true
                        tilNameOfSocialAssistanceProgram.isEnabled = true
                    }
                    else -> {
                        viewModel.benefitFromSocialAssistanceProgram =
                            rbNoSocialAssistanceProgram.text.toString()
                        tvSocialAssistanceProgram.isEnabled = false
                        tilNameOfSocialAssistanceProgram.isEnabled = false
                        tilNameOfSocialAssistanceProgram.editText?.text?.clear()

                    }
                }
            }
            val migrationStatus = resources.getStringArray(R.array.migration_status)
            val dropdownLayout = R.layout.dropdown_item
            (actvMigrationStatus as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, migrationStatus).also {
                        addTextChangedListener {
                            val status = it.toString()
                            if(status == "Other"){
                            viewModel.migrationStatus = status
                            tilOtherMigrationStatus.isEnabled = true
                            tvOtherMigrationStatus.isEnabled = true
                                tvDurationDisplacedReturnedRepatriatedRefugee.isEnabled=false
                                tilDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                    false
                            }
                            else{
                                tilOtherMigrationStatus.editText?.text?.clear()
                                tilOtherMigrationStatus.isEnabled = false
                                tvOtherMigrationStatus.isEnabled = false
                                actvUnitOfMigrationDuration.isEnabled = false
                            }

                            tvDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                status != "Resident"
                            tilDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                status != "Resident"
                            tilUnitOfMigrationDuration.isEnabled = status != "Resident"
                            actvUnitOfMigrationDuration.isEnabled = status != "Resident"
                            if( status != "Resident"){
                                tilDurationDisplacedReturnedRepatriatedRefugee.editText?.text?.clear()
                                tilUnitOfMigrationDuration.editText?.text?.clear()
                            }

                        }
                    }
                )
            }

            val unitOfMigrationDuration = resources.getStringArray(R.array.migration_duration_unit)
            (actvUnitOfMigrationDuration as? AutoCompleteTextView)?.apply {
                setAdapter(
                    ArrayAdapter(context, dropdownLayout, unitOfMigrationDuration).also {
                        addTextChangedListener {
                            val duration = it.toString()
                            viewModel.unitOfMigrationDuration = duration
                        }
                    }
                )
            }




            //            add text change listener text
            tilNameOfSocialAssistanceProgram.editText?.addTextChangedListener {
               viewModel.nameOfSocialAssistanceProgram = it.toString()
            }

            tilMigrationStatus.editText?.addTextChangedListener {
                viewModel.migrationStatus = it.toString()
            }
            tilOtherMigrationStatus.editText?.addTextChangedListener {
                viewModel.otherMigrationStatus = it.toString()
            }
            tilDurationDisplacedReturnedRepatriatedRefugee.editText?.addTextChangedListener {
                viewModel.durationDisplacedReturnedRepatriatedRefugee = it.toString()
            }
            tilUnitOfMigrationDuration.editText?.addTextChangedListener {
                viewModel.unitOfMigrationDuration = it.toString()
            }
            tilHouseholdMonthlyIncome.editText?.addTextChangedListener {
                viewModel.householdMonthlyIncome = it.toString()
            }
            tilMinimumMonthlyIncomeNecessaryLiveWithoutDifficulties.editText?.addTextChangedListener {
                viewModel.minimumMonthlyIncomeNecessaryLiveWithoutDifficulties = it.toString()
                viewModel.stepFourHasBlankFields()
            }
            tilMobileMoneyUsername.editText?.addTextChangedListener {
                viewModel.mobileMoneyUsername = it.toString()
                viewModel.stepFourHasBlankFields()
            }
            tilMobileMoneyPhoneNumber.editText?.addTextChangedListener {
                viewModel.mobileMoneyPhoneNumber = it.toString()
                viewModel.stepFourHasBlankFields()
            }



            btnNext.setOnClickListener {
                val action =
                    FormStepFourFragmentDirections.actionFormStepFourFragmentToFormStepFiveFragment()
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepFourFragmentDirections.actionFormStepFourFragmentToFormStepThreeFragment()
                findNavController().navigate(action)
            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepFourHasBlankFields.collectLatest {
                    btnNext.isEnabled =  !it
                }

            }


        }
    }
}