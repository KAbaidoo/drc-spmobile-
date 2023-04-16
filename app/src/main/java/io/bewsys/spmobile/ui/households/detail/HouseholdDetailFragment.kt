package io.bewsys.spmobile.ui.households.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentAddHouseholdSevenReview1Binding
import io.bewsys.spmobile.databinding.FragmentHouseholdDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HouseholdDetailFragment : Fragment(R.layout.fragment_household_detail) {
    val viewModel: HouseholdDetailViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHouseholdDetailBinding.bind(view)



        binding.apply {
            viewModel.apply {

                tvInitialRegistrationType.append(": $initialRegistrationType")
                tvRespondentFirstname.append(": $respondentFirstName")
                tvRespondentMiddlename.append(": $respondentMiddleName")
                tvRespondentLastname.append(": $respondentLastName")
                tvFamilyBondToHead.append(": $respondentFamilyBondToHead")
                tvRespondentAge.append(": $respondentAge")
                tvRespondentDob.append(": $respondentDOB")
                tvRespondentSex.append(": $respondentSex")
                tvRespondentVoterId.append(": $respondentVoterId")
                tvRespondentPhoneNumber.append(": $respondentPhoneNo")
                tvProvince.append(": $province")
                tvTerritory.append(": $territory")
                tvCommunity.append(": $community")
                tvGroupment.append(": $groupment")
                tvAddress.append(": $address")
                tvVillageOrQuartier.append(": $villageOrQuartier")
                tvTerritoryOrTown.append(": $territoryOrTown")
                tvAreaOfResidence.append(": $respondentFirstName")
                tvGpsLon.append(": $lon")
                tvGpsLat.append(": $lat")
                tvIsHeadRespondent.append(": $headIsRespondent")
                tvHeadFirstName.append(": $headFirstName")
                tvHeadMiddleName.append(": $headMiddleName")
                tvHeadLastName.append(": $headLastName")
                tvHeadDobKnown.append(": $headDOBKnown")
                tvHeadDob.append(": $headDOB")
                tvHeadAgeKnown.append(": $headAgeKnown")
                tvHeadAge.append(": $headAge")
                tvHeadSex.append(": $headSex")
                tvHeadPregnancyStatus.append(": $headPregnancyStatus")
                tvMaritalStatus.append(": $headMaritalStatus")
                tvHeadVoterId.append(": $headVoterId")
                tvHeadPhoneNumber.append(": $headPhoneNo")
                tvHeadBirthCertificate.append(": $headBirthCert")
                tvHeadEducationalLevel.append(": $headEduLevel")
                tvHeadSocioProfessionalCategory.append(": $headSocioProfessionalCategory")
                tvHeadSchoolAttendence.append(": $headSchoolAttendance")
                tvHeadSectorOfWork.append(": $headSectorOfWork")
                tvHeadDisability.append(": $headDisability")
                tvIsIncomeRegular.append(": $isIncomeRegular")
                tvBankAccountOrBankCard.append(": $bankCardOrAccount")
                tvBenefitFromSocialAssistanceProgram.append(": $benefitFromSocialAssistanceProgram")
                tvSocialAssistanceProgram.append(": $nameOfSocialAssistanceProgram")
                tvMigrationStatus.append(": $migrationStatus")
                val otherMigration =
                    if (otherMigrationStatus.isBlank()) "N/A" else otherMigrationStatus
                tvOtherMigrationStatus.append(": $otherMigration")
                val duration =
                    if (durationDisplacedReturnedRepatriatedRefugee.isBlank()) "N/A" else "$durationDisplacedReturnedRepatriatedRefugee $unitOfMigrationDuration"
                tvDurationDisplacedReturnedRepatriatedRefugee.append(": $duration")
                tvMonthlyIncome.append(": $householdMonthlyIncome")
                tvMonthlyIncomeNeeded.append(": $minimumMonthlyIncomeNecessaryLiveWithoutDifficulties")
                tvMobileMoneyUsername.append(": $mobileMoneyUsername")
                tvMobileMoneyNumber.append(": $mobileMoneyPhoneNumber")
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
        }
    }
}


