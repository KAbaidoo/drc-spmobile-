package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
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
import com.google.android.material.snackbar.Snackbar
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdEightReview2Binding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FormStepEightFragment : Fragment(R.layout.fragment_add_household_eight_review2) {
    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentAddHouseholdEightReview2Binding.bind(view)

        binding.apply {
            viewModel.apply {
                tvAffectedByConflict.append(": $affectedByConflict")
                tvAffectedByEpidemic.append(": $affectedByEpidemic")
                tvAffectedByClimateShock.append(": $affectedByClimateShock")
                tvAffectedByOtherShock.append(": $affectedByOtherShock")
                tvTakeChildrenOutOfSchool.append(": $takeChildrenOutOfSchool")
                tvUseOfChildLabor.append(": $useOfChildLabour")
                tvUseOfEarlyMarriage.append(": $useOfEarlyMarriage")
                tvGiveUpHealthCare.append(": $gaveUpHealthCare")
                tvSaleOfProductionAssets.append(": $saleOfProductionAssets")
                tvDaysSpentReduceMealsConsumedCopingStrategy.append(": $daysReducedMealsConsumed")
                tvDaysSpentReduceMealsAdultForfeitMealForChildCopingStrategy.append(": $daysReducedMealsAdult")
                tvDaysSpentReduceAmountConsumedCopingStrategy.append(": $daysReducedAmountConsumed")
                tvDaysSpentEatLessExpensivelyCopingStrategy.append(": $daysEatLessExpensively")
                tvDaysSpentDaysWithoutEatingCopingStrategy.append(": $daysWithoutEating")
                tvDaysSpentConsumeWildFoodCopingStrategy.append(": $daysConsumedWildFood")
                tvDaysSpentBorrowFoodOrRelyOnFamilyHelpCopingStrategy.append(": $daysBorrowFood")
                tvDaysSpentBeggingCopingStrategy.append(": $daysBeggingFood")
                tvDaysSpentOtherCopingStrategy.append(": $daysOtherCoping")
                tvNumberOfMealsEatenByChildren6To17Yesterday.append(": $numberOfMealsChildren6To17")
                tvNumberOfMealsEatenByChildren2To5Yesterday.append(": $numberOfMealsChildren2To5")
                tvNumberOfMealsEatenByAdults18PlusYesterday.append(": $numberOfMealsAdults18plus")
                tvNumberOfDaysInWeekConsumedSugarOrSweetProducts.append(": $daysConsumedSugarOrSweets")
                tvNumberOfDaysInWeekConsumedVegetables.append(": $daysConsumedVegetables")
                tvNumberOfDaysInWeekConsumedStapleFoods.append(": $daysConsumedStapleFoods")
                tvNumberOfDaysInWeekConsumedMeat.append(": $daysConsumedMeat")
                tvNumberOfDaysInWeekConsumedLegumesOrNuts.append(": $daysConsumedLegumes")
                tvNumberOfDaysInWeekConsumedFruits.append(": $daysConsumedFruits")
                tvNumberOfDaysInWeekConsumedDairyProducts.append(": $daysConsumedDiary")
                tvNumberOfDaysInWeekConsumedCookingOils.append(": $daysConsumedCookingOils")
                tvHasLivestock.append(": $hasLiveStock")
                tvHasHouseholdGoods.append(": $hasHouseholdGoods")
                tvAccessToCultivableLand.append(": $cultivatedLandOwned")
                tvPracticeOfCashCropFarmingOrCommercialFarming.append(": $cashCropOrCommercialFarming")
                tvOwnerOfCultivableLand.append(": $ownerOfCultivableLand")
                tvAmountOfCultivableLandOwned.append(": $cultivatedLandOwned")
                tvNumberOfGuineaPigOwned.append(": $guineaPigsOwned")
                tvNumberOfPoultryOwned.append(": $poultryOwned")
                tvNumberOfSheepOwned.append(": $sheepOwned")
                tvNumberOfRabbitOwned.append(": $rabbitOwned")
                tvNumberOfGoatOwned.append(": $goatOwned")
                tvNumberOfPigsOwned.append(": $pigsOwned")
                tvNumberOfCowOwned.append(": $cowOwned")
                tvNumberOfBicycleOwned.append(": $bicycleOwned")
                tvNumberOfOilStoveOwned.append(": $oilStoveOwned")
                tvNumberOfMosquitoNetsOwned.append(": $mosquitoNets")
                tvNumberOfMotorbikeOwned.append(": $motorbikeOwned")
                tvNumberOfRoomsUsedForSleeping.append(": $roomsUsedForSleeping")
                tvNumberOfWheelbarrowOwned.append(": $wheelBarrowOwned")
                tvNumberOfRadioOwned.append(": $radioOwned")
                tvNumberOfStoveOrOvenOwned.append(": $stoveOrOvenOwned")
                tvNumberOfRickshawOwned.append(": $rickShawOwned")
                tvNumberOfSolarPlateOwned.append(": $solarPlateOwned")
                tvNumberOfSewingMachineOwned.append(": $sewingMachineOwned")
                tvNumberOfMattressOwned.append(": $mattressOwned")
                tvNumberOfTelevisionOwned.append(": $televisionOwned")
                tvNumberOfTableOwned.append(": $tableOwned")
                tvNumberOfSofaOwned.append(": $sofaOwned")
                tvNumberOfHandsetOrPhoneOwned.append(": $handsetOrPhoneOwned")
                tvNumberOfElectricIronOwned.append(": $electricIronOwned")
                tvNumberOfPlowOwned.append(": $plowOwned")
                tvNumberOfFridgeOwned.append(": $fridgeOwned")
                tvNumberOfDvdDriverOwned.append(": $dvdDriverOwned")
                tvNumberOfFanOwned.append(": $fanOwned")
                tvNumberOfCanalTntTvCableOwned.append(": $cableTVOwned")
                tvNumberOfCharcoalIronOwned.append(": $charcoalIron")
                tvNumberOfComputerOwned.append(": $computerOwned")
                tvNumberOfChairOwned.append(": $chairOwned")
                tvNumberOfCartOwned.append(": $cartsOwned")
                tvNumberOfCarsOwned.append(": $carsOwned")
                tvNumberOfBedOwned.append(": $bedOwned")
                tvNumberOfAirConditionerOwned.append(": $airConditionerOwned")
            }

            btnNext.setOnClickListener {
                viewModel.onRegisterClicked()
            }
            val title = if (viewModel.household != null) getString(R.string.edit_household) else getString(R.string.add_household)
            btnPrevious.setOnClickListener {
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.formStepSevenFragment )
            }




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



        }
        // set up menu
      /*  val menuHost = requireActivity()
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
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)*/

    }


}