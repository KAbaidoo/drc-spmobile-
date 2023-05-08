package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf


import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import io.bewsys.spmobile.FormNavigationArgs
import io.bewsys.spmobile.R
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


        val household = args.household
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
                viewModel.id = household?.id
                consent = household?.consent.toString()
                initialRegistrationType = household?.initial_registration_type.toString()
                respondentFirstName = household?.respondent_firstname.toString()
                respondentMiddleName = household?.respondent_middlename.toString()
                respondentLastName = household?.respondent_lastname.toString()
                respondentSex = household?.respondent_sex.toString()
                respondentFamilyBondToHead = household?.respondent_family_bond_to_head.toString()
                respondentDOB = household?.respondent_dob.toString()
                respondentVoterId = household?.respondent_voter_id.toString()
                respondentPhoneNo = household?.respondent_phone_number.toString()
                address = household?.address.toString()
                lon = household?.gps_longitude.toString()
                lat = household?.gps_latitude.toString()
                villageOrQuartier = household?.village_or_quartier.toString()
                territoryOrTown = household?.territory_or_town.toString()
                areaOfResidence = household?.area_of_residence.toString()
                province = household?.province_name.toString()
                territory = household?.territory_name.toString()
                community = household?.community_name.toString()
                groupment = household?.groupement_name.toString()

                migrationStatus = household?.household_migration_status.toString()
                unitOfMigrationDuration = household?.unit_of_migration_duration.toString()
                otherMigrationStatus = household?.other_household_migration_status.toString()
                headIsRespondent = household?.is_head_respondent.toString()
                headFirstName = household?.household_head_firstname.toString()
                headMiddleName = household?.household_head_middlename.toString()
                headLastName = household?.household_head_lastname.toString()
                headAge = household?.household_head_age.toString()
                headAgeKnown = household?.head_age_known.toString()
                headDOB = household?.household_head_dob.toString()
                headDOBKnown = household?.head_dob_known.toString()
                headSex = household?.household_head_sex.toString()
                headVoterId = household?.household_head_voter_id_card.toString()
                headPhoneNo = household?.household_head_phone_number.toString()
                headBirthCert = household?.household_head_birth_certificate.toString()
                headEduLevel = household?.household_head_educational_level_id.toString()
                headSocioProfessionalCategory = household?.household_head_socio_professional_category_id.toString()
                headSchoolAttendance = household?.household_head_school_attendance_id.toString()
                headSectorOfWork = household?.household_head_sector_of_work_id.toString()
                headDisability = household?.household_head_disability_id.toString()
                headPregnancyStatus = household?.status.toString()
                headMaritalStatus = household?.household_head_marital_status_id.toString()

                isIncomeRegular = household?.is_income_regular.toString()
                bankCardOrAccount = household?.bank_account_or_bank_card_available.toString()
                benefitFromSocialAssistanceProgram = household?.household_member_with_benefit_from_social_assistance_program.toString()

                nameOfSocialAssistanceProgram = household?.name_of_social_assistance_program.toString()
                durationDisplacedReturnedRepatriatedRefugee = household?.duration_displaced_returned_repatriated_refugee.toString()
                householdMonthlyIncome = household?.household_monthly_income.toString()
                minimumMonthlyIncomeNecessaryLiveWithoutDifficulties = household?.minimum_monthly_income_necessary_live_without_difficulties.toString()
                mobileMoneyUsername = household?.mobile_money_username.toString()
                mobileMoneyPhoneNumber = household?.mobile_money_phone_number.toString()

                affectedByConflict = household?.affected_by_conflict.toString()
                affectedByEpidemic = household?.affected_by_epidemic.toString()
                affectedByClimateShock = household?.affected_by_climate_shock.toString()
                affectedByOtherShock = household?.affected_by_other_shock.toString()
                takeChildrenOutOfSchool = household?.take_children_out_of_school.toString()
                useOfChildLabour = household?.use_of_child_labor.toString()
                useOfEarlyMarriage = household?.use_of_early_marriage.toString()
                gaveUpHealthCare = household?.give_up_health_care.toString()
                saleOfProductionAssets = household?.sale_of_production_assets.toString()

                daysReducedAmountConsumed = household?.days_spent_reduce_amount_consumed_coping_strategy.toString()
                daysReducedMealsAdult = household?.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy.toString()
                daysReducedMealsConsumed = household?.days_spent_reduce_meals_consumed_coping_strategy.toString()
                daysEatLessExpensively = household?.days_spent_eat_less_expensively_coping_strategy.toString()
                daysWithoutEating = household?.days_spent_days_without_eating_coping_strategy.toString()
                daysConsumedWildFood = household?.days_spent_consume_wild_food_coping_strategy.toString()
                daysBorrowFood = household?.days_spent_borrow_food_or_rely_on_family_help_coping_strategy.toString()
                daysOtherCoping = household?.days_spent_other_coping_strategy.toString()
                numberOfMealsChildren2To5 = household?.number_of_meals_eaten_by_children_2_to_5_yesterday.toString()
                numberOfMealsAdults18plus = household?.number_of_meals_eaten_by_adults_18_plus_yesterday.toString()
                daysConsumedSugarOrSweets = household?.number_of_days_in_week_consumed_sugar_or_sweet_products.toString()
                daysConsumedStapleFoods = household?.number_of_days_in_week_consumed_staple_foods.toString()
                daysConsumedVegetables = household?.number_of_days_in_week_consumed_vegetables.toString()
                daysBeggingFood = household?.days_spent_begging_coping_strategy.toString()

                daysConsumedMeat = household?.days_spent_consume_wild_food_coping_strategy.toString()
                daysConsumedLegumes = household?.number_of_days_in_week_consumed_legumes_or_nuts.toString()
                daysConsumedFruits = household?.number_of_days_in_week_consumed_fruits.toString()
                daysConsumedDiary = household?.number_of_days_in_week_consumed_dairy_products.toString()
                daysConsumedCookingOils = household?.number_of_days_in_week_consumed_cooking_oils.toString()

                hasLiveStock = household?.has_livestock.toString()
                hasHouseholdGoods = household?.has_household_goods.toString()
                accessToCultivableLand = household?.household_member_access_to_cultivable_land.toString()
                ownerOfCultivableLand = household?.household_member_owner_of_cultivable_land.toString()
                cashCropOrCommercialFarming = household?.practice_of_cash_crop_farming_or_commercial_farming.toString()
                mosquitoNets = household?.number_of_mosquito_nets_owned.toString()
                guineaPigsOwned = household?.number_of_guinea_pig_owned.toString()
                poultryOwned = household?.number_of_poultry_owned.toString()
                bicycleOwned = household?.number_of_bicycle_owned.toString()
                oilStoveOwned = household?.number_of_oil_stove_owned.toString()

                rabbitOwned = household?.number_of_rabbit_owned.toString()
                sheepOwned = household?.number_of_sheep_owned.toString()
                motorbikeOwned = household?.number_of_motorbike_owned.toString()
                roomsUsedForSleeping = household?.number_of_rooms_used_for_sleeping.toString()
                wheelBarrowOwned = household?.number_of_wheelbarrow_owned.toString()
                radioOwned = household?.number_of_radio_owned.toString()
                stoveOrOvenOwned = household?.number_of_stove_or_oven_owned.toString()
                rickShawOwned = household?.number_of_rickshaw_owned.toString()
                solarPlateOwned = household?.number_of_solar_plate_owned.toString()
                sewingMachineOwned = household?.number_of_sewing_machine_owned.toString()

                mattressOwned = household?.number_of_mattress_owned.toString()
                televisionOwned = household?.number_of_television_owned.toString()
                tableOwned = household?.number_of_table_owned.toString()
                sofaOwned = household?.number_of_sofa_owned.toString()
                handsetOrPhoneOwned = household?.number_of_handset_or_phone_owned.toString()
                electricIronOwned = household?.number_of_electric_iron_owned.toString()
                plowOwned = household?.number_of_plow_owned.toString()
                goatOwned = household?.number_of_goat_owned.toString()
                fridgeOwned = household?.number_of_fridge_owned.toString()
                dvdDriverOwned = household?.number_of_dvd_driver_owned.toString()

                pigsOwned = household?.number_of_pigs_owned.toString()
                cableTVOwned = household?.number_of_canal_tnt_tv_cable_owned.toString()
                cowOwned = household?.number_of_cow_owned.toString()
                charcoalIron = household?.number_of_charcoal_iron_owned.toString()
                computerOwned = household?.number_of_computer_owned.toString()
                chairOwned = household?.number_of_chair_owned.toString()
                cartsOwned = household?.number_of_cart_owned.toString()
                carsOwned = household?.number_of_cars_owned.toString()
                bedOwned = household?.number_of_bed_owned.toString()
                airConditionerOwned = household?.number_of_air_conditioner_owned.toString()
                cultivatedLandOwned = household?.amount_of_cultivable_land_owned.toString()
                fanOwned = household?.number_of_fan_owned.toString()


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
                findNavController().navigate(R.id.formStepTwoFragment, bundle)
            }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}