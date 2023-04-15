package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdFourHomeBinding
import io.ktor.client.utils.EmptyContent.status
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepFourFragment : Fragment(R.layout.fragment_add_household_four_home) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdFourHomeBinding.bind(view)

        binding.apply {

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepFourHasBlankFields.collectLatest {
                    btnNext.isEnabled =  it.not()
                }
            }
            //set text fields
            val tils = listOf(
                tilNameOfSocialAssistanceProgram,
                tilOtherMigrationStatus,
                tilDurationDisplacedReturnedRepatriatedRefugee,
                tilHouseholdMonthlyIncome,
                tilMinimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
                tilMobileMoneyUsername,
                tilMobileMoneyPhoneNumber
            )
            tilMigrationStatus.editText?.setText(viewModel.migrationStatus)
            tilUnitOfMigrationDuration.editText?.setText(viewModel.unitOfMigrationDuration)

            viewModel.apply {
                tils.forEachIndexed { index, til ->
                    til.editText?.setText(stepFourFields[index])
                }
            }

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
                            if(status == getString(R.string.other)){
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
                                status != getString(R.string.resident)
                            tilDurationDisplacedReturnedRepatriatedRefugee.isEnabled =
                                status != getString(R.string.resident)
                            tilUnitOfMigrationDuration.isEnabled = status != getString(R.string.resident)
                            actvUnitOfMigrationDuration.isEnabled = status != getString(R.string.resident)
                            if( status != getString(R.string.resident)){
                                tilDurationDisplacedReturnedRepatriatedRefugee.editText?.text?.clear()
                                tilUnitOfMigrationDuration.editText?.text?.clear()
                            }
                            viewModel.stepFourHasBlankFields()

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
                            viewModel.stepFourHasBlankFields()
                        }
                    }
                )
            }




            //            add text change listener text
            tils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener( object : TextWatcher {
                    override fun beforeTextChanged(
                        p0: CharSequence?,
                        p1: Int,
                        p2: Int,
                        p3: Int
                    ) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        viewModel.setStepFourFields(index,p0)
                        viewModel.stepFourHasBlankFields()
                    }
                    override fun afterTextChanged(p0: Editable?) {
                    }
                }
                )
            }
            val title = if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val action =
                    FormStepFourFragmentDirections.actionFormStepFourFragmentToFormStepFiveFragment(
                        title  =title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }
            btnPrevious.setOnClickListener {
                val action =
                    FormStepFourFragmentDirections.actionFormStepFourFragmentToFormStepThreeFragment(
                        title  =title,
                        household = viewModel.household
                    )
                findNavController().navigate(action)
            }



        }// end of apply block

        // set up menu
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.fragment_households_menu, menu)

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {

                    R.id.action_download_households -> {
                        val bundle = bundleOf("id" to viewModel.id)
                        findNavController().navigate(R.id.deleteHouseholdDialogFragment, bundle)

                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }// end of onCreate
}