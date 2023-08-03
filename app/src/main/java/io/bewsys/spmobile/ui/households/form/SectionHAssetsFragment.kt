package io.bewsys.spmobile.ui.households.form

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHouseholdAssetsHBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel

class SectionHAssetsFragment : Fragment(R.layout.fragment_household_assets_h) {


    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)

    private var _binding: FragmentHouseholdAssetsHBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentHouseholdAssetsHBinding.bind(view)


        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.SectionHHasBlank.collectLatest {
                    btnNext.isEnabled = it.not()
                }
            }

            tvOwnerOfCultivableLand.isVisible = false
            rgOwnerOfCultivableLand.isVisible = false
            rbYesOwnerOfCultivableLand.isVisible = false
            rbNoOwnerOfCultivableLand.isVisible = false
            tvAmountOfCultivableLandOwned.isVisible = false
            tilAmountOfCultivableLandOwned.isVisible = false

            rgAccessToCultivableLand.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesAccessToCultivableLand.id -> {
                        viewModel.accessToCultivableLand =
                            rbYesAccessToCultivableLand.text as String

                        tvOwnerOfCultivableLand.isVisible = true
                        rgOwnerOfCultivableLand.isVisible = true
                        rbYesOwnerOfCultivableLand.isVisible = true
                        rbNoOwnerOfCultivableLand.isVisible = true
                        tvAmountOfCultivableLandOwned.isVisible = true
                        tilAmountOfCultivableLandOwned.isVisible = true

                        viewModel.sectionHHasBlankFields()

                    }
                    else -> {
                        rgOwnerOfCultivableLand.clearCheck()
                        viewModel.accessToCultivableLand = rbNoAccessToCultivableLand.text as String

                        tvOwnerOfCultivableLand.isVisible = false
                        rgOwnerOfCultivableLand.isVisible = false
                        rbYesOwnerOfCultivableLand.isVisible = false
                        rbNoOwnerOfCultivableLand.isVisible = false
                        tvAmountOfCultivableLandOwned.isVisible = false
                        tilAmountOfCultivableLandOwned.isVisible = false

                        viewModel.ownerOfCultivableLand = ""

                        viewModel.sectionHHasBlankFields()

                    }
                }

            }
            rgOwnerOfCultivableLand.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesOwnerOfCultivableLand.id -> {
                        viewModel.ownerOfCultivableLand = rbYesOwnerOfCultivableLand.text as String
                        viewModel.sectionHHasBlankFields()
                    }
                    else -> {

                        viewModel.ownerOfCultivableLand = rbNoOwnerOfCultivableLand.text as String
                        viewModel.sectionHHasBlankFields()

                    }
                }

            }
            rgPracticeOfCashCropFarmingOrCommercialFarming.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesPracticeOfCashCropFarmingOrCommercialFarming.id -> {
                        viewModel.cashCropOrCommercialFarming =
                            rbYesPracticeOfCashCropFarmingOrCommercialFarming.text as String

                        viewModel.sectionHHasBlankFields()
                    }
                    else -> {
                        viewModel.cashCropOrCommercialFarming =
                            rbNoPracticeOfCashCropFarmingOrCommercialFarming.text as String
                        viewModel.sectionHHasBlankFields()
                    }
                }
            }

            val tilLivestock = listOf(
                tilNumberOfGoatOwned,
                tilNumberOfSheepOwned,
                tilNumberOfCowOwned,
                tilNumberOfRabbitOwned,
                tilNumberOfPigsOwned,
                tilNumberOfPoultryOwned,
                tilNumberOfGuineaPigOwned,
                tilOtherLivestockQty

            )

            gridLayout.children.forEach {
                (it as View).visibility = View.GONE
            }
            tvLivestockIndicateQty.visibility =View.GONE

            tvOtherLivestock.visibility = View.GONE
            tilOtherLivestock.visibility = View.GONE
            rgHasLivestock.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasLivestock.id -> {
                        viewModel.hasLiveStock = rbYesHasLivestock.text as String
                        viewModel.sectionHHasBlankFields()

                        gridLayout.children.forEach {
                            it.visibility = View.VISIBLE
                        }
                        tvOtherLivestock.visibility = View.VISIBLE
                        tilOtherLivestock.visibility = View.VISIBLE
                        tvLivestockIndicateQty.visibility =View.VISIBLE
                    }
                    else -> {
                        tilLivestock.forEach { til ->
                            til.editText?.setText("")
                        }
                        tilOtherLivestock.editText?.setText("")
                        viewModel.hasLiveStock = rbNoHasLivestock.text as String
                        viewModel.sectionHHasBlankFields()

                        gridLayout.children.forEach {
                            it.visibility =  View.GONE
                        }
                        tvOtherLivestock.visibility = View.GONE
                        tilOtherLivestock.visibility = View.GONE
                        tvLivestockIndicateQty.visibility =View.GONE

                    }
                }
            }

            val tilHouseholdGoods = listOf(
                tilCar,
                tilMotorcycle,
                tilCart,
                tilPlow,
                tilWheelBarrow,
                tilRickshaw,
                tilMattress,
                tilAircon,
                tilFan,
                tilRadio,
                tilTelevision,
                tilCableTv,
                tilDvdPlayer,
                tilMobilePhone,
                tilComputer,
                tilStoveOrOven,
                tilOilStove,
                tilSunPlate,
                tilSewingMachine,
                tilElectricIron,
                tilCoalIron,
                tilBed,
                tilTable,
                tilChair,
                tilCanoe,
                tilMosquitoNet,
                tilRefrigerator,
                tilBicycle
            )
            gridLayout2.children.forEach {
                it.visibility = View.GONE
            }
            tvNumberOfHouseholdGoods.visibility = View.GONE
            rgHasHouseholdGoods.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYesHasHouseholdGoods.id -> {
                        viewModel.hasHouseholdGoods = rbYesHasHouseholdGoods.text as String
                        gridLayout2.children.forEach {
                            it.visibility = View.VISIBLE
                        }
                        tvNumberOfHouseholdGoods.visibility = View.VISIBLE
                        viewModel.sectionHHasBlankFields()
                    }
                    else -> {
                        tilHouseholdGoods.forEach { til ->
                            til.editText?.setText("")
                        }
                        viewModel.hasHouseholdGoods = rbNoHasHouseholdGoods.text as String
                        viewModel.sectionHHasBlankFields()
                        gridLayout2.children.forEach {
                            it.visibility = View.GONE
                        }
                        tvNumberOfHouseholdGoods.visibility = View.GONE
                    }
                }
            }

            tilOtherLivestockQty.editText?.addTextChangedListener {
                viewModel.apply {
                    otherLivestockQty = it.toString()
                    sectionHHasBlankFields()
                }
            }

            tilAmountOfCultivableLandOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    cultivatedLandOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }


            tilNumberOfGoatOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    goatOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfSheepOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    sheepOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfCowOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    cowOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfRabbitOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    rabbitOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfPigsOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    pigsOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfPoultryOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    poultryOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilNumberOfGuineaPigOwned.editText?.addTextChangedListener {
                viewModel.apply {
                    guineaPigsOwned = it.toString()
                    sectionHHasBlankFields()
                }
            }
            tilOtherLivestock.editText?.addTextChangedListener {
                viewModel.apply {
                    otherLivestockQty = it.toString()
                    sectionHHasBlankFields()
                }
            }

            tilCar.editText?.addTextChangedListener {
                viewModel.apply {
                    carsOwned = it.toString()
                }
            }
            tilMotorcycle.editText?.addTextChangedListener {
                viewModel.apply {
                    motorbikeOwned = it.toString()
                }
            }
            tilCart.editText?.addTextChangedListener {
                viewModel.apply {
                    cartsOwned = it.toString()
                }
            }
            tilPlow.editText?.addTextChangedListener {
                viewModel.apply {
                    plowOwned = it.toString()
                }
            }
            tilWheelBarrow.editText?.addTextChangedListener {
                viewModel.apply {
                    wheelBarrowOwned = it.toString()
                }
            }
            tilRickshaw.editText?.addTextChangedListener {
                viewModel.apply {
                    rickShawOwned = it.toString()
                }
            }
            tilMattress.editText?.addTextChangedListener {
                viewModel.apply {
                    mattressOwned = it.toString()
                }
            }
            tilAircon.editText?.addTextChangedListener {
                viewModel.apply {
                    airConditionerOwned = it.toString()
                }
            }
            tilFan.editText?.addTextChangedListener {
                viewModel.apply {
                    fanOwned = it.toString()
                }
            }
            tilRadio.editText?.addTextChangedListener {
                viewModel.apply {
                    radioOwned = it.toString()
                }
            }
            tilBicycle.editText?.addTextChangedListener {
                viewModel.apply {
                    bicycleOwned = it.toString()
                }
            }
            tilTelevision.editText?.addTextChangedListener {
                viewModel.apply {
                    televisionOwned = it.toString()
                }
            }
            tilCableTv.editText?.addTextChangedListener {
                viewModel.apply {
                    cableTVOwned = it.toString()
                }
            }
            tilDvdPlayer.editText?.addTextChangedListener {
                viewModel.apply {
                    dvdDriverOwned = it.toString()
                }
            }
            tilMobilePhone.editText?.addTextChangedListener {
                viewModel.apply {
                    handsetOrPhoneOwned = it.toString()
                }
            }
            tilComputer.editText?.addTextChangedListener {
                viewModel.apply {
                    computerOwned = it.toString()
                }
            }
            tilStoveOrOven.editText?.addTextChangedListener {
                viewModel.apply {
                    stoveOrOvenOwned = it.toString()
                }
            }
            tilOilStove.editText?.addTextChangedListener {
                viewModel.apply {
                    oilStoveOwned = it.toString()
                }
            }
            tilSunPlate.editText?.addTextChangedListener {
                viewModel.apply {
                    solarPlateOwned = it.toString()
                }
            }
            tilSewingMachine.editText?.addTextChangedListener {
                viewModel.apply {
                    sewingMachineOwned = it.toString()
                }
            }
            tilElectricIron.editText?.addTextChangedListener {
                viewModel.apply {
                    electricIronOwned = it.toString()
                }
            }
            tilCoalIron.editText?.addTextChangedListener {
                viewModel.apply {
                    charcoalIron = it.toString()
                }
            }
            tilBed.editText?.addTextChangedListener {
                viewModel.apply {
                    bedOwned = it.toString()
                }
            }
            tilTable.editText?.addTextChangedListener {
                viewModel.apply {
                    tableOwned = it.toString()
                }
            }
            tilChair.editText?.addTextChangedListener {
                viewModel.apply {
                    chairOwned = it.toString()
                }
            }
            tilCanoe.editText?.addTextChangedListener {
                viewModel.apply {
                    canoeOwned = it.toString()
                }
            }
            tilMosquitoNet.editText?.addTextChangedListener {
                viewModel.apply {
                    mosquitoNets = it.toString()
                }
            }
            tilRefrigerator.editText?.addTextChangedListener {
                viewModel.apply {
                    fridgeOwned = it.toString()

                }
            }


            val title =
                if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)

            btnNext.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionIVulnerabilityFragment, bundle)
            }
            btnPrevious.setOnClickListener {

                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionGIncomeFragment, bundle)

            }

            viewModel.apply {
                tilAmountOfCultivableLandOwned.editText?.setText(cultivatedLandOwned)
                tilNumberOfGoatOwned.editText?.setText(goatOwned)
                tilNumberOfSheepOwned.editText?.setText(sheepOwned)
                tilNumberOfCowOwned.editText?.setText(cowOwned)
                tilNumberOfRabbitOwned.editText?.setText(rabbitOwned)
                tilNumberOfPigsOwned.editText?.setText(pigsOwned)
                tilNumberOfPoultryOwned.editText?.setText(poultryOwned)
                tilNumberOfGuineaPigOwned.editText?.setText(guineaPigsOwned)
                tilOtherLivestock.editText?.setText(otherLivestockQty)

                tilCar.editText?.setText(carsOwned)
                tilBicycle.editText?.setText(bicycleOwned)
                tilMotorcycle.editText?.setText(motorbikeOwned)
                tilCart.editText?.setText(cartsOwned)
                tilPlow.editText?.setText(plowOwned)
                tilWheelBarrow.editText?.setText(wheelBarrowOwned)
                tilRickshaw.editText?.setText(rickShawOwned)
                tilMattress.editText?.setText(mattressOwned)
                tilAircon.editText?.setText(airConditionerOwned)
                tilFan.editText?.setText(fanOwned)
                tilRadio.editText?.setText(radioOwned)
                tilTelevision.editText?.setText(televisionOwned)
                tilCableTv.editText?.setText(cableTVOwned)
                tilDvdPlayer.editText?.setText(dvdDriverOwned)
                tilMobilePhone.editText?.setText(handsetOrPhoneOwned)
                tilComputer.editText?.setText(computerOwned)
                tilStoveOrOven.editText?.setText(stoveOrOvenOwned)
                tilOilStove.editText?.setText(oilStoveOwned)
                tilSunPlate.editText?.setText(solarPlateOwned)
                tilSewingMachine.editText?.setText(sewingMachineOwned)
                tilElectricIron.editText?.setText(electricIronOwned)
                tilCoalIron.editText?.setText(charcoalIron)
                tilBed.editText?.setText(bedOwned)
                tilTable.editText?.setText(tableOwned)
                tilChair.editText?.setText(chairOwned)
                tilCanoe.editText?.setText(canoeOwned)
                tilMosquitoNet.editText?.setText(mosquitoNets)
                tilRefrigerator.editText?.setText(fridgeOwned)


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
            when (viewModel.hasLiveStock) {
                rbYesHasLivestock.text -> rgHasLivestock.check(rbYesHasLivestock.id)
                rbNoHasLivestock.text -> rgHasLivestock.check(rbNoHasLivestock.id)
            }
            when (viewModel.hasHouseholdGoods) {
                rbYesHasHouseholdGoods.text -> rgHasHouseholdGoods.check(rbYesHasHouseholdGoods.id)
                rbNoHasHouseholdGoods.text -> rgHasHouseholdGoods.check(rbNoHasHouseholdGoods.id)
            }

        } //end of apply block

        viewModel.sectionHHasBlankFields()
    }   //end of onCreate


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}