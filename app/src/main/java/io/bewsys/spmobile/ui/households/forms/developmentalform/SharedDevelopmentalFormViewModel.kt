package io.bewsys.spmobile.ui.households.forms.developmentalform

import android.app.Application
import android.util.Log

import androidx.lifecycle.*

import androidx.work.*
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.work.HouseholdUploadWorker
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.random.Random


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
    }
    //      step one
    private val _yesConsent = MutableStateFlow("")
    val yesConsent: StateFlow<String>
        get() = _yesConsent

    var consent: String = ""
        set(value) {
            field = value
            _yesConsent.value = value
        }

    //      step two
    var initialRegistrationType: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var respondentFirstName: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var respondentMiddleName: String = ""
    var respondentLastName: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var respondentFamilyBondToHead: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var respondentDOBKnown: String? = null
    var respondentDOB: String = ""
    var respondentAgeKnown: String? = null
    var respondentAge: String = ""
    var respondentSex: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }

    var respondentVoterId: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var respondentPhoneNo: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var province = ""
        set(value) {
            field = value
            getProvinceId()
            loadTerritories()
        }
    var territory = ""
        set(value) {
            field = value
            getTerritoryId()
            loadCommunities()
        }
    var community = ""
        set(value) {
            field = value
            getCommunityId()
            loadGroupments()
        }
    var groupment = ""
        set(value) {
            field = value
            getGroupmentId()
        }
    var healthZone = ""
    var healthArea = ""
    private var provinceId: String = "1"
    private var communityId: String = "1"
    private var groupmentId: String = "1"
    private var territoryId: String = "1"
    private var healthAreaId: String = "1"
    private var healthZoneId: String = "1"
    var address: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var villageOrQuartier: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var territoryOrTown: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var areaOfResidence: String = ""
        set(value) {
            field = value
            stepTwoHasBlankFields()
        }
    var lon = ""
    var lat = ""




    private val _stepTwoHasBlank = MutableStateFlow(true)
    val stepTwoHasBlankFields: StateFlow<Boolean>
        get() = _stepTwoHasBlank

    fun stepTwoHasBlankFields() {
        _stepTwoHasBlank.value = hasBlank(
            respondentSex,
            address,
            areaOfResidence,
            villageOrQuartier,
            territoryOrTown,
            initialRegistrationType,
            respondentFirstName,
            respondentLastName,
            respondentFamilyBondToHead,
            respondentVoterId,
            respondentPhoneNo
        )
    }


    //    step three
    var headIsRespondent: String = ""
    var headFirstName: String = ""
    var headMiddleName: String = ""
    var headLastName: String = ""
    var headAgeKnown: String? = null
    var headAge: String = ""
    var headDOBKnown: String? = null
    var headSex: String = ""
    var headDOB: String = ""
    var headPregnancyStatus: String = ""
    var headMaritalStatus: String = ""
    var headVoterId: String = ""
    var headPhoneNo: String = ""
    var headBirthCert: String = ""
    var headEduLevel: String = ""
    var headSocioProfessionalCategory: String = ""
    var headSchoolAttendance: String = ""
    var headSectorOfWork: String = ""
    var headDisability: String = ""

    val fields = mutableListOf(
        headFirstName,
        headMiddleName,
        headLastName,
        headAge,
        headDOB,
        headVoterId,
        headPhoneNo,
        headBirthCert,
        headEduLevel,
        headSocioProfessionalCategory,
        headSchoolAttendance,
        headSectorOfWork,
        headDisability
    )

    private val _stepThreeHasBlank = MutableStateFlow(true)
    val stepThreeHasBlankFields: StateFlow<Boolean>
        get() = _stepThreeHasBlank

    fun stepThreeHasBlankFields() {
        _stepThreeHasBlank.value = hasBlank(
            headBirthCert,
            headEduLevel,
            headSocioProfessionalCategory,
            headSchoolAttendance,
            headSectorOfWork
        )

    }

    //    step four household
    var isIncomeRegular: String = ""
    var bankCardOrAccount: String = ""
    var benefitFromSocialAssistanceProgram: String = ""
    var nameOfSocialAssistanceProgram: String = ""
    var migrationStatus: String = ""
    var otherMigrationStatus: String = ""
    var unitOfMigrationDuration: String = ""

    var durationDisplacedReturnedRepatriatedRefugee: String = ""
    var unitOfMigrationStatus: String = ""
    var householdMonthlyIncome: String = ""
    var minimumMonthlyIncomeNecessaryLiveWithoutDifficulties: String = ""
    var mobileMoneyUsername: String = ""
    var mobileMoneyPhoneNumber: String = ""

    val stepFourFields = mutableListOf(
        nameOfSocialAssistanceProgram,
        migrationStatus,
        otherMigrationStatus,
        durationDisplacedReturnedRepatriatedRefugee,
        unitOfMigrationStatus,
        householdMonthlyIncome,
        minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
        mobileMoneyUsername,
        mobileMoneyPhoneNumber
    )

    private val _stepFourHasBlankFields = MutableStateFlow(true)
    val stepFourHasBlankFields: StateFlow<Boolean>
        get() = _stepFourHasBlankFields

    fun stepFourHasBlankFields() {
        _stepFourHasBlankFields.value = hasBlank(
            householdMonthlyIncome,
            minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            mobileMoneyUsername,
            mobileMoneyPhoneNumber
        )

    }

//    Step Five
//      rbs
    var affectedByConflict: String = ""
    var affectedByEpidemic: String = ""
    var affectedByClimateShock: String = ""
    var affectedByOtherShock: String = ""
    var takeChildrenOutOfSchool: String = ""
    var useOfChildLabour: String = ""
    var useOfEarlyMarriage: String = ""
    var gaveUpHealthCare: String = ""
    var saleOfProductionAssets: String = ""
//    til
    var daysReducedMealsConsumed: String = ""
    var daysReducedMealsAdult: String = ""
    var daysReducedAmountConsumed: String = ""
    var daysEatLessExpensively: String = ""
    var daysWithoutEating: String = ""
    var daysConsumedWildFood: String = ""
    var daysBorrowFood: String = ""
    var daysBeggingFood: String = ""
    var daysOtherCoping: String = ""
    var numberOfMealsChildren6To17: String = ""
    var numberOfMealsChildren2To5: String = ""
    var numberOfMealsAdults18plus: String = ""
    var daysConsumedSugarOrSweets: String = ""
    var daysConsumedStapleFoods: String = ""
    var daysConsumedVegetables: String = ""
    var daysConsumedMeat: String = ""
    var daysConsumedLegumes: String = ""
    var daysConsumedFruits: String = ""
    var daysConsumedDiary: String = ""
    var daysConsumedCookingOils: String = ""

    val stepFiveFields = mutableListOf(
        daysReducedMealsConsumed,
        daysReducedMealsAdult,
        daysReducedAmountConsumed,
        daysEatLessExpensively,
        daysWithoutEating,
        daysConsumedWildFood,
        daysBorrowFood,
        daysBeggingFood,
        daysOtherCoping,
        numberOfMealsChildren6To17,
        numberOfMealsChildren2To5,
        numberOfMealsAdults18plus,
        daysConsumedSugarOrSweets,
        daysConsumedStapleFoods,
        daysConsumedVegetables,
        daysConsumedMeat,
        daysConsumedLegumes,
        daysConsumedFruits,
        daysConsumedDiary,
        daysConsumedCookingOils
    )

    private val _stepFiveHasBlankFields = MutableStateFlow(true)
    val stepFiveHasBlankFields: StateFlow<Boolean>
        get() = _stepFiveHasBlankFields

    fun stepFiveHasBlankFields() {
        _stepFiveHasBlankFields.value = hasBlank(
            daysReducedMealsConsumed,
            daysReducedMealsAdult,
            daysReducedAmountConsumed,
            daysEatLessExpensively,
            daysWithoutEating,
            daysConsumedWildFood,
            daysBorrowFood,
            daysBeggingFood,
            daysOtherCoping,
            numberOfMealsChildren6To17,
            numberOfMealsChildren2To5,
            numberOfMealsAdults18plus,
            daysConsumedSugarOrSweets,
            daysConsumedStapleFoods,
            daysConsumedVegetables,
            daysConsumedMeat,
            daysConsumedLegumes,
            daysConsumedFruits,
            daysConsumedDiary,
            daysConsumedCookingOils
        )
    }

    /*      Form Six    */
    var hasLiveStock: String = ""
    var hasHouseholdGoods: String = ""
    var accessToCultivableLand: String = ""
    var ownerOfCultivableLand: String = ""
    var cashCropOrCommercialFarming: String = ""



    val randomID = List(8) { Random.nextInt(0, 10) }.joinToString(separator = "")


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
    /*

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
*/
    var startTime: String = ""

    fun setStartTime() {
        startTime = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
    }

    fun onRegisterClicked() {

        HouseholdModel(
            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = initialRegistrationType,
            respondent_firstname = respondentFirstName,
            respondent_middlename = respondentMiddleName,
            respondent_lastname = respondentLastName,
            respondent_dob = respondentDOB,
            respondent_family_bond_to_head = respondentFamilyBondToHead,
            respondent_voter_id = respondentVoterId,
            respondent_phone_number = respondentPhoneNo,
            household_head_firstname = headFirstName,
            household_head_lastname = headLastName,
            household_head_middlename = headMiddleName,
            household_head_dob = headDOB,
            household_head_sex = headSex,
            head_age_known = headAgeKnown,
            head_dob_known = headDOBKnown,
            is_head_respondent = headIsRespondent,
            household_head_voter_id_card = headVoterId,
            household_head_phone_number = headPhoneNo,
            household_head_age = headAge,
            household_head_marital_status_id = headMaritalStatus,
            household_head_birth_certificate = headBirthCert,
            household_head_educational_level_id = headEduLevel,
            household_head_disability_id = headDisability,
            household_head_socio_professional_category_id = headSocioProfessionalCategory,
            household_head_school_attendance_id = headSchoolAttendance,
            household_head_sector_of_work_id = headSectorOfWork,
            household_head_pregnancy_status = headPregnancyStatus,
            unit_of_migration_duration = unitOfMigrationDuration,
            territory_or_town = territoryOrTown,
            bank_account_or_bank_card_available = bankCardOrAccount,
            is_income_regular = isIncomeRegular,
            household_member_access_to_cultivable_land = _entriesMap["household_member_access_to_cultivable_land"] ?: "",
            household_member_owner_of_cultivable_land = _entriesMap["household_member_owner_of_cultivable_land"] ?: "",
            practice_of_cash_crop_farming_or_commercial_farming = _entriesMap["practice_of_cash_crop_farming_or_commercial_farming"] ?: "",
            has_livestock = _entriesMap["has_livestock"] ?: "",
            has_household_goods = _entriesMap["has_household_goods"] ?: "",
            affected_by_conflict = affectedByConflict,
            affected_by_epidemic = affectedByEpidemic,
            affected_by_climate_shock = affectedByClimateShock,
            take_children_out_of_school = takeChildrenOutOfSchool,
            sale_of_production_assets = saleOfProductionAssets,
            use_of_child_labor = useOfChildLabour,
            use_of_early_marriage = useOfEarlyMarriage,
            give_up_health_care = gaveUpHealthCare,
            days_spent_other_coping_strategy = daysOtherCoping,
            gps_longitude = lon,
            gps_latitude = lat,
            start_time =startTime,
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = address,
            comments = _entriesMap["comments"] ?: "",
            profile_picture = _entriesMap["profile_picture"] ?: "",
            mobile_money_username = mobileMoneyUsername,
            mobile_money_phone_number =mobileMoneyPhoneNumber,
            village_or_quartier = villageOrQuartier,
            other_household_migration_status = migrationStatus,
            other_occupation_status_of_current_accommodation = _entriesMap["other_occupation_status_of_current_accommodation"]
                ?: "",
            other_main_material_of_exterior_walls = _entriesMap["other_main_material_of_exterior_walls"]
                ?: "",
            other_main_soil_material = _entriesMap["other_main_soil_material"] ?: "",
            other_type_of_fuel_used_for_household_cooking = _entriesMap["other_type_of_fuel_used_for_household_cooking"]
                ?: "",
            other_main_source_of_household_drinking_water = _entriesMap["other_main_source_of_household_drinking_water"]
                ?: "",
            other_type_of_household_toilet = _entriesMap["other_type_of_household_toilet"] ?: "",
            other_method_of_waste_disposal = _entriesMap["other_method_of_waste_disposal"] ?: "",
            other_livestock_owned = _entriesMap["other_livestock_owned"] ?: "",
            other_household_activities_in_past_12_months = _entriesMap["other_household_activities_in_past_12_months"] ?: "",

            household_member_with_benefit_from_social_assistance_program = benefitFromSocialAssistanceProgram,
            name_of_social_assistance_program = nameOfSocialAssistanceProgram,
            affected_by_other_shock = affectedByOtherShock,
            duration_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_months_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,

            number_of_meals_eaten_by_children_6_to_17_yesterday = numberOfMealsChildren6To17,
            number_of_days_in_week_consumed_sugar_or_sweet_products = daysConsumedSugarOrSweets,
            number_of_days_in_week_consumed_vegetables =daysConsumedVegetables,
            number_of_days_in_week_consumed_staple_foods = daysConsumedStapleFoods,
            number_of_days_in_week_consumed_meat =daysConsumedMeat,
            number_of_days_in_week_consumed_legumes_or_nuts = daysConsumedLegumes,
            number_of_days_in_week_consumed_fruits = daysConsumedFruits,
            number_of_days_in_week_consumed_dairy_products = daysConsumedDiary,
            number_of_days_in_week_consumed_cooking_oils = daysConsumedCookingOils,
            number_of_meals_eaten_by_children_2_to_5_yesterday = numberOfMealsChildren2To5,

//            number_of_bicycle_owned = _entriesMap["number_of_bicycle_owned"]?.toLong() ?: 0,
//            number_of_mosquito_nets_owned = _entriesMap["number_of_mosquito_nets_owned"]?.toLong() ?: 0,
//            number_of_guinea_pig_owned = _entriesMap["number_of_guinea_pig_owned"]?.toLong() ?: 0,
//            number_of_poultry_owned = _entriesMap["number_of_poultry_owned"]?.toLong() ?: 0,
//            number_of_oil_stove_owned = _entriesMap["number_of_oil_stove_owned"]?.toLong() ?: 0,
//            number_of_rabbit_owned = _entriesMap["number_of_rabbit_owned"]?.toLong() ?: 0,
//            number_of_sheep_owned = _entriesMap["number_of_sheep_owned"]?.toLong() ?: 0,
//            number_of_motorbike_owned = _entriesMap["number_of_motorbike_owned"]?.toLong() ?: 0,
//            number_of_rooms_used_for_sleeping = _entriesMap["number_of_rooms_used_for_sleeping"]?.toLong() ?: 0,
//            number_of_wheelbarrow_owned = _entriesMap["number_of_wheelbarrow_owned"]?.toLong() ?: 0,
//            number_of_radio_owned = _entriesMap["number_of_radio_owned"]?.toLong() ?: 0,
//            number_of_stove_or_oven_owned = _entriesMap["number_of_stove_or_oven_owned"]?.toLong() ?: 0,
//            number_of_rickshaw_owned = _entriesMap["number_of_rickshaw_owned"]?.toLong() ?: 0,
//            number_of_solar_plate_owned = _entriesMap["number_of_solar_plate_owned"]?.toLong() ?: 0,
//            number_of_sewing_machine_owned = _entriesMap["number_of_sewing_machine_owned"]?.toLong() ?: 0,
//            number_of_mattress_owned = _entriesMap["number_of_mattress_owned"]?.toLong() ?: 0,
//            number_of_television_owned = _entriesMap["number_of_television_owned"]?.toLong() ?: 0,
//            number_of_table_owned = _entriesMap["number_of_table_owned"]?.toLong() ?: 0,
//            number_of_sofa_owned = _entriesMap["number_of_sofa_owned"]?.toLong() ?: 0,
//            number_of_handset_or_phone_owned = _entriesMap["number_of_handset_or_phone_owned"]?.toLong() ?: 0,
//            number_of_electric_iron_owned = _entriesMap["number_of_electric_iron_owned"]?.toLong() ?: 0,
//            number_of_plow_owned = _entriesMap["number_of_plow_owned"]?.toLong() ?: 0,
//            number_of_goat_owned = _entriesMap["number_of_goat_owned"]?.toLong() ?: 0,
//            number_of_fridge_owned = _entriesMap["number_of_fridge_owned"]?.toLong() ?: 0,
//            number_of_dvd_driver_owned = _entriesMap["number_of_dvd_driver_owned"]?.toLong() ?: 0,
//            number_of_pigs_owned = _entriesMap["number_of_pigs_owned"]?.toLong() ?: 0,
//            number_of_fan_owned = _entriesMap["number_of_fan_owned"]?.toLong() ?: 0,
//            number_of_canal_tnt_tv_cable_owned = _entriesMap["number_of_canal_tnt_tv_cable_owned"]?.toLong() ?: 0,
//            number_of_cow_owned = _entriesMap["number_of_cow_owned"]?.toLong() ?: 0,
//            number_of_meals_eaten_by_adults_18_plus_yesterday = _entriesMap["number_of_meals_eaten_by_adults_18_plus_yesterday"]?.toLong() ?: 0,
//            number_of_charcoal_iron_owned = _entriesMap["number_of_charcoal_iron_owned"]?.toLong() ?: 0,
//            number_of_computer_owned = _entriesMap["number_of_computer_owned"]?.toLong() ?: 0,
//            number_of_chair_owned = _entriesMap["number_of_chair_owned"]?.toLong() ?: 0,
//            number_of_cart_owned = _entriesMap["number_of_cart_owned"]?.toLong() ?: 0,
//            number_of_cars_owned = _entriesMap["number_of_cars_owned"]?.toLong() ?: 0,
//            number_of_bed_owned = _entriesMap["number_of_bed_owned"]?.toLong() ?: 0,
//            number_of_air_conditioner_owned = _entriesMap["number_of_air_conditioner_owned"]?.toLong() ?: 0,

            days_spent_reduce_meals_consumed_coping_strategy = daysReducedMealsConsumed,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = daysReducedMealsAdult,
            days_spent_reduce_amount_consumed_coping_strategy = daysReducedAmountConsumed,
            days_spent_eat_less_expensively_coping_strategy = daysEatLessExpensively,
            days_spent_days_without_eating_coping_strategy = daysWithoutEating,
            days_spent_consume_wild_food_coping_strategy = daysConsumedWildFood,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = daysBorrowFood,
            days_spent_begging_coping_strategy = daysBeggingFood,

            amount_of_cultivable_land_owned = "",
            minimum_monthly_income_necessary_live_without_difficulties = minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            household_monthly_income = householdMonthlyIncome,
            consent = consent,
            household_status = _entriesMap["household_status"] ?: "",
            method_of_waste_disposal = "",
            household_migration_status = migrationStatus,
            place_to_wash_hands = "",
            survey_no = "$provinceId$territoryId$communityId$groupmentId$randomID}",
            temp_survey_no = "SQE211",
            main_material_of_exterior_walls = "",
            occupation_status_of_current_accommodation = "",
            type_of_fuel_used_for_household_cooking = "",
            main_soil_material = "",
            main_source_of_household_drinking_water = "",
            type_of_household_toilet = "",
            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,
            cac = "",
            area_of_residence = "1",
            health_area_id = "1",
            health_zone_id = "1",
            respondent_type = "",
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

    private fun hasBlank(vararg fields: String): Boolean {
        var hasBlank = false
        fields.forEach {
            if (it.isBlank()) hasBlank = true
        }
        return hasBlank
    }


    sealed class AddDevelopmentalHouseholdEvent {
        data class ShowInvalidInputMessage(val msg: String) : AddDevelopmentalHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddDevelopmentalHouseholdEvent()
//        data class NavigateBack(val results: Int) : AddDevelopmentalHouseholdEvent()
    }
}