package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdFiveShockBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepFiveFragment : Fragment(R.layout.fragment_add_household_five_shock) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddHouseholdFiveShockBinding.bind(view)

        binding.apply {
            val tils = listOf(
                tilDaysSpentReduceMealsConsumedCopingStrategy,
                tilDaysSpentReduceMealsAdultForfeitMealForChildCopingStrategy,
                tilDaysSpentReduceAmountConsumedCopingStrategy,
                tilDaysSpentEatLessExpensivelyCopingStrategy,
                tilDaysSpentDaysWithoutEatingCopingStrategy,
                tilDaysSpentConsumeWildFoodCopingStrategy,
                tilDaysSpentBorrowFoodOrRelyOnFamilyHelpCopingStrategy,
                tilDaysSpentBeggingCopingStrategy,
                tilDaysSpentOtherCopingStrategy,
                tilNumberOfMealsEatenByChildren6To17Yesterday,
                tilNumberOfMealsEatenByChildren2To5Yesterday,
                tilNumberOfMealsEatenByAdults18PlusYesterday,
                tilNumberOfDaysInWeekConsumedSugarOrSweetProducts,
                tilNumberOfDaysInWeekConsumedStapleFoods,
                tilNumberOfDaysInWeekConsumedVegetables,
                tilNumberOfDaysInWeekConsumedMeat,
                tilNumberOfDaysInWeekConsumedLegumesOrNuts,
                tilNumberOfDaysInWeekConsumedFruits,
                tilNumberOfDaysInWeekConsumedDairyProducts,
                tilNumberOfDaysInWeekConsumedCookingOils
            )
            tils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener {
                    viewModel.setStepFiveFields(index, it)
                    viewModel.stepFiveHasBlankFields()
                }
            }

            when (viewModel.affectedByConflict) {
                rbYesAffectedByConflict.text -> rgAffectedByConflict.check(rbYesAffectedByConflict.id)
                rbNoAffectedByConflict.text -> rgAffectedByConflict.check(rbNoAffectedByConflict.id)
            }
            when (viewModel.affectedByEpidemic) {
                rbYesAffectedByEpidemic.text -> rgAffectedByEpidemic.check(rbYesAffectedByEpidemic.id)
                rbNoAffectedByEpidemic.text -> rgAffectedByEpidemic.check(rbNoAffectedByEpidemic.id)
            }
            when (viewModel.affectedByClimateShock) {
                rbYesAffectedByClimateShock.text -> rgAffectedByClimateShock.check(
                    rbYesAffectedByClimateShock.id
                )
                rbNoAffectedByClimateShock.text -> rgAffectedByClimateShock.check(
                    rbNoAffectedByClimateShock.id
                )
            }
            when (viewModel.affectedByOtherShock) {
                rbYesAffectedByOtherShock.text -> rgAffectedByOtherShock.check(
                    rbYesAffectedByOtherShock.id
                )
                rbNoAffectedByOtherShock.text -> rgAffectedByOtherShock.check(
                    rbNoAffectedByOtherShock.id
                )
            }
            when (viewModel.takeChildrenOutOfSchool) {
                rbYesTakeChildrenOutOfSchool.text -> rgTakeChildrenOutOfSchool.check(
                    rbYesTakeChildrenOutOfSchool.id
                )
                rbNoTakeChildrenOutOfSchool.text -> rgTakeChildrenOutOfSchool.check(
                    rbNoTakeChildrenOutOfSchool.id
                )
            }
            when (viewModel.useOfChildLabour) {
                rbYesUseOfChildLabor.text -> rgUseOfChildLabor.check(rbYesUseOfChildLabor.id)
                rbNoUseOfChildLabor.text -> rgUseOfChildLabor.check(rbNoUseOfChildLabor.id)
            }
            when (viewModel.useOfEarlyMarriage) {
                rbYesUseOfEarlyMarriage.text -> rgUseOfEarlyMarriage.check(rbYesUseOfEarlyMarriage.id)
                rbNoUseOfEarlyMarriage.text -> rgUseOfEarlyMarriage.check(rbNoUseOfEarlyMarriage.id)
            }
            when (viewModel.gaveUpHealthCare) {
                rbYesGiveUpHealthCare.text -> rgGiveUpHealthCare.check(rbYesGiveUpHealthCare.id)
                rbNoGiveUpHealthCare.text -> rgGiveUpHealthCare.check(rbNoGiveUpHealthCare.id)
            }
            when (viewModel.saleOfProductionAssets) {
                rbYesSaleOfProductionAssets.text -> rgSaleOfProductionAssets.check(
                    rbYesSaleOfProductionAssets.id
                )
                rbNoSaleOfProductionAssets.text -> rgSaleOfProductionAssets.check(
                    rbNoSaleOfProductionAssets.id
                )
            }





            rgAffectedByConflict.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAffectedByConflict.id -> {
                        viewModel.affectedByConflict = rbYesAffectedByConflict.text.toString()
                    }
                    else -> {
                        viewModel.affectedByConflict = rbNoAffectedByConflict.text.toString()
                    }
                }
            }
            rgAffectedByEpidemic.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAffectedByEpidemic.id -> {
                        viewModel.affectedByEpidemic = rbYesAffectedByEpidemic.text.toString()
                    }
                    else -> {
                        viewModel.affectedByEpidemic = rbNoAffectedByEpidemic.text.toString()
                    }
                }
            }
            rgAffectedByClimateShock.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAffectedByClimateShock.id -> {
                        viewModel.affectedByClimateShock =
                            rbYesAffectedByClimateShock.text.toString()
                    }
                    else -> {
                        viewModel.affectedByClimateShock =
                            rbNoAffectedByClimateShock.text.toString()
                    }
                }
            }
            rgAffectedByOtherShock.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAffectedByOtherShock.id -> {
                        viewModel.affectedByOtherShock = rbYesAffectedByOtherShock.text.toString()
                    }
                    else -> {
                        viewModel.affectedByOtherShock = rbNoAffectedByOtherShock.text.toString()
                    }
                }
            }
            rgTakeChildrenOutOfSchool.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesTakeChildrenOutOfSchool.id -> {
                        viewModel.takeChildrenOutOfSchool =
                            rbYesTakeChildrenOutOfSchool.text.toString()
                    }
                    else -> {
                        viewModel.takeChildrenOutOfSchool =
                            rbNoTakeChildrenOutOfSchool.text.toString()
                    }
                }
            }
            rgUseOfChildLabor.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesUseOfChildLabor.id -> {
                        viewModel.useOfChildLabour = rbYesUseOfChildLabor.text.toString()
                    }
                    else -> {
                        viewModel.useOfChildLabour = rbNoUseOfChildLabor.text.toString()
                    }
                }
            }
            rgUseOfEarlyMarriage.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesUseOfEarlyMarriage.id -> {
                        viewModel.useOfEarlyMarriage = rbYesUseOfEarlyMarriage.text.toString()
                    }
                    else -> {
                        viewModel.useOfEarlyMarriage = rbNoUseOfEarlyMarriage.text.toString()
                    }
                }
            }
            rgGiveUpHealthCare.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesGiveUpHealthCare.id -> {
                        viewModel.gaveUpHealthCare = rbYesGiveUpHealthCare.text.toString()
                    }
                    else -> {
                        viewModel.gaveUpHealthCare = rbNoGiveUpHealthCare.text.toString()
                    }
                }
            }
            rgSaleOfProductionAssets.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesSaleOfProductionAssets.id -> {
                        viewModel.saleOfProductionAssets =
                            rbYesSaleOfProductionAssets.text.toString()
                    }
                    else -> {
                        viewModel.saleOfProductionAssets =
                            rbNoSaleOfProductionAssets.text.toString()
                    }
                }
            }

            viewModel.apply {
                tils.forEachIndexed { index, til ->
                    til.editText?.setText(stepFiveFields[index])
                }
            }




            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepSixFragment2,bundle )
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepFourFragment,bundle )

            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepFiveHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }

        }
        // set up menu
//        val menuHost = requireActivity()
//        menuHost.addMenuProvider(object : MenuProvider {
//            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//                menuInflater.inflate(R.menu.fragment_households_menu, menu)
//
//            }
//
//            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
//                return when (menuItem.itemId) {
//
//                    R.id.action_download_households -> {
//                        val bundle = bundleOf("id" to viewModel.id)
//                        findNavController().navigate(R.id.deleteHouseholdDialogFragment, bundle)
//
//                        true
//                    }
//
//                    else -> false
//                }
//            }
//        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }
}


