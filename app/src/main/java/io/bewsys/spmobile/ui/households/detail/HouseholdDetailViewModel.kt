package io.bewsys.spmobile.ui.households.detail


import android.util.Log
import androidx.lifecycle.*

import io.bewsys.spmobile.data.local.HouseholdModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class HouseholdDetailViewModel(
    private val state: SavedStateHandle

) : ViewModel() {
    private val _detailChannel = Channel<DetailEvent>()
    val detailChannel get() = _detailChannel.receiveAsFlow()


    var household: HouseholdModel? = state["household"]

    var id = household?.id
    var consent = household?.consent ?: ""
    var initialRegistrationType = household?.initial_registration_type ?: ""
    var respondentFirstName = household?.respondent_firstname ?: ""
    var respondentMiddleName = household?.respondent_middlename ?: ""
    var respondentLastName = household?.respondent_middlename ?: ""
    var respondentAge = household?.respondent_middlename ?: ""
    var respondentDOB = household?.respondent_dob ?: ""
    var respondentVoterId = household?.respondent_voter_id ?: ""
    var respondentPhoneNo = household?.respondent_phone_number ?: ""
    var address = household?.address ?: ""
    var lon = household?.gps_longitude ?: ""
    var lat = household?.gps_latitude ?: ""
    var respondentFamilyBondToHead = household?.respondent_family_bond_to_head ?: ""
    var villageOrQuartier = household?.village_or_quartier ?: ""
    var territoryOrTown = household?.territory_or_town ?: ""
    var areaOfResidence = household?.area_of_residence ?: ""
    var respondentSex: String = ""
    var province = household?.province_name ?: ""
    var territory = household?.territory_name ?: ""
    var community = household?.community_name ?: ""
    var groupment = household?.groupement_name ?: ""
    private var provinceId = household?.province_id ?: "1"
    private var communityId = household?.community_id ?: "1"
    private var groupmentId = household?.groupment_id ?: "1"
    private var territoryId = household?.territory_id ?: "1"
    private var healthAreaId = household?.health_area_id ?: "1"
    private var healthZoneId = household?.health_zone_id ?: "1"
    var headIsRespondent: String = household?.is_head_respondent ?: ""
    var headFirstName = household?.household_head_firstname ?: ""
    var headMiddleName = household?.household_head_middlename ?: ""
    var headLastName = household?.household_head_middlename ?: ""
    var headAgeKnown = household?.head_age_known ?: ""
    var headAge = household?.household_head_age ?: ""
    var headDOB = household?.household_head_dob ?: ""
    var headVoterId = household?.household_head_voter_id_card ?: ""
    var headPhoneNo = household?.household_head_phone_number ?: ""
    var headBirthCert = household?.household_head_birth_certificate ?: ""
    var headEduLevel = household?.household_head_educational_level_id ?: ""
    var headSocioProfessionalCategory =
        household?.household_head_socio_professional_category_id ?: ""
    var headSchoolAttendance = household?.household_head_school_attendance_id ?: ""
    var headSectorOfWork = household?.household_head_sector_of_work_id ?: ""
    var headDisability = household?.household_head_disability_id ?: ""
    var headPregnancyStatus = household?.household_head_pregnancy_status ?: ""
    var headMaritalStatus = household?.household_head_marital_status_id ?: ""
    var headDOBKnown = household?.head_dob_known ?: ""
    var headSex = household?.household_head_sex ?: ""
    var isIncomeRegular = household?.is_income_regular ?: ""
    var bankCardOrAccount = household?.bank_account_or_bank_card_available ?: ""
    var benefitFromSocialAssistanceProgram =
        household?.household_member_with_benefit_from_social_assistance_program ?: ""
    var migrationStatus = household?.household_migration_status ?: ""
    var unitOfMigrationDuration = household?.unit_of_migration_duration ?: ""
    var nameOfSocialAssistanceProgram = household?.name_of_social_assistance_program ?: ""
    var otherMigrationStatus = household?.other_household_migration_status ?: ""
    var durationDisplacedReturnedRepatriatedRefugee =
        household?.duration_displaced_returned_repatriated_refugee ?: ""
    var householdMonthlyIncome = household?.household_monthly_income ?: ""
    var minimumMonthlyIncomeNecessaryLiveWithoutDifficulties =
        household?.minimum_monthly_income_necessary_live_without_difficulties ?: ""
    var mobileMoneyUsername = household?.mobile_money_username ?: ""
    var mobileMoneyPhoneNumber = household?.mobile_money_phone_number ?: ""
    var affectedByConflict = household?.affected_by_conflict ?: ""
    var affectedByEpidemic = household?.affected_by_epidemic ?: ""
    var affectedByClimateShock = household?.affected_by_climate_shock ?: ""
    var affectedByOtherShock = household?.affected_by_other_shock ?: ""
    var takeChildrenOutOfSchool = household?.take_children_out_of_school ?: ""
    var useOfChildLabour = household?.use_of_child_labor ?: ""
    var useOfEarlyMarriage = household?.use_of_early_marriage ?: ""
    var gaveUpHealthCare = household?.give_up_health_care ?: ""
    var saleOfProductionAssets = household?.sale_of_production_assets ?: ""
    var daysReducedMealsConsumed = household?.days_spent_reduce_meals_consumed_coping_strategy ?: ""
    var daysReducedMealsAdult =
        household?.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy ?: ""
    var daysReducedAmountConsumed =
        household?.days_spent_reduce_amount_consumed_coping_strategy ?: ""
    var daysEatLessExpensively = household?.days_spent_eat_less_expensively_coping_strategy ?: ""
    var daysWithoutEating = household?.days_spent_days_without_eating_coping_strategy ?: ""
    var daysConsumedWildFood = household?.days_spent_consume_wild_food_coping_strategy ?: ""
    var daysBorrowFood =
        household?.days_spent_borrow_food_or_rely_on_family_help_coping_strategy ?: ""
    var daysBeggingFood = household?.days_spent_begging_coping_strategy ?: ""
    var daysOtherCoping = household?.days_spent_other_coping_strategy ?: ""
    var numberOfMealsChildren6To17 =
        household?.number_of_meals_eaten_by_children_6_to_17_yesterday ?: ""
    var numberOfMealsChildren2To5 =
        household?.number_of_meals_eaten_by_children_2_to_5_yesterday ?: ""
    var numberOfMealsAdults18plus =
        household?.number_of_meals_eaten_by_adults_18_plus_yesterday ?: ""
    var daysConsumedSugarOrSweets =
        household?.number_of_days_in_week_consumed_sugar_or_sweet_products ?: ""
    var daysConsumedStapleFoods = household?.number_of_days_in_week_consumed_staple_foods ?: ""
    var daysConsumedVegetables = household?.number_of_days_in_week_consumed_vegetables ?: ""
    var daysConsumedMeat = household?.number_of_days_in_week_consumed_meat ?: ""
    var daysConsumedLegumes = household?.number_of_days_in_week_consumed_legumes_or_nuts ?: ""
    var daysConsumedFruits = household?.number_of_days_in_week_consumed_fruits ?: ""
    var daysConsumedDiary = household?.number_of_days_in_week_consumed_dairy_products ?: ""
    var daysConsumedCookingOils = household?.number_of_days_in_week_consumed_cooking_oils ?: ""
    var hasLiveStock = household?.has_livestock ?: ""
    var hasHouseholdGoods = household?.has_household_goods ?: ""
    var accessToCultivableLand = household?.household_member_access_to_cultivable_land ?: ""
    var ownerOfCultivableLand = household?.household_member_owner_of_cultivable_land ?: ""
    var cashCropOrCommercialFarming =
        household?.practice_of_cash_crop_farming_or_commercial_farming ?: ""
    var mosquitoNets: String = household?.number_of_mosquito_nets_owned ?: ""
    var guineaPigsOwned: String = household?.number_of_guinea_pig_owned ?: ""
    var poultryOwned: String = household?.number_of_poultry_owned ?: ""
    var bicycleOwned: String = household?.number_of_bicycle_owned ?: ""
    var oilStoveOwned: String = household?.number_of_oil_stove_owned ?: ""
    var rabbitOwned: String = household?.number_of_rabbit_owned ?: ""
    var sheepOwned: String = household?.number_of_sheep_owned ?: ""
    var motorbikeOwned: String = household?.number_of_motorbike_owned ?: ""
    var roomsUsedForSleeping: String = household?.number_of_rooms_used_for_sleeping ?: ""
    var wheelBarrowOwned: String = household?.number_of_wheelbarrow_owned ?: ""
    var radioOwned: String = household?.number_of_radio_owned ?: ""
    var stoveOrOvenOwned: String = household?.number_of_stove_or_oven_owned ?: ""
    var rickShawOwned: String = household?.number_of_rickshaw_owned ?: ""
    var solarPlateOwned: String = household?.number_of_solar_plate_owned ?: ""
    var sewingMachineOwned: String = household?.number_of_sewing_machine_owned ?: ""
    var mattressOwned: String = household?.number_of_mattress_owned ?: ""
    var televisionOwned: String = household?.number_of_television_owned ?: ""
    var tableOwned: String = household?.number_of_table_owned ?: ""
    var sofaOwned: String = household?.number_of_sofa_owned ?: ""
    var handsetOrPhoneOwned: String = household?.number_of_handset_or_phone_owned ?: ""
    var electricIronOwned: String = household?.number_of_electric_iron_owned ?: ""
    var plowOwned: String = household?.number_of_plow_owned ?: ""
    var goatOwned: String = household?.number_of_goat_owned ?: ""
    var fridgeOwned: String = household?.number_of_fridge_owned ?: ""
    var dvdDriverOwned: String = household?.number_of_dvd_driver_owned ?: ""
    var pigsOwned: String = household?.number_of_pigs_owned ?: ""
    var cableTVOwned: String = household?.number_of_canal_tnt_tv_cable_owned ?: ""
    var cowOwned: String = household?.number_of_cow_owned ?: ""
    var charcoalIron: String = household?.number_of_charcoal_iron_owned ?: ""
    var computerOwned: String = household?.number_of_computer_owned ?: ""
    var chairOwned: String = household?.number_of_chair_owned ?: ""
    var cartsOwned: String = household?.number_of_cart_owned ?: ""
    var carsOwned: String = household?.number_of_cars_owned ?: ""
    var bedOwned: String = household?.number_of_bed_owned ?: ""
    var airConditionerOwned: String = household?.number_of_air_conditioner_owned ?: ""
    var cultivatedLandOwned: String = household?.amount_of_cultivable_land_owned ?: ""
    var fanOwned: String = household?.number_of_fan_owned ?: ""


    fun onEditFabClicked() {
        viewModelScope.launch {
            _detailChannel.send(DetailEvent.FabClicked)
        }
    }

    fun onDeleteActionFabClicked() {
        viewModelScope.launch {
            _detailChannel.send(DetailEvent.FabDeleteActionClicked(id!!))
        }
    }

    fun onEditActionFabClicked() {
        Log.d("HouseholdViewModel", "Edit Action Fab Clicked")
    }

    sealed class DetailEvent {
        object FabClicked : DetailEvent()
        data class FabEditActionClicked(val id: Long) : DetailEvent()
        data class FabDeleteActionClicked(val id: Long) : DetailEvent()

    }
}