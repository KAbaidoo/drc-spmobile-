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

            tvSurveyNo.append(": ${household.survey_no}")
            tvAreaOfResidence.append(": ${household.area_of_residence}")
            tvProvince.append(": ${household.province_name}")
            tvTerritory.append(": ${household.territory_name}")
            tvCommunity.append(": ${household.community_name}")
            tvGroupment.append(": ${household.groupement_name}")
            tvVillageOrQuartier.append(": ${household.village_or_quartier}")
            tvAddress.append(": ${household.address}")
            tvCac.append(": ${household.address}")
            tvGpsLon.append(": ${household.gps_longitude}")
            tvGpsLat.append(": ${household.gps_latitude}")


//            tvStatus.append(": ${if (household.status.isNullOrEmpty()) "not submitted" else household.status}")
            tvRespondentFirstname.append(": ${household.respondent_firstname}")
            tvRespondentMiddlename.append(": ${household.respondent_middlename}")
            tvRespondentLastname.append(": ${household.respondent_lastname}")
            tvFamilyBondToHead.append(": ${household.respondent_family_bond_to_head}")
            tvRespondentVoterId.append(": ${household.respondent_voter_id}")
            tvRespondentPhoneNumber.append(": ${household.respondent_phone_number}")
            tvHeadFirstName.append(": ${household.household_head_firstname}")
            tvHeadMiddleName.append(": ${household.household_head_middlename}")
            tvHeadLastName.append(": ${household.household_head_lastname}")
            tvHeadAge.append(": ${household.household_head_age}")
            tvHeadSex.append(": ${household.household_head_sex}")
            tvHeadVoterId.append(": ${household.respondent_voter_id}")
            tvHeadPhoneNumber.append(": ${household.mobile_money_phone_number}")

            tvMigrationStatus.append(": ${household.household_migration_status}")
            val duration =
                if (household.duration_displaced_returned_repatriated_refugee!!.isBlank()) "N/A" else "${household.duration_displaced_returned_repatriated_refugee} ${household.unit_of_migration_duration}"
            tvOtherOccupancyStatus.append(": ${household.occupation_status_of_current_accommodation}")
            tvSocialAssistanceProgram.append(": ${household.name_of_social_assistance_program}")

            tvDurationDisplacedReturnedRepatriatedRefugee.append(duration)

            tvExteriorWalls.append(": ${household.main_material_of_exterior_walls}")
            tvSoilMaterial.append(": ${household.main_soil_material}")
            tvNumberOfRoomsUsedForSleeping.append(": ${household.number_of_rooms_used_for_sleeping}")
            tvFuelForCooking.append(": ${household.type_of_fuel_used_for_household_cooking}")


            tvSourceOfDrinkingWater.append(": ${household.main_source_of_household_drinking_water}")
            tvTypeOfToilet.append(": ${household.type_of_household_toilet}")
            tvWasteDisposal.append(": ${household.method_of_waste_disposal}")
            tvWasteDisposal.append(": ${household.place_to_wash_hands}")

            tvIsIncomeRegular.append(": ${household.is_income_regular}")
            tvBankAccountOrBankCard.append(": ${household.bank_account_or_bank_card_available}")
            tvMonthlyIncome.append(": ${household.household_monthly_income}")
            tvMonthlyIncomeNeeded.append(": ${household.minimum_monthly_income_necessary_live_without_difficulties}")
            tvMobileMoneyUsername.append(": ${household.mobile_money_username}")
            tvMobileMoneyNumber.append(": ${household.mobile_money_phone_number}")

            tvAccessToCultivableLand.append(": ${household.household_member_access_to_cultivable_land}")
            tvPracticeOfCashCropFarmingOrCommercialFarming.append(": ${household.practice_of_cash_crop_farming_or_commercial_farming}")
            tvHasLivestock.append(": ${household.has_livestock}")
            tvHasHouseholdGoods.append(": ${household.has_household_goods}")

            tvConflict.append(": ${household.affected_by_conflict}")
            tvEpidemic.append(": ${household.affected_by_epidemic}")
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


