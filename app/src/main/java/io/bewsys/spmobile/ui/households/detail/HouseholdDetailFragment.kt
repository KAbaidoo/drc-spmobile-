package io.bewsys.spmobile.ui.households.detail

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.bewsys.spmobile.FormNavigationArgs
import io.bewsys.spmobile.R
import io.bewsys.spmobile.databinding.FragmentHouseholdDetailBinding
import io.bewsys.spmobile.util.exhaustive
import org.koin.androidx.viewmodel.ext.android.viewModel

class HouseholdDetailFragment : Fragment(R.layout.fragment_household_detail) {
    val viewModel: HouseholdDetailViewModel by viewModel()
    var isOpen: Boolean = false
    lateinit var listener: OnClickListener

    val args: HouseholdDetailFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHouseholdDetailBinding.bind(view)

        hideActions(binding)

        val household = args.household
        viewModel.household = household
        viewModel.id = household?.id


        binding.apply {

            fabEdit.setOnClickListener {
                viewModel.onEditFabClicked()
            }
            fabDeleteAction.setOnClickListener {
                viewModel.onDeleteActionFabClicked()
            }
            textDeleteAction.setOnClickListener {
                viewModel.onDeleteActionFabClicked()
            }
            fabEditAction.setOnClickListener {
                viewModel.onEditActionFabClicked()
            }
            textEditAction.setOnClickListener {
                viewModel.onEditActionFabClicked()
            }


            tvStatus.append(": ${household.remote_id.toString()}")
            tvInitialRegistrationType.append(": ${household.initial_registration_type}")
            tvRespondentFirstname.append(": ${household.respondent_firstname}")
            tvRespondentMiddlename.append(": ${household.respondent_middlename}")
            tvRespondentLastname.append(": ${household.respondent_lastname}")
            tvFamilyBondToHead.append(": ${household.respondent_family_bond_to_head}")
            tvRespondentDob.append(": ${household.respondent_dob}")
            tvRespondentSex.append(": ${household.respondent_sex}")
            tvRespondentVoterId.append(": ${household.respondent_voter_id}")
            tvRespondentPhoneNumber.append(": ${household.respondent_phone_number}")

            tvProvince.append(": ${household.province_name}")
            tvTerritory.append(": ${household.territory_name}")
            tvCommunity.append(": ${household.community_name}")
            tvGroupment.append(": ${household.groupement_name}")
            tvAddress.append(": ${household.address}")
            tvVillageOrQuartier.append(": ${household.village_or_quartier}")
            tvTerritoryOrTown.append(": ${household.territory_or_town}")
            tvAreaOfResidence.append(": ${household.area_of_residence}")
            tvGpsLon.append(": ${household.gps_longitude}")
            tvGpsLat.append(": ${household.gps_latitude}")

            tvIsHeadRespondent.append(": ${household.is_head_respondent}")
            tvHeadFirstName.append(": ${household.household_head_firstname}")
            tvHeadMiddleName.append(": ${household.household_head_middlename}")
            tvHeadLastName.append(": ${household.household_head_lastname}")

            tvHeadDobKnown.append(": ${household.head_dob_known}")
            tvHeadDob.append(": ${household.household_head_dob}")
            tvHeadAgeKnown.append(": ${household.head_age_known}")
            tvHeadAge.append(": ${household.household_head_age}")
            tvHeadSex.append(": ${household.household_head_sex}")
            tvHeadPregnancyStatus.append(": ${household.household_head_pregnancy_status}")

            tvMaritalStatus.append(": ${household.household_head_marital_status_id}")
            tvHeadVoterId.append(": ${household.respondent_voter_id}")
            tvHeadPhoneNumber.append(": ${household.mobile_money_phone_number}")
            tvHeadBirthCertificate.append(": ${household.household_head_birth_certificate}")
            tvHeadEducationalLevel.append(": ${household.household_head_educational_level_id}")
            tvHeadSocioProfessionalCategory.append(": ${household.household_head_socio_professional_category_id}")
            tvHeadSchoolAttendence.append(": ${household.household_head_school_attendance_id}")
            tvHeadSectorOfWork.append(": ${household.household_head_sector_of_work_id}")
            tvHeadDisability.append(": ${household.household_head_disability_id}")
            tvIsIncomeRegular.append(": ${household.is_income_regular}")
            tvBankAccountOrBankCard.append(": ${household.bank_account_or_bank_card_available}")
            tvBenefitFromSocialAssistanceProgram.append(": ${household.name_of_social_assistance_program}")
            tvSocialAssistanceProgram.append(": ${household.name_of_social_assistance_program}")
            tvMigrationStatus.append(": ${household.household_migration_status}")


            tvOtherMigrationStatus.append(": ${household.other_household_migration_status ?: "N/A"}")
           /* val duration =
                if (household.duration_displaced_returned_repatriated_refugee!!.isNullOrBlank()) "N/A" else "${household.duration_displaced_returned_repatriated_refugee} ${household.unit_of_migration_duration}"

            tvDurationDisplacedReturnedRepatriatedRefugee.append(duration)*/
            tvMonthlyIncome.append(": ${household.household_monthly_income}")
            tvMonthlyIncomeNeeded.append(": ${household.minimum_monthly_income_necessary_live_without_difficulties}")
            tvMobileMoneyUsername.append(": ${household.mobile_money_username}")
            tvMobileMoneyNumber.append(": ${household.mobile_money_phone_number}")
            tvAffectedByConflict.append(": ${household.affected_by_conflict}")

            tvAffectedByEpidemic.append(": ${household.affected_by_epidemic}")

            tvAffectedByClimateShock.append(": ${household.affected_by_climate_shock}")

            tvAffectedByOtherShock.append(": ${household.affected_by_other_shock}")

            tvTakeChildrenOutOfSchool.append(": ${household.take_children_out_of_school}")
            tvUseOfChildLabor.append(": ${household.use_of_child_labor}")

            tvUseOfEarlyMarriage.append(": ${household.use_of_early_marriage}")
            tvGiveUpHealthCare.append(": ${household.give_up_health_care}")
            tvSaleOfProductionAssets.append(": ${household.sale_of_production_assets}")
            tvDaysSpentReduceMealsConsumedCopingStrategy.append(": ${household.days_spent_reduce_meals_consumed_coping_strategy}")
            tvDaysSpentReduceMealsAdultForfeitMealForChildCopingStrategy.append(": ${household.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy}")
            tvDaysSpentReduceAmountConsumedCopingStrategy.append(": ${household.days_spent_reduce_amount_consumed_coping_strategy}")
            tvDaysSpentEatLessExpensivelyCopingStrategy.append(": ${household.days_spent_eat_less_expensively_coping_strategy}")
            tvDaysSpentDaysWithoutEatingCopingStrategy.append(": ${household.days_spent_days_without_eating_coping_strategy}")
            tvDaysSpentConsumeWildFoodCopingStrategy.append(": ${household.days_spent_consume_wild_food_coping_strategy}")
            tvDaysSpentBorrowFoodOrRelyOnFamilyHelpCopingStrategy.append(": ${household.days_spent_borrow_food_or_rely_on_family_help_coping_strategy}")
            tvDaysSpentBeggingCopingStrategy.append(": ${household.days_spent_begging_coping_strategy}")
            tvDaysSpentOtherCopingStrategy.append(": ${household.days_spent_other_coping_strategy}")
            tvNumberOfMealsEatenByChildren6To17Yesterday.append(": ${household.number_of_meals_eaten_by_children_6_to_17_yesterday}")
            tvNumberOfMealsEatenByChildren2To5Yesterday.append(": ${household.number_of_meals_eaten_by_children_2_to_5_yesterday}")
            tvNumberOfMealsEatenByAdults18PlusYesterday.append(": ${household.number_of_meals_eaten_by_adults_18_plus_yesterday}")
            tvNumberOfDaysInWeekConsumedSugarOrSweetProducts.append(": ${household.number_of_days_in_week_consumed_sugar_or_sweet_products}")
            tvNumberOfDaysInWeekConsumedVegetables.append(": ${household.number_of_days_in_week_consumed_vegetables}")
            tvNumberOfDaysInWeekConsumedStapleFoods.append(": ${household.number_of_days_in_week_consumed_staple_foods}")
            tvNumberOfDaysInWeekConsumedMeat.append(": ${household.number_of_days_in_week_consumed_meat}")
            tvNumberOfDaysInWeekConsumedLegumesOrNuts.append(": ${household.number_of_days_in_week_consumed_legumes_or_nuts}")
            tvNumberOfDaysInWeekConsumedFruits.append(": ${household.number_of_days_in_week_consumed_fruits}")
            tvNumberOfDaysInWeekConsumedDairyProducts.append(": ${household.number_of_days_in_week_consumed_dairy_products}")
            tvNumberOfDaysInWeekConsumedCookingOils.append(": ${household.number_of_days_in_week_consumed_cooking_oils}")
            tvHasLivestock.append(": ${household.has_livestock}")
            tvHasHouseholdGoods.append(": ${household.has_household_goods}")
            tvAccessToCultivableLand.append(": ${household.household_member_access_to_cultivable_land}")
            tvPracticeOfCashCropFarmingOrCommercialFarming.append(": ${household.practice_of_cash_crop_farming_or_commercial_farming}")
            tvOwnerOfCultivableLand.append(": ${household.household_member_owner_of_cultivable_land}")
            tvAmountOfCultivableLandOwned.append(": ${household.amount_of_cultivable_land_owned}")
            tvNumberOfGuineaPigOwned.append(": ${household.number_of_pigs_owned}")
            tvNumberOfPoultryOwned.append(": ${household.number_of_poultry_owned}")

            tvNumberOfSheepOwned.append(": ${household.number_of_sheep_owned}")
            tvNumberOfRabbitOwned.append(": ${household.number_of_rabbit_owned}")
            tvNumberOfGoatOwned.append(": ${household.number_of_goat_owned}")
            tvNumberOfPigsOwned.append(": ${household.number_of_pigs_owned}")
            tvNumberOfCowOwned.append(": ${household.number_of_cow_owned}")
            tvNumberOfBicycleOwned.append(": ${household.number_of_bicycle_owned}")
            tvNumberOfOilStoveOwned.append(": ${household.number_of_oil_stove_owned}")
            tvNumberOfMosquitoNetsOwned.append(": ${household.number_of_mosquito_nets_owned}")
            tvNumberOfMotorbikeOwned.append(": ${household.number_of_motorbike_owned}")
            tvNumberOfRoomsUsedForSleeping.append(": ${household.number_of_rooms_used_for_sleeping}")
            tvNumberOfWheelbarrowOwned.append(": ${household.number_of_wheelbarrow_owned}")
            tvNumberOfRadioOwned.append(": ${household.number_of_radio_owned}")
            tvNumberOfStoveOrOvenOwned.append(": ${household.number_of_stove_or_oven_owned}")
            tvNumberOfRickshawOwned.append(": ${household.number_of_rickshaw_owned}")
            tvNumberOfSolarPlateOwned.append(": ${household.number_of_solar_plate_owned}")
            tvNumberOfSewingMachineOwned.append(": ${household.number_of_sewing_machine_owned}")
            tvNumberOfMattressOwned.append(": ${household.number_of_mattress_owned}")
            tvNumberOfTelevisionOwned.append(": ${household.number_of_television_owned}")
            tvNumberOfTableOwned.append(": ${household.number_of_table_owned}")
            tvNumberOfSofaOwned.append(": ${household.number_of_sofa_owned}")
            tvNumberOfHandsetOrPhoneOwned.append(": ${household.number_of_handset_or_phone_owned}")
            tvNumberOfElectricIronOwned.append(": ${household.number_of_electric_iron_owned}")
            tvNumberOfPlowOwned.append(": ${household.number_of_plow_owned}")
            tvNumberOfFridgeOwned.append(": ${household.number_of_fridge_owned}")
            tvNumberOfDvdDriverOwned.append(": ${household.number_of_dvd_driver_owned}")
            tvNumberOfFanOwned.append(": ${household.number_of_fan_owned}")
            tvNumberOfCanalTntTvCableOwned.append(": ${household.number_of_canal_tnt_tv_cable_owned}")
            tvNumberOfCharcoalIronOwned.append(": ${household.number_of_charcoal_iron_owned}")
            tvNumberOfComputerOwned.append(": ${household.number_of_computer_owned}")
            tvNumberOfChairOwned.append(": ${household.number_of_chair_owned}")
            tvNumberOfCartOwned.append(": ${household.number_of_cart_owned}")
            tvNumberOfCarsOwned.append(": ${household.number_of_cars_owned}")
            tvNumberOfBedOwned.append(": ${household.number_of_bed_owned}")
            tvNumberOfAirConditionerOwned.append(": ${household.number_of_air_conditioner_owned}")

        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.detailChannel.collect { event ->
                when (event) {
                    is HouseholdDetailViewModel.DetailEvent.FabClicked -> {
                        if (!isOpen) showActions(binding) else hideActions(binding)
                    }
                    is HouseholdDetailViewModel.DetailEvent.FabDeleteActionClicked -> {
                        val bundle = bundleOf("id" to event.id)
                        findNavController().navigate(R.id.deleteHouseholdDialogFragment, bundle)
                    }
                    is HouseholdDetailViewModel.DetailEvent.FabEditActionClicked -> {
                        val action =
                            HouseholdDetailFragmentDirections.actionHouseholdDetailFragmentToFormNavigation(
                                getString(R.string.edit_household),
                                event.householdModel
                            )
                        findNavController().navigate(action)
                    }
                }.exhaustive
            }
        }
    }// end of onViewCreated

    private fun showActions(binding: FragmentHouseholdDetailBinding) {
        isOpen = true
        binding.fabEdit.setImageResource(R.drawable.fab_close_24)
        binding.fabDeleteAction.show()
        binding.fabEditAction.show()
        binding.textEditAction.visibility = View.VISIBLE
        binding.textDeleteAction.visibility = View.VISIBLE
        binding.textEditAction.visibility = View.VISIBLE
    }

    private fun hideActions(binding: FragmentHouseholdDetailBinding) {
        isOpen = false
        binding.fabEdit.setImageResource(R.drawable.ic_edit_24)
        binding.fabDeleteAction.hide()
        binding.fabEditAction.hide()
        binding.textEdit.visibility = View.GONE
        binding.textDeleteAction.visibility = View.GONE
        binding.textEditAction.visibility = View.GONE

    }
}


