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


            viewModel.apply {
                tilNumberOfMosquitoNetsOwned.editText?.addTextChangedListener {
                    mosquitoNets = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfGuineaPigOwned.editText?.addTextChangedListener {
                    guineaPigsOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfPoultryOwned.editText?.addTextChangedListener {
                    poultryOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfBicycleOwned.editText?.addTextChangedListener {
                    bicycleOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfOilStoveOwned.editText?.addTextChangedListener {
                    oilStoveOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfRabbitOwned.editText?.addTextChangedListener {
                    rabbitOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfSheepOwned.editText?.addTextChangedListener {
                    sheepOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfMotorbikeOwned.editText?.addTextChangedListener {
                    motorbikeOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfRoomsUsedForSleeping.editText?.addTextChangedListener {
                    roomsUsedForSleeping = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfWheelbarrowOwned.editText?.addTextChangedListener {
                    wheelBarrowOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfRadioOwned.editText?.addTextChangedListener {
                    radioOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfStoveOrOvenOwned.editText?.addTextChangedListener {
                    stoveOrOvenOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfRickshawOwned.editText?.addTextChangedListener {
                    rickShawOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfSolarPlateOwned.editText?.addTextChangedListener {
                    solarPlateOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfSewingMachineOwned.editText?.addTextChangedListener {
                    sewingMachineOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfMattressOwned.editText?.addTextChangedListener {
                    mattressOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfTelevisionOwned.editText?.addTextChangedListener {
                    televisionOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfTableOwned.editText?.addTextChangedListener {
                    tableOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfSofaOwned.editText?.addTextChangedListener {
                    sofaOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfHandsetOrPhoneOwned.editText?.addTextChangedListener {
                    handsetOrPhoneOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfElectricIronOwned.editText?.addTextChangedListener {
                    electricIronOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfPlowOwned.editText?.addTextChangedListener {
                    plowOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfGoatOwned.editText?.addTextChangedListener {
                    goatOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfFridgeOwned.editText?.addTextChangedListener {
                    fridgeOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfDvdDriverOwned.editText?.addTextChangedListener {
                    dvdDriverOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfPigsOwned.editText?.addTextChangedListener {
                    pigsOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfCanalTntTvCableOwned.editText?.addTextChangedListener {
                    cableTVOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfCowOwned.editText?.addTextChangedListener {
                    cowOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfCharcoalIronOwned.editText?.addTextChangedListener {
                    charcoalIron = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfComputerOwned.editText?.addTextChangedListener {
                    computerOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfChairOwned.editText?.addTextChangedListener {
                    chairOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfCartOwned.editText?.addTextChangedListener {
                    cartsOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfCarsOwned.editText?.addTextChangedListener {
                    carsOwned
                    stepSixHasBlankFields()
                }
                tilNumberOfBedOwned.editText?.addTextChangedListener {
                    bedOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfAirConditionerOwned.editText?.addTextChangedListener {
                    airConditionerOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilAmountOfCultivableLandOwned.editText?.addTextChangedListener {
                    cultivatedLandOwned = it.toString()
                    stepSixHasBlankFields()
                }
                tilNumberOfFanOwned.editText?.addTextChangedListener {
                    fanOwned = it.toString()
                    stepSixHasBlankFields()
                }

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



            viewModel.apply {
                tilNumberOfMosquitoNetsOwned.editText?.setText(mosquitoNets)
                tilNumberOfGuineaPigOwned.editText?.setText(guineaPigsOwned)
                tilNumberOfPoultryOwned.editText?.setText(poultryOwned)
                tilNumberOfBicycleOwned.editText?.setText(bicycleOwned)
                tilNumberOfOilStoveOwned.editText?.setText(oilStoveOwned)
                tilNumberOfRabbitOwned.editText?.setText(rabbitOwned)
                tilNumberOfSheepOwned.editText?.setText(sheepOwned)
                tilNumberOfMotorbikeOwned.editText?.setText(motorbikeOwned)
                tilNumberOfRoomsUsedForSleeping.editText?.setText(roomsUsedForSleeping)
                tilNumberOfWheelbarrowOwned.editText?.setText(wheelBarrowOwned)
                tilNumberOfRadioOwned.editText?.setText(radioOwned)
                tilNumberOfStoveOrOvenOwned.editText?.setText(stoveOrOvenOwned)
                tilNumberOfRickshawOwned.editText?.setText(rickShawOwned)
                tilNumberOfSolarPlateOwned.editText?.setText(solarPlateOwned)
                tilNumberOfSewingMachineOwned.editText?.setText(sewingMachineOwned)
                tilNumberOfMattressOwned.editText?.setText(mattressOwned)
                tilNumberOfTelevisionOwned.editText?.setText(televisionOwned)
                tilNumberOfTableOwned.editText?.setText(tableOwned)
                tilNumberOfSofaOwned.editText?.setText(sofaOwned)
                tilNumberOfHandsetOrPhoneOwned.editText?.setText(handsetOrPhoneOwned)
                tilNumberOfElectricIronOwned.editText?.setText(electricIronOwned)
                tilNumberOfPlowOwned.editText?.setText(plowOwned)
                tilNumberOfGoatOwned.editText?.setText(goatOwned)
                tilNumberOfFridgeOwned.editText?.setText(fridgeOwned)
                tilNumberOfDvdDriverOwned.editText?.setText(dvdDriverOwned)
                tilNumberOfPigsOwned.editText?.setText(pigsOwned)
                tilNumberOfCanalTntTvCableOwned.editText?.setText(cableTVOwned)
                tilNumberOfCowOwned.editText?.setText(cowOwned)
                tilNumberOfCharcoalIronOwned.editText?.setText(charcoalIron)
                tilNumberOfComputerOwned.editText?.setText(computerOwned)
                tilNumberOfChairOwned.editText?.setText(chairOwned)
                tilNumberOfCartOwned.editText?.setText(cartsOwned)
                tilNumberOfCarsOwned.editText?.setText(carsOwned)
                tilNumberOfBedOwned.editText?.setText(bedOwned)
                tilNumberOfAirConditionerOwned.editText?.setText(airConditionerOwned)
                tilAmountOfCultivableLandOwned.editText?.setText(cultivatedLandOwned)
                tilNumberOfFanOwned.editText?.setText(fanOwned)


            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepSevenFragment)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepFiveFragment, bundle)

            }


        }

    }


}