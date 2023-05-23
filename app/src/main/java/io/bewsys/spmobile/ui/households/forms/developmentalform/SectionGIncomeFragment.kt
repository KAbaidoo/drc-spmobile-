package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.os.bundleOf

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHouseholdIncomeGBinding
import io.bewsys.spmobile.databinding.FragmentHousingEBinding
import io.bewsys.spmobile.databinding.FragmentWaterAndSanitationFBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

import java.util.*

class SectionGIncomeFragment : Fragment(R.layout.fragment_household_income_g) {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private var _binding: FragmentHouseholdIncomeGBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHouseholdIncomeGBinding.bind(view)


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionGHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }

            rgIncomeRegular.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbNoIncomeRegular.id -> {
                        viewModel.isIncomeRegular = rbNoIncomeRegular.text as String
                        viewModel.sectionGHasBlankFields()
                    }
                    else -> {

                        viewModel.isIncomeRegular = rbYesIncomeRegular.text as String
                        viewModel.sectionGHasBlankFields()
                    }
                }

            }
            rgBankAccountOrBankCard.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAccountOrBankCard.id -> {
                        viewModel.bankCardOrAccount = rbYesAccountOrBankCard.text as String

                        viewModel.sectionGHasBlankFields()
                    }
                    else -> {
                        viewModel.bankCardOrAccount = rbNoAccountOrBankCardr.text as String
                        viewModel.sectionGHasBlankFields()
                    }
                }

            }


            tilMonthlyIncomeNeeded.editText?.addTextChangedListener {
                viewModel.apply {
                    minimumMonthlyIncomeNecessaryLiveWithoutDifficulties = it.toString()
                    sectionGHasBlankFields()
                }
            }
            tilMonthlyIncome.editText?.addTextChangedListener {
                viewModel.apply {
                    householdMonthlyIncome = it.toString()
                    sectionGHasBlankFields()
                }
            }
            tilMobileMoneyUsername.editText?.addTextChangedListener {
                viewModel.apply {
                    mobileMoneyUsername = it.toString()
                    sectionGHasBlankFields()
                }
            }
            tilMobileMoneyPhoneNumber.editText?.addTextChangedListener {
                viewModel.apply {
                    mobileMoneyPhoneNumber = it.toString()
                    sectionGHasBlankFields()
                }
            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionHAssetsIncomeFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionFWaterAndSanitationFragment, bundle)

            }


            viewModel.apply {
                tilMonthlyIncomeNeeded.editText?.setText(
                    minimumMonthlyIncomeNecessaryLiveWithoutDifficulties
                )
                tilMonthlyIncome.editText?.setText(householdMonthlyIncome)
                tilMobileMoneyUsername.editText?.setText(mobileMoneyUsername)
                tilMobileMoneyPhoneNumber.editText?.setText(mobileMoneyPhoneNumber)
            }
            when (viewModel.isIncomeRegular) {
                rbYesIncomeRegular.text -> rgIncomeRegular.check(rbYesIncomeRegular.id)
                rbNoIncomeRegular.text -> rgIncomeRegular.check(rbNoIncomeRegular.id)
            }
            when (viewModel.bankCardOrAccount) {
                rbYesAccountOrBankCard.text -> rgBankAccountOrBankCard.check(rbYesAccountOrBankCard.id)
                rbNoAccountOrBankCardr.text -> rgBankAccountOrBankCard.check(rbNoAccountOrBankCardr.id)
            }

        } //end of apply block

        viewModel.sectionGHasBlankFields()

    }   //end of onCreate

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}