package io.bewsys.spmobile.ui.households


import androidx.lifecycle.*
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.DELETE_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.household.BulkUploadResponse
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HouseholdsViewModel(
    private val state: SavedStateHandle,
    private val householdRepository: HouseholdRepository,
    private val dashboardRepository: DashboardRepository,

    ) : ViewModel() {

    private val _householdsEventChannel = Channel<HouseholdEvent>()
    val householdsEventEvent = _householdsEventChannel.receiveAsFlow()

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
                .map { householdEntity ->
                    householdEntity.map {
                        HouseholdModel(
                            id = it.id,
                            remote_id = it.remote_id,
                            survey_no = it.survey_no,
                            cac = it.cac,
                            temp_survey_no = it.temp_survey_no,
                            supervisor_id = it.supervisor_id,
                            team_leader_id = it.team_leader_id,
                            user_id = it.user_id,
                            survey_date = it.survey_date,
                            consent = it.consent,
                            CBT_score = it.CBT_score,
                            initial_registration_type = it.initial_registration_type,
                            respondent_type = it.respondent_type,
                            respondent_firstname = it.respondent_firstname,
                            respondent_middlename = it.respondent_middlename,
                            respondent_lastname = it.respondent_lastname,
                            respondent_dob = it.respondent_dob,
                            respondent_family_bond_to_head = it.respondent_family_bond_to_head,
                            household_head_voter_id_card = it.respondent_voter_id,
                            respondent_phone_number = it.respondent_phone_number,
                            household_head_firstname = it.household_head_firstname,
                            household_head_lastname = it.household_head_lastname,
                            head_dob_known = it.household_head_dob,
                            household_head_sex = it.household_head_sex,
                            head_age_known = it.head_age_known,
                            household_head_dob = it.head_dob_known,
                            household_head_phone_number = it.household_head_phone_number,
                            household_head_age = it.household_head_age,
                            household_head_middlename = it.household_head_middlename,
                            household_head_marital_status_id = it.household_head_marital_status_id,
                            household_head_birth_certificate = it.household_head_birth_certificate,
                            household_head_educational_level_id = it.household_head_educational_level_id,
                            household_head_disability_id = it.household_head_disability_id,
                            household_head_socio_professional_category_id = it.household_head_socio_professional_category_id,
                            household_head_school_attendance_id = it.household_head_school_attendance_id,
                            household_head_sector_of_work_id = it.household_head_sector_of_work_id,
                            household_head_pregnancy_status = it.household_head_pregnancy_status,
                            household_migration_status = it.household_migration_status,
                            is_head_respondent = it.is_head_respondent,
                            area_of_residence = it.area_of_residence,
                            province_id = it.province_id,
                            community_id = it.community_id,
                            territory_id = it.territory_id,
                            health_area_id = it.health_zone_id,
                            health_zone_id = it.health_area_id,
                            groupment_id = it.groupment_id,
                            province_name = it.province_id?.let { id ->
                                dashboardRepository.getProvinceById(
                                    id.toLong()
                                )?.name
                            },
                            community_name = it.community_id?.let { id ->
                                dashboardRepository.getCommunityById(
                                    id.toLong()
                                )?.name
                            },
                            territory_name = it.territory_id?.let { id ->
                                dashboardRepository.getTerritoryById(
                                    id.toLong()
                                )?.name
                            },
                            groupement_name = it.groupment_id?.let { id ->
                                dashboardRepository.getGroupmentById(
                                    id.toLong()
                                )?.name
                            },
                            health_area_name = it.health_area_id?.let { id ->
                                dashboardRepository.getHealthAreaById(
                                    id.toLong()
                                )?.name
                            },
                            health_zone_name = it.health_zone_id?.let { id ->
                                dashboardRepository.getHealthZoneById(
                                    id.toLong()
                                )?.name
                            },
                            duration_displaced_returned_repatriated_refugee = it.duration_displaced_returned_repatriated_refugee,
                            unit_of_migration_duration = it.unit_of_migration_duration,
                            territory_or_town = it.territory_or_town,
                            household_status = it.household_status,
                            status = it.status,
                            bank_account_or_bank_card_available = it.bank_account_or_bank_card_available,
                            is_income_regular = it.is_income_regular,
                            household_member_access_to_cultivable_land = it.household_member_access_to_cultivable_land,
                            household_member_owner_of_cultivable_land = it.household_member_owner_of_cultivable_land,
                            practice_of_cash_crop_farming_or_commercial_farming = it.practice_of_cash_crop_farming_or_commercial_farming,
                            has_livestock = it.has_livestock,
                            has_household_goods = it.has_household_goods,
                            affected_by_conflict = it.affected_by_conflict,
                            affected_by_epidemic = it.affected_by_epidemic,
                            affected_by_climate_shock = it.affected_by_climate_shock,
                            take_children_out_of_school = it.take_children_out_of_school,
                            sale_of_production_assets = it.sale_of_production_assets,
                            use_of_child_labor = it.use_of_child_labor,
                            use_of_early_marriage = it.use_of_early_marriage,
                            give_up_health_care = it.give_up_health_care,
                            days_spent_other_coping_strategy = it.days_spent_other_coping_strategy,
                            number_of_months_displaced_returned_repatriated_refugee = it.number_of_months_displaced_returned_repatriated_refugee,
                            gps_longitude = it.gps_longitude,
                            gps_latitude = it.gps_latitude,
                            start_time = it.start_time,
                            finish_time = it.finish_time,
                            address = it.address,
                            comments = it.comments,
                            profile_picture = it.profile_picture,
                            mobile_money_username = it.mobile_money_username,
                            mobile_money_phone_number = it.mobile_money_phone_number,
                            village_or_quartier = it.village_or_quartier,
                            other_household_migration_status = it.other_household_migration_status,
                            other_occupation_status_of_current_accommodation = it.other_occupation_status_of_current_accommodation,
                            other_main_material_of_exterior_walls = it.other_main_material_of_exterior_walls,
                            other_main_soil_material = it.other_main_soil_material,
                            occupation_status_of_current_accommodation = it.occupation_status_of_current_accommodation,
                            main_material_of_exterior_walls = it.main_material_of_exterior_walls,
                            main_soil_material = it.main_soil_material,
                            number_of_rooms_used_for_sleeping = it.number_of_rooms_used_for_sleeping,
                            type_of_fuel_used_for_household_cooking = it.type_of_fuel_used_for_household_cooking,
                            other_type_of_fuel_used_for_household_cooking = it.other_type_of_fuel_used_for_household_cooking,
                            main_source_of_household_drinking_water = it.main_source_of_household_drinking_water,
                            other_main_source_of_household_drinking_water = it.other_main_source_of_household_drinking_water,
                            type_of_household_toilet = it.type_of_household_toilet,
                            other_type_of_household_toilet = it.other_type_of_household_toilet,
                            method_of_waste_disposal = it.method_of_waste_disposal,
                            place_to_wash_hands = it.place_to_wash_hands,
                            minimum_monthly_income_necessary_live_without_difficulties = it.minimum_monthly_income_necessary_live_without_difficulties,
                            household_monthly_income = it.household_monthly_income,
                            number_of_goat_owned = it.number_of_goat_owned,
                            number_of_sheep_owned = it.number_of_sheep_owned,
                            number_of_cow_owned = it.number_of_cow_owned,
                            number_of_rabbit_owned = it.number_of_rabbit_owned,
                            number_of_pigs_owned = it.number_of_pigs_owned,
                            number_of_poultry_owned = it.number_of_poultry_owned,
                            number_of_guinea_pig_owned = it.number_of_guinea_pig_owned,
                            number_of_cars_owned = it.number_of_cars_owned,
                            number_of_motorbike_owned = it.number_of_motorbike_owned,
                            number_of_bicycle_owned = it.number_of_bicycle_owned,
                            number_of_cart_owned = it.number_of_cart_owned,
                            number_of_plow_owned = it.number_of_plow_owned,
                            number_of_wheelbarrow_owned = it.number_of_wheelbarrow_owned,
                            number_of_rickshaw_owned = it.number_of_rickshaw_owned,
                            number_of_mattress_owned = it.number_of_mattress_owned,
                            number_of_fridge_owned = it.number_of_fridge_owned,
                            number_of_fan_owned = it.number_of_fan_owned,
                            number_of_air_conditioner_owned = it.number_of_air_conditioner_owned,
                            number_of_radio_owned = it.number_of_radio_owned,
                            number_of_television_owned = it.number_of_television_owned,
                            number_of_canal_tnt_tv_cable_owned = it.number_of_canal_tnt_tv_cable_owned,
                            number_of_dvd_driver_owned = it.number_of_dvd_driver_owned,
                            number_of_handset_or_phone_owned = it.number_of_handset_or_phone_owned,
                            number_of_computer_owned = it.number_of_computer_owned,
                            number_of_oil_stove_owned = it.number_of_stove_or_oven_owned,
                            number_of_stove_or_oven_owned = it.number_of_oil_stove_owned,
                            number_of_solar_plate_owned = it.number_of_solar_plate_owned,
                            number_of_sewing_machine_owned = it.number_of_sewing_machine_owned,
                            number_of_electric_iron_owned = it.number_of_electric_iron_owned,
                            number_of_charcoal_iron_owned = it.number_of_charcoal_iron_owned,
                            number_of_bed_owned = it.number_of_bed_owned,
                            number_of_table_owned = it.number_of_table_owned,
                            number_of_chair_owned = it.number_of_chair_owned,
                            number_of_sofa_owned = it.number_of_sofa_owned,
                            number_of_mosquito_nets_owned = it.number_of_mosquito_nets_owned,
                            amount_of_cultivable_land_owned = it.amount_of_cultivable_land_owned,
                            number_of_meals_eaten_by_adults_18_plus_yesterday = it.number_of_meals_eaten_by_adults_18_plus_yesterday,
                            number_of_meals_eaten_by_children_6_to_17_yesterday = it.number_of_meals_eaten_by_children_6_to_17_yesterday,
                            number_of_meals_eaten_by_children_2_to_5_yesterday = it.number_of_meals_eaten_by_children_2_to_5_yesterday,
                            number_of_days_in_week_consumed_staple_foods = it.number_of_days_in_week_consumed_staple_foods,
                            number_of_days_in_week_consumed_legumes_or_nuts = it.number_of_days_in_week_consumed_legumes_or_nuts,
                            number_of_days_in_week_consumed_dairy_products = it.number_of_days_in_week_consumed_dairy_products,
                            number_of_days_in_week_consumed_meat = it.number_of_days_in_week_consumed_meat,
                            number_of_days_in_week_consumed_vegetables = it.number_of_days_in_week_consumed_vegetables,
                            number_of_days_in_week_consumed_fruits = it.number_of_days_in_week_consumed_fruits,
                            number_of_days_in_week_consumed_cooking_oils = it.number_of_days_in_week_consumed_cooking_oils,
                            number_of_days_in_week_consumed_sugar_or_sweet_products = it.number_of_days_in_week_consumed_sugar_or_sweet_products,
                            days_spent_reduce_amount_consumed_coping_strategy = it.days_spent_reduce_amount_consumed_coping_strategy,
                            days_spent_reduce_meals_consumed_coping_strategy = it.days_spent_reduce_meals_consumed_coping_strategy,
                            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = it.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy,
                            days_spent_eat_less_expensively_coping_strategy = it.days_spent_eat_less_expensively_coping_strategy,
                            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = it.days_spent_borrow_food_or_rely_on_family_help_coping_strategy,
                            days_spent_days_without_eating_coping_strategy = it.days_spent_days_without_eating_coping_strategy,
                            days_spent_begging_coping_strategy = it.days_spent_begging_coping_strategy,
                            days_spent_consume_wild_food_coping_strategy = it.days_spent_consume_wild_food_coping_strategy,
                            other_household_activities_in_past_12_months = it.other_household_activities_in_past_12_months,
                            other_method_of_waste_disposal = it.other_method_of_waste_disposal,
                            other_livestock_owned = it.other_livestock_owned,
                            household_member_with_benefit_from_social_assistance_program = it.household_member_with_benefit_from_social_assistance_program,
                            name_of_social_assistance_program = it.name_of_social_assistance_program,
                            affected_by_other_shock = it.affected_by_other_shock,
                            respondent_sex = it.respondent_sex,
                            respondent_voter_id = it.respondent_voter_id,


                            )
                    }
                }.collectLatest {
                    _households.value = it
                }
        }
    }




//    fun onAddRegistrationFabClicked() = viewModelScope.launch {
//        householdsEventChannel.send(HouseholdEvent.AddRegistrationClicked)
//    }
//
//    fun onHumanitarianFabClicked() = viewModelScope.launch {
//        householdsEventChannel.send(HouseholdEvent.HumanitarianClicked)
//    }

    fun onDevelopmentalFabClicked() = viewModelScope.launch {
        _householdsEventChannel.send(HouseholdEvent.DevelopmentalClicked)
    }


    fun onHouseholdResult(result: Int) {
        when (result) {
            ADD_HOUSEHOLD_RESULT_OK -> showHouseholdSavedConfirmationMessage()
            DELETE_HOUSEHOLD_RESULT_OK -> showHouseholdDeletedConfirmationMessage()
        }
    }


    private fun showHouseholdSavedConfirmationMessage() =
        viewModelScope.launch {

            _householdsEventChannel.send(
                HouseholdEvent.ShowSnackMessage(
                    "Household saved!"
                )
            )
        }

    private fun showHouseholdDeletedConfirmationMessage() =
        viewModelScope.launch {
            _householdsEventChannel.send(
                HouseholdEvent.ShowSnackMessage(
                    "Household deleted!"
                )
            )
        }

    fun onHouseholdSelected(householdModel: HouseholdModel) = viewModelScope.launch {
        _householdsEventChannel.send(HouseholdEvent.NavigateToHouseholdDetailScreen(householdModel))
    }
    fun onUploadMenuItemClicked() = viewModelScope.launch {
//        householdRepository.uploadBulkHouseholds()
        loadState()
    }
    private fun loadState() = viewModelScope.launch {
        householdRepository.bulkUploadHouseholdsFlow().collectLatest { results ->

            when (results) {
                is Resource.Loading -> _householdsEventChannel.send(HouseholdEvent.Loading)
                is Resource.Success -> {
                    val data = results.data as BulkUploadResponse
                    val msg =
                        "${data.data?.households?.size} households and ${data.data?.members?.size} members uploaded!"
                    _householdsEventChannel.send(HouseholdEvent.Successful(msg))
                }

                is Resource.Failure -> {
                    val msg = results.error as ErrorResponse
                    _householdsEventChannel.send(HouseholdEvent.Failure(msg.msg))
                }

                is Resource.Exception -> {
                    results.throwable.localizedMessage?.let { errorMsg ->
                        HouseholdEvent.Exception(
                            errorMsg
                        )
                    }?.let { _householdsEventChannel.send(it) }
                }

            }

        }

    }


    sealed class HouseholdEvent {
        object AddRegistrationClicked : HouseholdEvent()
        object DevelopmentalClicked : HouseholdEvent()
        object HumanitarianClicked : HouseholdEvent()
        data class ShowSnackMessage(val msg: String) : HouseholdEvent()

        data class Exception(val errMsg: String) : HouseholdEvent()
        data class Successful(val errMsg: String) : HouseholdEvent()
        data class Failure(val errMsg: String) : HouseholdEvent()
        object Loading : HouseholdEvent()

        data class NavigateToHouseholdDetailScreen(val householdModel: HouseholdModel) :
            HouseholdEvent()
    }


}