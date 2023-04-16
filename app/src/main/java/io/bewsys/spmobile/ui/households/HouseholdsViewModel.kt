package io.bewsys.spmobile.ui.households


import androidx.lifecycle.*
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HouseholdsViewModel(
    private val state: SavedStateHandle,
    private val householdRepository: HouseholdRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val householdsEventChannel = Channel<HouseholdEvent>()
    val householdsEventEvent = householdsEventChannel.receiveAsFlow()

    private val _households = MutableLiveData<List<HouseholdModel>>()
    val households: LiveData<List<HouseholdModel>>
        get() = _households


    init {
        loadHouseholds()
    }

    //    Todo: get province and community ids
    private fun loadHouseholds() {
        viewModelScope.launch {
            householdRepository.getAllHouseholds()
                .map { households ->
                    households.map {

                        HouseholdModel(
                            it.id,
                            it.remote_id,
                            it.survey_no,
                            it.cac,
                            it.temp_survey_no,
                            it.supervisor_id,
                            it.team_leader_id,
                            it.user_id,
                            it.survey_date,
                            it.consent,
                            it.CBT_score,
                            it.initial_registration_type,
                            it.respondent_type,
                            it.respondent_firstname,
                            it.respondent_middlename,
                            it.respondent_lastname,
                            it.respondent_dob,
                            it.respondent_family_bond_to_head,
                            it.respondent_voter_id,
                            it.respondent_phone_number,
                            it.household_head_firstname,
                            it.household_head_lastname,
                            it.household_head_dob,
                            it.household_head_sex,
                            it.head_age_known,
                            it.head_dob_known,
                            it.household_head_voter_id_card,
                            it.household_head_phone_number,
                            it.household_head_age,
                            it.household_head_middlename,
                            it.household_head_marital_status_id,
                            it.household_head_birth_certificate,
                            it.household_head_educational_level_id,
                            it.household_head_disability_id,
                            it.household_head_socio_professional_category_id,
                            it.household_head_school_attendance_id,
                            it.household_head_sector_of_work_id,
                            it.household_head_pregnancy_status,
                            it.household_migration_status,
                            it.is_head_respondent,
                            it.area_of_residence,
                            it.province_id,
                            it.community_id,
                            it.territory_id,
                            it.health_zone_id,
                            it.health_area_id,
                            it.groupment_id,
                            it.province_id?.let { id -> dashboardRepository.getProvinceById(id.toLong())?.name } ,
                            it.community_id?.let { id -> dashboardRepository.getCommunityById(id.toLong())?.name },
                            it.territory_id?.let { id -> dashboardRepository.getTerritoryById(id.toLong())?.name },
                            it.groupment_id?.let { id -> dashboardRepository.getGroupmentById(id.toLong())?.name },
                            it.duration_displaced_returned_repatriated_refugee,
                            it.unit_of_migration_duration,
                            it.territory_or_town,
                            it.household_status,
                            it.status,
                            it.bank_account_or_bank_card_available,
                            it.is_income_regular,
                            it.household_member_access_to_cultivable_land,
                            it.household_member_owner_of_cultivable_land,
                            it.practice_of_cash_crop_farming_or_commercial_farming,
                            it.has_livestock,
                            it.has_household_goods,
                            it.affected_by_conflict,
                            it.affected_by_epidemic,
                            it.affected_by_climate_shock,
                            it.take_children_out_of_school,
                            it.sale_of_production_assets,
                            it.use_of_child_labor,
                            it.use_of_early_marriage,
                            it.give_up_health_care,
                            it.days_spent_other_coping_strategy,
                            it.number_of_months_displaced_returned_repatriated_refugee,
                            it.gps_longitude,
                            it.gps_latitude,
                            it.start_time,
                            it.finish_time,
                            it.address,
                            it.comments,
                            it.profile_picture,
                            it.mobile_money_username,
                            it.mobile_money_phone_number,
                            it.village_or_quartier,
                            it.other_household_migration_status,
                            it.other_occupation_status_of_current_accommodation,
                            it.other_main_material_of_exterior_walls,
                            it.other_main_soil_material,
                            it.occupation_status_of_current_accommodation,
                            it.main_material_of_exterior_walls,
                            it.main_soil_material,
                            it.number_of_rooms_used_for_sleeping,
                            it.type_of_fuel_used_for_household_cooking,
                            it.other_type_of_fuel_used_for_household_cooking,
                            it.main_source_of_household_drinking_water,
                            it.other_main_source_of_household_drinking_water,
                            it.type_of_household_toilet,
                            it.other_type_of_household_toilet,
                            it.method_of_waste_disposal,
                            it.place_to_wash_hands,
                            it.minimum_monthly_income_necessary_live_without_difficulties,
                            it.household_monthly_income,
                            it.number_of_goat_owned,
                            it.number_of_sheep_owned,
                            it.number_of_cow_owned,
                            it.number_of_rabbit_owned,
                            it.number_of_pigs_owned,
                            it.number_of_poultry_owned,
                            it.number_of_guinea_pig_owned,
                            it.number_of_cars_owned,
                            it.number_of_motorbike_owned,
                            it.number_of_bicycle_owned,
                            it.number_of_cart_owned,
                            it.number_of_plow_owned,
                            it.number_of_wheelbarrow_owned,
                            it.number_of_rickshaw_owned,
                            it.number_of_mattress_owned,
                            it.number_of_fridge_owned,
                            it.number_of_fan_owned,
                            it.number_of_air_conditioner_owned,
                            it.number_of_radio_owned,
                            it.number_of_television_owned,
                            it.number_of_canal_tnt_tv_cable_owned,
                            it.number_of_dvd_driver_owned,
                            it.number_of_handset_or_phone_owned,
                            it.number_of_computer_owned,
                            it.number_of_stove_or_oven_owned,
                            it.number_of_oil_stove_owned,
                            it.number_of_solar_plate_owned,
                            it.number_of_sewing_machine_owned,
                            it.number_of_electric_iron_owned,
                            it.number_of_charcoal_iron_owned,
                            it.number_of_bed_owned,
                            it.number_of_table_owned,
                            it.number_of_chair_owned,
                            it.number_of_sofa_owned,
                            it.number_of_mosquito_nets_owned,
                            it.amount_of_cultivable_land_owned,
                            it.number_of_meals_eaten_by_adults_18_plus_yesterday,
                            it.number_of_meals_eaten_by_children_6_to_17_yesterday,
                            it.number_of_meals_eaten_by_children_2_to_5_yesterday,
                            it.number_of_days_in_week_consumed_staple_foods,
                            it.number_of_days_in_week_consumed_legumes_or_nuts,
                            it.number_of_days_in_week_consumed_dairy_products,
                            it.number_of_days_in_week_consumed_meat,
                            it.number_of_days_in_week_consumed_vegetables,
                            it.number_of_days_in_week_consumed_fruits,
                            it.number_of_days_in_week_consumed_cooking_oils,
                            it.number_of_days_in_week_consumed_sugar_or_sweet_products,
                            it.days_spent_reduce_amount_consumed_coping_strategy,
                            it.days_spent_reduce_meals_consumed_coping_strategy,
                            it.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy,
                            it.days_spent_eat_less_expensively_coping_strategy,
                            it.days_spent_borrow_food_or_rely_on_family_help_coping_strategy,
                            it.days_spent_days_without_eating_coping_strategy,
                            it.days_spent_begging_coping_strategy,
                            it.days_spent_consume_wild_food_coping_strategy,
                            it.other_household_activities_in_past_12_months,
                            it.other_method_of_waste_disposal,
                            it.other_livestock_owned,
                            it.household_member_with_benefit_from_social_assistance_program,
                            it.name_of_social_assistance_program,
                            it.affected_by_other_shock

                        )

                    }
                }.collectLatest {
                    _households.value = it
                }
        }
    }


    fun onAddRegistrationFabClicked() = viewModelScope.launch {
        householdsEventChannel.send(HouseholdEvent.AddRegistrationClicked)
    }

    fun onHumanitarianFabClicked() = viewModelScope.launch {
        householdsEventChannel.send(HouseholdEvent.HumanitarianClicked)
    }

    fun onDevelopmentalFabClicked() = viewModelScope.launch {
        householdsEventChannel.send(HouseholdEvent.DevelopmentalClicked)
    }


    fun onAddHouseholdResult(result: Int) {
        when (result) {
            ADD_HOUSEHOLD_RESULT_OK -> showHouseholdSavedConfirmationMessage()
        }
    }

    private fun showHouseholdSavedConfirmationMessage() =
        viewModelScope.launch {
            householdsEventChannel.send(
                HouseholdEvent.ShowHouseholdSavedConfirmationMessage(
                    "Household saved!"
                )
            )
        }

    fun onHouseholdSelected(householdModel: HouseholdModel)= viewModelScope.launch {
        householdsEventChannel.send(HouseholdEvent.NavigateToHouseholdDetailScreen(householdModel))
    }

    sealed class HouseholdEvent {
        object AddRegistrationClicked : HouseholdEvent()
        object DevelopmentalClicked : HouseholdEvent()
        object HumanitarianClicked : HouseholdEvent()
        data class ShowHouseholdSavedConfirmationMessage(val msg: String) : HouseholdEvent()
        data class NavigateToHouseholdDetailScreen(val householdModel: HouseholdModel) : HouseholdEvent()
    }
}