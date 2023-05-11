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

            viewModel.apply {
                tilDaysSpentReduceMealsConsumedCopingStrategy.editText?.addTextChangedListener {
                    daysReducedMealsConsumed = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentReduceMealsAdultForfeitMealForChildCopingStrategy.editText?.addTextChangedListener {
                    daysReducedMealsAdult = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentReduceAmountConsumedCopingStrategy.editText?.addTextChangedListener {
                    daysReducedAmountConsumed = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentEatLessExpensivelyCopingStrategy.editText?.addTextChangedListener {
                    daysEatLessExpensively = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentDaysWithoutEatingCopingStrategy.editText?.addTextChangedListener {
                    daysWithoutEating = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentConsumeWildFoodCopingStrategy.editText?.addTextChangedListener {
                    daysConsumedWildFood = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentBorrowFoodOrRelyOnFamilyHelpCopingStrategy.editText?.addTextChangedListener {
                    daysBorrowFood = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentBeggingCopingStrategy.editText?.addTextChangedListener {
                    daysBeggingFood = it.toString()
                    stepFiveHasBlankFields()
                }
                tilDaysSpentOtherCopingStrategy.editText?.addTextChangedListener {
                    daysOtherCoping = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfMealsEatenByChildren6To17Yesterday.editText?.addTextChangedListener {
                    numberOfMealsChildren6To17 = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfMealsEatenByChildren2To5Yesterday.editText?.addTextChangedListener {
                    numberOfMealsChildren2To5 = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfMealsEatenByAdults18PlusYesterday.editText?.addTextChangedListener {
                    numberOfMealsAdults18plus = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedSugarOrSweetProducts.editText?.addTextChangedListener {
                    daysConsumedSugarOrSweets = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedStapleFoods.editText?.addTextChangedListener {
                    daysConsumedStapleFoods = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedVegetables.editText?.addTextChangedListener {
                    daysConsumedVegetables = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedMeat.editText?.addTextChangedListener {
                    daysConsumedMeat = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedLegumesOrNuts.editText?.addTextChangedListener {
                    daysConsumedLegumes = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedFruits.editText?.addTextChangedListener {
                    daysConsumedFruits = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedDairyProducts.editText?.addTextChangedListener {
                    daysConsumedDiary = it.toString()
                    stepFiveHasBlankFields()
                }
                tilNumberOfDaysInWeekConsumedCookingOils.editText?.addTextChangedListener {
                    daysConsumedCookingOils = it.toString()
                    stepFiveHasBlankFields()
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
                tilDaysSpentReduceMealsConsumedCopingStrategy.editText?.setText(daysReducedMealsConsumed)
                tilDaysSpentReduceMealsAdultForfeitMealForChildCopingStrategy.editText?.setText(daysReducedMealsAdult)
                tilDaysSpentReduceAmountConsumedCopingStrategy.editText?.setText(daysReducedAmountConsumed)
                tilDaysSpentEatLessExpensivelyCopingStrategy.editText?.setText(daysEatLessExpensively)
                tilDaysSpentDaysWithoutEatingCopingStrategy.editText?.setText(daysWithoutEating)
                tilDaysSpentConsumeWildFoodCopingStrategy.editText?.setText(daysConsumedWildFood)
                tilDaysSpentBorrowFoodOrRelyOnFamilyHelpCopingStrategy.editText?.setText(daysBorrowFood)
                tilDaysSpentBeggingCopingStrategy.editText?.setText(daysBeggingFood)
                tilDaysSpentOtherCopingStrategy.editText?.setText(daysOtherCoping)
                tilNumberOfMealsEatenByChildren6To17Yesterday.editText?.setText(numberOfMealsChildren6To17)
                tilNumberOfMealsEatenByChildren2To5Yesterday.editText?.setText(numberOfMealsChildren2To5)
                tilNumberOfMealsEatenByAdults18PlusYesterday.editText?.setText(numberOfMealsAdults18plus)
                tilNumberOfDaysInWeekConsumedSugarOrSweetProducts.editText?.setText(daysConsumedSugarOrSweets)
                tilNumberOfDaysInWeekConsumedStapleFoods.editText?.setText(daysConsumedStapleFoods)
                tilNumberOfDaysInWeekConsumedVegetables.editText?.setText(daysConsumedVegetables)
                tilNumberOfDaysInWeekConsumedMeat.editText?.setText(daysConsumedMeat)
                tilNumberOfDaysInWeekConsumedLegumesOrNuts.editText?.setText(daysConsumedLegumes)
                tilNumberOfDaysInWeekConsumedFruits.editText?.setText(daysConsumedFruits)
                tilNumberOfDaysInWeekConsumedDairyProducts.editText?.setText(daysConsumedDiary)
                tilNumberOfDaysInWeekConsumedCookingOils.editText?.setText(daysConsumedCookingOils)

            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepSixFragment2, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepFourFragment, bundle)

            }
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepFiveHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }

        }


    }
}


