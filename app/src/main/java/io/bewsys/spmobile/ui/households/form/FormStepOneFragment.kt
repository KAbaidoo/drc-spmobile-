package io.bewsys.spmobile.ui.households.form

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf


import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.bewsys.spmobile.FormNavigationArgs
import io.bewsys.spmobile.R
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.databinding.FragmentAddHouseholdOneConsentBinding
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.navigation.koinNavGraphViewModel


class FormStepOneFragment : Fragment(R.layout.fragment_add_household_one_consent) {


    val args: FormNavigationArgs by navArgs()

    private val viewModel: SharedDevelopmentalFormViewModel by koinNavGraphViewModel(R.id.form_navigation)
    private var _binding: FragmentAddHouseholdOneConsentBinding? = null

    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddHouseholdOneConsentBinding.bind(view)


        val household: HouseholdModel? = args.household
        viewModel.household = household

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.yesConsent.collectLatest {
                    btnNext.isEnabled = it == getString(R.string.yes)
                }
            }
        }



        household?.let {
            viewModel.apply {
                id = household?.id
                remoteId = household?.remote_id ?: ""
                status = household?.status ?: ""
                consent = household?.consent ?: ""
                remoteId = household?.remote_id ?: ""
                registrationType = household?.initial_registration_type ?: ""
                respondentFirstName = household?.respondent_firstname ?: ""
                respondentMiddleName = household?.respondent_middlename ?: ""
                respondentLastName = household?.respondent_lastname ?: ""
                respondentSex = household?.respondent_sex ?: ""
                respondentFamilyBondToHead = household?.respondent_family_bond_to_head ?: ""
                respondentDOB = household?.respondent_dob ?: ""
                respondentAgeKnown = household?.respondent_age_known ?: ""
                respondentDOB = household?.respondent_dob_known ?: ""
                respondentAge = household?.respondent_age ?: ""
                respondentSex = household?.respondent_sex ?: ""
                respondentVoterId = household.respondent_voter_id ?: ""
                respondentPhoneNo = household?.respondent_phone_number ?: ""

                address = household.address ?: ""
                cac = household.cac ?: ""


                lon = household.gps_longitude ?: ""
                lat = household.gps_latitude ?: ""


                province = household.province_name ?: ""
                territory = household.territory_name ?: ""
                community = household.community_name ?: ""
                groupment = household.groupement_name ?: ""
                healthArea = household.health_area_name ?: ""
                healthZone = household.health_zone_name ?: ""

                 provinceId = household.province_id.toString()
                 communityId = household.community_id.toString()
                 groupmentId = household.groupment_id.toString()
                 territoryId = household.territory_id.toString()
                 healthAreaId = household.health_area_id.toString()
                 healthZoneId = household.health_zone_id.toString()

                cookingFuel = household.type_of_fuel_used_for_household_cooking ?: ""
                soilMaterial = household.main_soil_material ?: ""
                exteriorWalls = household.main_material_of_exterior_walls ?: ""


                villageOrDistrict = household.village_or_quartier ?: ""
                territoryOrTown = household.territory_or_town ?: ""
                placeOfResidence = household.area_of_residence ?: ""
                hasLiveStock = household.has_livestock ?: ""

                occupancyStatus = household.occupation_status_of_current_accommodation ?: ""

                migrationStatus = household?.household_migration_status ?: ""
                unitOfMigrationDuration = household?.unit_of_migration_duration ?: ""
                otherMigrationStatus = household?.other_household_migration_status ?: ""
                headIsRespondent = household?.is_head_respondent ?: ""
                headFirstName = household?.household_head_firstname ?: ""
                headMiddleName = household?.household_head_middlename ?: ""
                headLastName = household?.household_head_lastname ?: ""
                headAge = household?.household_head_age ?: ""
                headAgeKnown = household?.head_age_known ?: ""
                headDOB = household?.household_head_dob ?: ""
                headDOBKnown = household?.head_dob_known ?: ""
                headSex = household?.household_head_sex ?: ""
                headVoterId = household?.household_head_voter_id_card ?: ""
                headPhoneNo = household?.household_head_phone_number ?: ""
                headBirthCert = household?.household_head_birth_certificate ?: ""
                headEduLevel = household?.household_head_educational_level_id ?: ""
                headSocioProfessionalCategory =
                    household?.household_head_socio_professional_category_id ?: ""
                headSchoolAttendance = household?.household_head_school_attendance_id ?: ""
                headSectorOfWork = household?.household_head_sector_of_work_id ?: ""
                headDisability = household?.household_head_disability_id ?: ""
                headPregnancyStatus = household?.status ?: ""
                headMaritalStatus = household?.household_head_marital_status_id ?: ""

                isIncomeRegular = household?.is_income_regular ?: ""
                bankCardOrAccount = household?.bank_account_or_bank_card_available ?: ""
                benefitFromSocialAssistanceProgram =
                    household?.household_member_with_benefit_from_social_assistance_program ?: ""

                nameOfSocialAssistanceProgram = household?.name_of_social_assistance_program ?: ""
                durationDisplacedReturnedRepatriatedRefugee =
                    household?.duration_displaced_returned_repatriated_refugee ?: ""
                householdMonthlyIncome = household?.household_monthly_income ?: ""
                minimumMonthlyIncomeNecessaryLiveWithoutDifficulties =
                    household?.minimum_monthly_income_necessary_live_without_difficulties ?: ""
                mobileMoneyUsername = household?.mobile_money_username ?: ""
                mobileMoneyPhoneNumber = household?.mobile_money_phone_number ?: ""

                affectedByConflict = household?.affected_by_conflict ?: ""
                affectedByEpidemic = household?.affected_by_epidemic ?: ""
                affectedByClimateShock = household?.affected_by_climate_shock ?: ""
                affectedByOtherShock = household?.affected_by_other_shock ?: ""
                takeChildrenOutOfSchool = household?.take_children_out_of_school ?: ""
                useOfChildLabour = household?.use_of_child_labor ?: ""
                useOfEarlyMarriage = household?.use_of_early_marriage ?: ""
                gaveUpHealthCare = household?.give_up_health_care ?: ""
                saleOfProductionAssets = household?.sale_of_production_assets ?: ""

                daysReducedAmountConsumed =
                    household?.days_spent_reduce_amount_consumed_coping_strategy ?: ""
                daysReducedMealsAdult =
                    household?.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy
                        ?: ""
                daysReducedMealsConsumed =
                    household?.days_spent_reduce_meals_consumed_coping_strategy ?: ""
                daysEatLessExpensively =
                    household?.days_spent_eat_less_expensively_coping_strategy ?: ""
                daysWithoutEating = household?.days_spent_days_without_eating_coping_strategy ?: ""
                daysConsumedWildFood = household?.days_spent_consume_wild_food_coping_strategy ?: ""
                daysBorrowFood =
                    household?.days_spent_borrow_food_or_rely_on_family_help_coping_strategy ?: ""
                daysOtherCoping = household?.days_spent_other_coping_strategy ?: ""
                numberOfMealsChildren2To5 =
                    household?.number_of_meals_eaten_by_children_2_to_5_yesterday ?: ""
                numberOfMealsAdults18plus =
                    household?.number_of_meals_eaten_by_adults_18_plus_yesterday ?: ""
                daysConsumedSugarOrSweets =
                    household?.number_of_days_in_week_consumed_sugar_or_sweet_products ?: ""
                daysConsumedStapleFoods =
                    household?.number_of_days_in_week_consumed_staple_foods ?: ""
                daysConsumedVegetables = household?.number_of_days_in_week_consumed_vegetables ?: ""
                daysBeggingFood = household?.days_spent_begging_coping_strategy ?: ""

                daysConsumedMeat = household?.days_spent_consume_wild_food_coping_strategy ?: ""
                daysConsumedLegumes =
                    household?.number_of_days_in_week_consumed_legumes_or_nuts ?: ""
                daysConsumedFruits = household?.number_of_days_in_week_consumed_fruits ?: ""
                daysConsumedDiary = household?.number_of_days_in_week_consumed_dairy_products ?: ""
                daysConsumedCookingOils =
                    household?.number_of_days_in_week_consumed_cooking_oils ?: ""


                hasHouseholdGoods = household?.has_household_goods ?: ""
                accessToCultivableLand = household?.household_member_access_to_cultivable_land ?: ""
                ownerOfCultivableLand = household?.household_member_owner_of_cultivable_land ?: ""
                cashCropOrCommercialFarming =
                    household?.practice_of_cash_crop_farming_or_commercial_farming ?: ""
                mosquitoNets = household?.number_of_mosquito_nets_owned ?: ""
                guineaPigsOwned = household?.number_of_guinea_pig_owned ?: ""
                poultryOwned = household?.number_of_poultry_owned ?: ""
                bicycleOwned = household?.number_of_bicycle_owned ?: ""
                oilStoveOwned = household?.number_of_oil_stove_owned ?: ""

                rabbitOwned = household?.number_of_rabbit_owned ?: ""
                sheepOwned = household?.number_of_sheep_owned ?: ""
                motorbikeOwned = household?.number_of_motorbike_owned ?: ""
                roomsUsedForSleeping = household?.number_of_rooms_used_for_sleeping ?: ""
                wheelBarrowOwned = household?.number_of_wheelbarrow_owned ?: ""
                radioOwned = household?.number_of_radio_owned ?: ""
                stoveOrOvenOwned = household?.number_of_stove_or_oven_owned ?: ""
                rickShawOwned = household?.number_of_rickshaw_owned ?: ""
                solarPlateOwned = household?.number_of_solar_plate_owned ?: ""
                sewingMachineOwned = household?.number_of_sewing_machine_owned ?: ""

                mattressOwned = household?.number_of_mattress_owned ?: ""
                televisionOwned = household?.number_of_television_owned ?: ""
                tableOwned = household?.number_of_table_owned ?: ""
                sofaOwned = household?.number_of_sofa_owned ?: ""
                handsetOrPhoneOwned = household?.number_of_handset_or_phone_owned ?: ""
                electricIronOwned = household?.number_of_electric_iron_owned ?: ""
                plowOwned = household?.number_of_plow_owned ?: ""
                goatOwned = household?.number_of_goat_owned ?: ""
                fridgeOwned = household?.number_of_fridge_owned ?: ""
                dvdDriverOwned = household?.number_of_dvd_driver_owned ?: ""

                pigsOwned = household?.number_of_pigs_owned ?: ""
                cableTVOwned = household?.number_of_canal_tnt_tv_cable_owned ?: ""
                cowOwned = household?.number_of_cow_owned ?: ""
                charcoalIron = household?.number_of_charcoal_iron_owned ?: ""
                computerOwned = household?.number_of_computer_owned ?: ""
                chairOwned = household?.number_of_chair_owned ?: ""
                cartsOwned = household?.number_of_cart_owned ?: ""
                carsOwned = household?.number_of_cars_owned ?: ""
                bedOwned = household?.number_of_bed_owned ?: ""
                airConditionerOwned = household?.number_of_air_conditioner_owned ?: ""
                cultivatedLandOwned = household?.amount_of_cultivable_land_owned ?: ""
                fanOwned = household?.number_of_fan_owned ?: ""

//                form f
                sourceOfDrinkingWater = household?.main_source_of_household_drinking_water ?: ""
                otherSourceOfDrinkingWater =
                    household?.other_main_source_of_household_drinking_water ?: ""
                typeOfToilet = household?.type_of_household_toilet ?: ""
                otherTypeOfToilet = household?.other_type_of_household_toilet ?: ""
                wasteDisposal = household?.method_of_waste_disposal ?: ""
                otherWasteDisposal = household?.other_method_of_waste_disposal ?: ""
                placeForHandWashing = household?.place_to_wash_hands ?: ""
                comments = household?.comments ?: ""


//          init  step five fields
            }
        }



        viewModel.setStartTime()

        /*viewLifecycleOwner.lifecycleScope.launchWhenStarted {
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

        }*/


        binding.apply {

            when (viewModel.consent) {
                rbYes.text -> rgConsent.check(rbYes.id)
                rbNo.text -> rgConsent.check(rbNo.id)
            }

            rgConsent.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbYes.id -> {
                        viewModel.consent = rbYes.text.toString()
                    }

                    else -> {
                        viewModel.consent = rbNo.text.toString()
                    }
                }
            }


            btnNext.setOnClickListener {
                val title =
                    if (household != null) getString(R.string.edit_household) else getString(
                        R.string.add_household
                    )
                val bundle = bundleOf("title" to title)
                findNavController().navigate(R.id.sectionBLocationFragment, bundle)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}