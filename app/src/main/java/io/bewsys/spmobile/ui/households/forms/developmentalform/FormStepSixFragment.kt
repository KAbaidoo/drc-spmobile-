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
import io.bewsys.spmobile.databinding.FragmentAddHouseholdSixPropertyBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepSixFragment : Fragment(R.layout.fragment_add_household_six_property) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddHouseholdSixPropertyBinding.bind(view)


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.stepSixHasBlankFields.collectLatest {
                    btnNext.isEnabled = it.not()
                }

            }
            val tils = listOf(
                tilNumberOfMosquitoNetsOwned,
                tilNumberOfGuineaPigOwned,
                tilNumberOfPoultryOwned,
                tilNumberOfBicycleOwned,
                tilNumberOfOilStoveOwned,
                tilNumberOfRabbitOwned,
                tilNumberOfSheepOwned,
                tilNumberOfMotorbikeOwned,
                tilNumberOfRoomsUsedForSleeping,
                tilNumberOfWheelbarrowOwned,
                tilNumberOfRadioOwned,
                tilNumberOfStoveOrOvenOwned,
                tilNumberOfRickshawOwned,
                tilNumberOfSolarPlateOwned,
                tilNumberOfSewingMachineOwned,
                tilNumberOfMattressOwned,
                tilNumberOfTelevisionOwned,
                tilNumberOfTableOwned,
                tilNumberOfSofaOwned,
                tilNumberOfHandsetOrPhoneOwned,
                tilNumberOfElectricIronOwned,
                tilNumberOfPlowOwned,
                tilNumberOfGoatOwned,
                tilNumberOfFridgeOwned,
                tilNumberOfDvdDriverOwned,
                tilNumberOfPigsOwned,
                tilNumberOfCanalTntTvCableOwned,
                tilNumberOfCowOwned,
                tilNumberOfCharcoalIronOwned,
                tilNumberOfComputerOwned,
                tilNumberOfChairOwned,
                tilNumberOfCartOwned,
                tilNumberOfCarsOwned,
                tilNumberOfBedOwned,
                tilNumberOfAirConditionerOwned,
                tilAmountOfCultivableLandOwned,
                tilNumberOfFanOwned
            )


            when (viewModel.hasLiveStock) {
                rbYesHasLivestock.text -> rgHasLivestock.check(rbYesHasLivestock.id)
                rbNoHasLivestock.text -> rgHasLivestock.check(rbNoHasLivestock.id)
            }
            when (viewModel.hasHouseholdGoods) {
                rbYesHasHouseholdGoods.text -> rgHasHouseholdGoods.check(rbYesHasHouseholdGoods.id)
                rbNoHasHouseholdGoods.text -> rgHasHouseholdGoods.check(rbNoHasHouseholdGoods.id)
            }
            when (viewModel.accessToCultivableLand) {
                rbYesAccessToCultivableLand.text -> rgAccessToCultivableLand.check(
                    rbYesAccessToCultivableLand.id
                )
                rbNoAccessToCultivableLand.text -> rgAccessToCultivableLand.check(
                    rbNoAccessToCultivableLand.id
                )
            }
            when (viewModel.ownerOfCultivableLand) {
                rbYesOwnerOfCultivableLand.text -> rgOwnerOfCultivableLand.check(
                    rbYesOwnerOfCultivableLand.id
                )
                rbNoOwnerOfCultivableLand.text -> rgOwnerOfCultivableLand.check(
                    rbNoOwnerOfCultivableLand.id
                )
            }
            when (viewModel.cashCropOrCommercialFarming) {
                rbYesPracticeOfCashCropFarmingOrCommercialFarming.text -> rgPracticeOfCashCropFarmingOrCommercialFarming.check(
                    rbYesPracticeOfCashCropFarmingOrCommercialFarming.id
                )
                rbNoPracticeOfCashCropFarmingOrCommercialFarming.text -> rgPracticeOfCashCropFarmingOrCommercialFarming.check(
                    rbNoPracticeOfCashCropFarmingOrCommercialFarming.id
                )
            }

            rgHasLivestock.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasLivestock.id -> {
                        viewModel.hasLiveStock = rbYesHasLivestock.text.toString()
                    }
                    else -> {
                        viewModel.hasLiveStock = rbNoHasLivestock.text.toString()
                    }
                }
            }
            rgHasHouseholdGoods.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasHouseholdGoods.id -> {
                        viewModel.hasHouseholdGoods = rbYesHasHouseholdGoods.text.toString()
                    }
                    else -> {
                        viewModel.hasHouseholdGoods = rbNoHasHouseholdGoods.text.toString()
                    }
                }
            }
            rgAccessToCultivableLand.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAccessToCultivableLand.id -> {
                        viewModel.accessToCultivableLand =
                            rbYesAccessToCultivableLand.text.toString()
                    }
                    else -> {
                        viewModel.accessToCultivableLand =
                            rbNoAccessToCultivableLand.text.toString()
                    }
                }
            }
            rgOwnerOfCultivableLand.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesOwnerOfCultivableLand.id -> {
                        viewModel.ownerOfCultivableLand =
                            rbYesAccessToCultivableLand.text.toString()
                    }
                    else -> {
                        viewModel.ownerOfCultivableLand =
                            rbNoOwnerOfCultivableLand.text.toString()
                    }
                }
            }
            rgPracticeOfCashCropFarmingOrCommercialFarming.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesPracticeOfCashCropFarmingOrCommercialFarming.id -> {
                        viewModel.cashCropOrCommercialFarming =
                            rbYesPracticeOfCashCropFarmingOrCommercialFarming.text.toString()
                    }
                    else -> {
                        viewModel.cashCropOrCommercialFarming =
                            rbNoPracticeOfCashCropFarmingOrCommercialFarming.text.toString()
                    }
                }
            }

            tils.forEachIndexed { index, til ->
                til.editText?.addTextChangedListener {
                    viewModel.setStepSixFields(index,it)
                    viewModel.stepSixHasBlankFields()
                }
            }

            viewModel.apply {
                tils.forEachIndexed { index, til ->
                    til.editText?.setText(stepSixFields[index])
                }
            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepSevenFragment )
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepFiveFragment,bundle )

            }



        }

    }


}