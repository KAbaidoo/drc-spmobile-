package io.bewsys.spmobile.ui.households.forms

import android.app.Application
import android.os.Build

import androidx.annotation.RequiresApi
import androidx.lifecycle.*

import androidx.work.*
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.work.HouseholdUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.UUID.randomUUID


class SharedDevelopmentalFormViewModel(
    application: Application,
    private val state: SavedStateHandle,
    private val householdRepository: HouseholdRepository,
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    private val workManager = WorkManager.getInstance(application)

    private val addHouseholdEventChannel = Channel<AddDevelopmentalHouseholdEvent>()
    val addDevelopmentalHouseholdEvent = addHouseholdEventChannel.receiveAsFlow()


    private val _provinces = MutableLiveData<List<String>>()
    val provinces: LiveData<List<String>>
        get() = _provinces

    private val _communities = MutableLiveData<List<String>>()
    val communities: LiveData<List<String>>
        get() = _communities

    private val _territories = MutableLiveData<List<String>>()
    val territories: LiveData<List<String>>
        get() = _territories

    private val _groupments = MutableLiveData<List<String>>()
    val groupments: LiveData<List<String>>
        get() = _groupments

    init {
        loadProvinces()
//        loadCommunities()
    }

    var province = state.get<String>("province") ?: ""
        set(value) {
            field = value
            state["province"] = value
            getProvinceId()
            loadTerritories()
        }
    var territory = state.get<String>("territory") ?: ""
        set(value) {
            field = value
            state["territory"] = value
            getTerritoryId()
            loadCommunities()
        }
    var community = state.get<String>("community") ?: ""
        set(value) {
            field = value
            state["community"] = value
            getCommunityId()
            loadGroupments()
        }
    var groupment = state.get<String>("groupment") ?: ""
        set(value) {
            field = value
            state["groupment"] = value
            getGroupmentId()
        }

    private var provinceId: String = state.get<String>("province_id") ?: "1"
        set(value) {
            field = value
            state["province_id"] = value
        }

    private var communityId: String = state.get<String>("community_id") ?: "1"
        set(value) {
            field = value
            state["community_id"] = value
        }

    private var groupmentId: String = state.get<String>("groupment_id") ?: "1"
        set(value) {
            field = value
            state["groupment_id"] = value
        }

    private var territoryId: String = state.get<String>("territory_id") ?: "1"
        set(value) {
            field = value
            state["territory_id"] = value
        }

    private fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getProvincesList().collectLatest {
                _provinces.value = it
            }
        }
    }

    fun loadTerritories() {
        viewModelScope.launch {
            dashboardRepository.getTerritoriesList(provinceId).collectLatest {
                _territories.value = it
            }
        }
    }

    fun loadCommunities() {
        viewModelScope.launch {
            dashboardRepository.getCommunitiesList(territoryId).collectLatest {
                _communities.value = it
            }
        }
    }

    fun loadGroupments() {
        viewModelScope.launch {
            dashboardRepository.getGroupmentsList(communityId).collectLatest {
                _groupments.value = it
            }
        }
    }

    private val provinceQuery = state.getStateFlow("province", "").flatMapLatest {
        dashboardRepository.getProvinceByName(it)
    }

    fun getProvinceId() {
        viewModelScope.launch {
            provinceQuery.collect {
                provinceId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val communityQuery = state.getStateFlow("community", "").flatMapLatest {
        dashboardRepository.getCommunityByName(it)
    }

    fun getCommunityId() {
        viewModelScope.launch {
            communityQuery.collect {
                communityId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val territoryQuery = state.getStateFlow("territory", "").flatMapLatest {
        dashboardRepository.getTerritoryByName(it)
    }

    fun getTerritoryId() {
        viewModelScope.launch {
            territoryQuery.collect {
                territoryId = it.firstOrNull()?.id.toString()
            }
        }
    }

    private val groupmentQuery = state.getStateFlow("groupment", "").flatMapLatest {
        dashboardRepository.getGroupmentByName(it)
    }

    fun getGroupmentId() {
        viewModelScope.launch {
            groupmentQuery.collect {
                groupmentId = it.firstOrNull()?.id.toString()
            }
        }
    }


    private val _entriesMap = mutableMapOf<String, String>()

    fun saveEntry(title: String, answer: String) {
        val key = title.lowercase().split(' ').joinToString("_")
        _entriesMap[key] = answer
    }

    fun getEntry(title: String): String {
        val key = title.lowercase().split(' ').joinToString("_")
        return _entriesMap[key] ?: ""
    }

    fun clearEntries() {
        _entriesMap.clear()
    }


    fun setStartTime() {
        _entriesMap["start_time"] = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
    }


    fun onRegisterClicked() {

        HouseholdModel(
            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = _entriesMap["initial_registration_type"] ?: "",
            respondent_firstname = _entriesMap["respondent_firstname"] ?: "",
            respondent_middlename = _entriesMap["respondent_middlename"] ?: "",
            respondent_lastname = _entriesMap["respondent_lastname"] ?: "",
            respondent_dob = _entriesMap["respondent_dob"] ?: "",
            respondent_family_bond_to_head = if (_entriesMap["respondent_family_bond_to_head"] == "yes") 1 else 0,
            respondent_voter_id = _entriesMap["respondent_voter_id"] ?: "",
            respondent_phone_number = _entriesMap["respondent_phone_number"] ?: "",
            household_head_firstname = _entriesMap["household_head_firstname"] ?: "",
            household_head_lastname = _entriesMap["household_head_lastname"] ?: "",
            household_head_middlename = _entriesMap["household_head_middlename"] ?: "",
            household_head_dob = _entriesMap["household_head_dob"] ?: "",
            household_head_sex = _entriesMap["household_head_sex"] ?: "",
            head_age_known = _entriesMap["head_age_known"] ?: "",
            head_dob_known = _entriesMap["head_dob_known"] ?: "",
            household_head_voter_id_card = _entriesMap["household_head_voter_id_card"] ?: "",
            household_head_phone_number = _entriesMap["household_head_phone_number"] ?: "",
            household_head_age = _entriesMap["household_head_age"] ?: "",
            household_head_marital_status_id = _entriesMap["household_head_marital_status_id"] ?: "",
            household_head_birth_certificate = _entriesMap["household_head_birth_certificate"] ?: "",
            household_head_educational_level_id = _entriesMap["household_head_educational_level_id"] ?: "",
            household_head_disability_id = _entriesMap["household_head_disability_id"] ?: "",
            household_head_socio_professional_category_id = _entriesMap["household_head_socio_professional_category_id"] ?: "",
            household_head_school_attendance_id = _entriesMap["household_head_school_attendance_id"] ?: "",
            household_head_sector_of_work_id = _entriesMap["household_head_sector_of_work_id"] ?: "",
            household_head_pregnancy_status = _entriesMap["household_head_pregnancy_status"] ?: "",
            unit_of_migration_duration = _entriesMap["unit_of_migration_duration"] ?: "",
            territory_or_town = _entriesMap["territory_or_town"] ?: "",
            bank_account_or_bank_card_available = _entriesMap["bank_account_or_bank_card_available"] ?: "",
            is_income_regular = _entriesMap["is_income_regular"] ?: "",
            household_member_access_to_cultivable_land = _entriesMap["household_member_access_to_cultivable_land"] ?: "",
            household_member_owner_of_cultivable_land = _entriesMap["household_member_owner_of_cultivable_land"] ?: "",
            practice_of_cash_crop_farming_or_commercial_farming = _entriesMap["practice_of_cash_crop_farming_or_commercial_farming"] ?: "",
            has_livestock = _entriesMap["has_livestock"] ?: "",
            has_household_goods = _entriesMap["has_household_goods"] ?: "",
            affected_by_conflict = _entriesMap["affected_by_conflict"] ?: "",
            affected_by_epidemic = _entriesMap["affected_by_epidemic"] ?: "",
            affected_by_climate_shock = _entriesMap["affected_by_climate_shock"] ?: "",
            take_children_out_of_school = _entriesMap["take_children_out_of_school"] ?: "",
            sale_of_production_assets = _entriesMap["sale_of_production_assets"] ?: "",
            use_of_child_labor = _entriesMap["use_of_child_labor"] ?: "",
            use_of_early_marriage = _entriesMap["use_of_early_marriage"] ?: "",
            give_up_health_care = _entriesMap["give_up_health_care"] ?: "",
            days_spent_other_coping_strategy = _entriesMap["days_spent_other_coping_strategy"] ?: "",
            gps_longitude = _entriesMap["gps_longitude"] ?: "",
            gps_latitude = _entriesMap["gps_latitude"] ?: "",
            start_time = _entriesMap["start_time"] ?: "",
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = _entriesMap["address"] ?: "",
            comments = _entriesMap["comments"] ?: "",
            profile_picture = _entriesMap["profile_picture"] ?: "",
            mobile_money_username = _entriesMap["mobile_money_username"] ?: "",
            mobile_money_phone_number = _entriesMap["mobile_money_phone_number"] ?: "",
            village_or_quartier = _entriesMap["village_or_quartier"] ?: "",
            other_household_migration_status = _entriesMap["other_household_migration_status"] ?: "",
            other_occupation_status_of_current_accommodation = _entriesMap["other_occupation_status_of_current_accommodation"] ?: "",
            other_main_material_of_exterior_walls = _entriesMap["other_main_material_of_exterior_walls"] ?: "",
            other_main_soil_material = _entriesMap["other_main_soil_material"] ?: "",
            other_type_of_fuel_used_for_household_cooking = _entriesMap["other_type_of_fuel_used_for_household_cooking"] ?: "",
            other_main_source_of_household_drinking_water = _entriesMap["other_main_source_of_household_drinking_water"] ?: "",
            other_type_of_household_toilet = _entriesMap["other_type_of_household_toilet"] ?: "",
            other_method_of_waste_disposal = _entriesMap["other_method_of_waste_disposal"] ?: "",
            other_livestock_owned = _entriesMap["other_livestock_owned"] ?: "",
            household_member_with_benefit_from_social_assistance_program = _entriesMap["household_member_with_benefit_from_social_assistance_program"] ?: "",
            name_of_social_assistance_program = _entriesMap["name_of_social_assistance_program"] ?: "",
            affected_by_other_shock = _entriesMap["affected_by_other_shock"] ?: "",
            other_household_activities_in_past_12_months = _entriesMap["other_household_activities_in_past_12_months"] ?: "",
            duration_displaced_returned_repatriated_refugee = _entriesMap["duration_displaced_returned_repatriated_refugee"]?.toLong() ?: 0,
            number_of_months_displaced_returned_repatriated_refugee = _entriesMap["number_of_months_displaced_returned_repatriated_refugee"]?.toLong() ?: 0,
            number_of_meals_eaten_by_children_6_to_17_yesterday = _entriesMap["number_of_meals_eaten_by_children_6_to_17_yesterday"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_sugar_or_sweet_products = _entriesMap["number_of_days_in_week_consumed_sugar_or_sweet_products"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_vegetables = _entriesMap["number_of_days_in_week_consumed_vegetables"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_staple_foods = _entriesMap["number_of_days_in_week_consumed_staple_foods"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_meat = _entriesMap["number_of_days_in_week_consumed_meat"]?.toLong() ?: 0,
            number_of_bicycle_owned = _entriesMap["number_of_bicycle_owned"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_legumes_or_nuts = _entriesMap["number_of_days_in_week_consumed_legumes_or_nuts"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_fruits = _entriesMap["number_of_days_in_week_consumed_fruits"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_dairy_products = _entriesMap["number_of_days_in_week_consumed_dairy_products"]?.toLong() ?: 0,
            number_of_days_in_week_consumed_cooking_oils = _entriesMap["number_of_days_in_week_consumed_dairy_products"]?.toLong() ?: 0,
            number_of_meals_eaten_by_children_2_to_5_yesterday = _entriesMap["number_of_meals_eaten_by_children_2_to_5_yesterday"]?.toLong() ?: 0,
            number_of_mosquito_nets_owned = _entriesMap["number_of_mosquito_nets_owned"]?.toLong() ?: 0,
            number_of_guinea_pig_owned = _entriesMap["number_of_guinea_pig_owned"]?.toLong() ?: 0,
            number_of_poultry_owned = _entriesMap["number_of_poultry_owned"]?.toLong() ?: 0,
            number_of_oil_stove_owned = _entriesMap["number_of_oil_stove_owned"]?.toLong() ?: 0,
            number_of_rabbit_owned = _entriesMap["number_of_rabbit_owned"]?.toLong() ?: 0,
            number_of_sheep_owned = _entriesMap["number_of_sheep_owned"]?.toLong() ?: 0,
            number_of_motorbike_owned = _entriesMap["number_of_motorbike_owned"]?.toLong() ?: 0,
            number_of_rooms_used_for_sleeping = _entriesMap["number_of_rooms_used_for_sleeping"]?.toLong()?: 0,
            number_of_wheelbarrow_owned = _entriesMap["number_of_wheelbarrow_owned"]?.toLong() ?: 0,
            number_of_radio_owned = _entriesMap["number_of_radio_owned"]?.toLong() ?: 0,
            number_of_stove_or_oven_owned = _entriesMap["number_of_stove_or_oven_owned"]?.toLong() ?: 0,
            number_of_rickshaw_owned = _entriesMap["number_of_rickshaw_owned"]?.toLong() ?: 0,
            number_of_solar_plate_owned = _entriesMap["number_of_solar_plate_owned"]?.toLong() ?: 0,
            number_of_sewing_machine_owned = _entriesMap["number_of_sewing_machine_owned"]?.toLong() ?: 0,
            number_of_mattress_owned = _entriesMap["number_of_mattress_owned"]?.toLong() ?: 0,
            number_of_television_owned = _entriesMap["number_of_television_owned"]?.toLong() ?: 0,
            number_of_table_owned = _entriesMap["number_of_table_owned"]?.toLong() ?: 0,
            number_of_sofa_owned = _entriesMap["number_of_sofa_owned"]?.toLong() ?: 0,
            number_of_handset_or_phone_owned = _entriesMap["number_of_handset_or_phone_owned"]?.toLong() ?: 0,
            number_of_electric_iron_owned = _entriesMap["number_of_electric_iron_owned"]?.toLong() ?: 0,
            number_of_plow_owned = _entriesMap["number_of_plow_owned"]?.toLong() ?: 0,
            number_of_goat_owned = _entriesMap["number_of_goat_owned"]?.toLong() ?: 0,
            number_of_fridge_owned = _entriesMap["number_of_fridge_owned"]?.toLong() ?: 0,
            number_of_dvd_driver_owned = _entriesMap["number_of_dvd_driver_owned"]?.toLong() ?: 0,
            number_of_pigs_owned = _entriesMap["number_of_pigs_owned"]?.toLong() ?: 0,
            number_of_fan_owned = _entriesMap["number_of_fan_owned"]?.toLong() ?: 0,
            number_of_canal_tnt_tv_cable_owned = _entriesMap["number_of_canal_tnt_tv_cable_owned"]?.toLong() ?: 0,
            number_of_cow_owned = _entriesMap["number_of_cow_owned"]?.toLong() ?: 0,
            number_of_meals_eaten_by_adults_18_plus_yesterday = _entriesMap["number_of_meals_eaten_by_adults_18_plus_yesterday"]?.toLong() ?: 0,
            number_of_charcoal_iron_owned = _entriesMap["number_of_charcoal_iron_owned"]?.toLong() ?: 0,
            number_of_computer_owned = _entriesMap["number_of_computer_owned"]?.toLong() ?: 0,
            number_of_chair_owned = _entriesMap["number_of_chair_owned"]?.toLong() ?: 0,
            number_of_cart_owned = _entriesMap["number_of_cart_owned"]?.toLong() ?: 0,
            number_of_cars_owned = _entriesMap["number_of_cars_owned"]?.toLong() ?: 0,
            number_of_bed_owned = _entriesMap["number_of_bed_owned"]?.toLong() ?: 0,
            number_of_air_conditioner_owned = _entriesMap["number_of_air_conditioner_owned"]?.toLong() ?: 0,
            days_spent_reduce_meals_consumed_coping_strategy = _entriesMap["days_spent_reduce_meals_consumed_coping_strategy"]?.toLong() ?: 0,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = _entriesMap["days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy"]?.toLong() ?: 0,
            days_spent_reduce_amount_consumed_coping_strategy = _entriesMap["days_spent_reduce_amount_consumed_coping_strategy"]?.toLong() ?: 0,
            days_spent_eat_less_expensively_coping_strategy = _entriesMap["days_spent_eat_less_expensively_coping_strategy"]?.toLong() ?: 0,
            days_spent_days_without_eating_coping_strategy = _entriesMap["days_spent_days_without_eating_coping_strategy"]?.toLong() ?: 0,
            days_spent_consume_wild_food_coping_strategy = _entriesMap["days_spent_consume_wild_food_coping_strategy"]?.toLong() ?: 0,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = _entriesMap["days_spent_borrow_food_or_rely_on_family_help_coping_strategy"]?.toLong() ?: 0,
            days_spent_begging_coping_strategy = _entriesMap["days_spent_begging_coping_strategy"]?.toLong() ?: 0,
            amount_of_cultivable_land_owned = _entriesMap["amount_of_cultivable_land_owned"]?.toLong() ?: 0,
            minimum_monthly_income_necessary_live_without_difficulties = _entriesMap["minimum_monthly_income_necessary_live_without_difficulties"]?.toLong() ?: 0,
            household_monthly_income = _entriesMap["household_monthly_income"]?.toLong() ?: 0,
            is_head_respondent = if (_entriesMap["is_head_respondent"] == "yes") 1 else 0,
            consent = _entriesMap["consent"] ?: "",
            household_status = _entriesMap["household_status"] ?: "",
            method_of_waste_disposal = if (_entriesMap["method_of_waste_disposal"] == "yes") 1 else 0,
            household_migration_status = if (_entriesMap["household_migration_status"] == "yes") 1 else 0,
            place_to_wash_hands = if (_entriesMap["place_to_wash_hands"] == "yes") 1 else 0,
            survey_no = "$provinceId$territoryId$communityId$groupmentId}",
            temp_survey_no = "SQE211",
            main_material_of_exterior_walls = 1,
            occupation_status_of_current_accommodation = 1,
            type_of_fuel_used_for_household_cooking = 1,
            main_soil_material = 1,
            main_source_of_household_drinking_water = 1,
            type_of_household_toilet = 1,
            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,
            cac = 1,
            area_of_residence = "1",
            health_area_id = "1",
            health_zone_id = "1",
            respondent_type = 1,
        ).also {
            addHousehold(it)
        }
    }

    private fun addHousehold(newHouseholdModel: HouseholdModel) = viewModelScope.launch {
        householdRepository.insertHousehold(newHouseholdModel)

//upload with work manager
        uploadNonConsentingHousehold(householdRepository.getLastInsertedRowId())
        addHouseholdEventChannel.send(
            AddDevelopmentalHouseholdEvent.NavigateBackWithResults(
                ADD_HOUSEHOLD_RESULT_OK
            )
        )
    }

    private fun uploadNonConsentingHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<HouseholdUploadWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)

    }

    private fun createInputDataForId(id: Long): Data {
        val builder = Data.Builder()
        builder.putLong(KEY_DATA_ID, id)
        return builder.build()
    }


    sealed class AddDevelopmentalHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddDevelopmentalHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddDevelopmentalHouseholdEvent()
//        data class NavigateBack(val results: Int) : AddDevelopmentalHouseholdEvent()
    }
}