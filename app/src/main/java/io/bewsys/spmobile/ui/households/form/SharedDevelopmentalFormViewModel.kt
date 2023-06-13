package io.bewsys.spmobile.ui.households.form

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log

import androidx.lifecycle.*

import androidx.work.*
import app.cash.sqldelight.db.QueryResult.Unit.value
import io.bewsys.spmobile.ADD_HOUSEHOLD_RESULT_OK
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.local.MemberModel
import io.bewsys.spmobile.data.repository.DashboardRepository
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.data.repository.MemberRepository
import io.bewsys.spmobile.work.HouseholdUpdateWorker
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
    private val dashboardRepository: DashboardRepository,
    private val memberRepository: MemberRepository
) : ViewModel() {

    //    private val _location = MutableLiveData<Location>()
//    val location: LiveData<Location>
//        get() = _location
//
    var household: HouseholdModel? = null
    var id: Long? = null
    var remoteId = ""
    var status = ""

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

    private val _healthZones = MutableLiveData<List<String>>()
    val healthZones: LiveData<List<String>>
        get() = _healthZones

    private val _healthAreas = MutableLiveData<List<String>>()
    val healthAreas: LiveData<List<String>>
        get() = _healthAreas

    init {
        loadProvinces()
    }

    //      step one
    private val _yesConsent = MutableStateFlow("")
    val yesConsent: StateFlow<String>
        get() = _yesConsent

    var consent = ""
        set(value) {
            field = value
            _yesConsent.value = value
        }
    var province = ""
    var territory = ""
    var community = ""
    var groupment = ""
    var healthZone = ""
    var healthArea = ""

    var canoeOwned: String? = ""
    var otherLivestockQty: String = ""
    var placeForHandWashing: String = ""
    var wasteDisposal: String = ""
    var typeOfToilet: String = ""
    var sourceOfDrinkingWater: String = ""
    var otherWasteDisposal: String = ""
    var otherTypeOfToilet: String = ""
    var otherSourceOfDrinkingWater: String = ""
    var occupancyStatus: String = ""
    var exteriorWalls: String = ""
    var soilMaterial: String = ""
    var cookingFuel: String = ""
    var otherCookingFuelType: String = ""
    var otherMaterialExterial: String = ""
    var otherSoilMaterial: String = ""
    var otherOccupancyStatus: String = ""

    //    section B: Location of household
    var placeOfResidence = ""
    var registrationType = ""
    var respondentFirstName = ""
    var respondentMiddleName = ""
    var respondentLastName = ""
    var respondentAge = ""
    var respondentDOB = ""
    var respondentVoterId = ""
    var respondentPhoneNo = ""
    var address = ""
    var cac: String = ""
    var respondentFamilyBondToHead = ""
    var respondentDOBKnown: String = ""
    var respondentAgeKnown: String = ""
    var villageOrDistrict = ""
    var territoryOrTown = ""
    var respondentSex: String = ""

    var lon: String? = state["lon"] ?: ""
        set(value) {
            state["lon"] = value
            field = value
        }
    var lat: String? = state["lat"] ?: ""
        set(value) {
            state["lat"] = value
            field = value
        }

    var provinceId: String = "1"
    var communityId: String = "1"
    var groupmentId: String = "1"
    var territoryId: String = "1"
    var healthAreaId = "1"
    var healthZoneId = "1"

    var provinceCode = ""
    var territoryCode = ""
    var communityCode = ""
    var groupmentCode = ""

    fun loadProvinces() {
        viewModelScope.launch {
            dashboardRepository.getProvincesList().collectLatest {
                _provinces.value = it
            }
        }
    }

    fun getGroupmentId(groupmentName: String) {
        viewModelScope.launch {
            dashboardRepository.getGroupmentByName(groupmentName).collectLatest { list ->
                list.firstOrNull()?.apply {
                    groupmentId = id.toString()
                    groupmentCode = survey_no_code.toString()

                }


            }
        }
    }

    fun loadTerritoriesWithName(provinceName: String) {
        viewModelScope.launch {
            dashboardRepository.getProvinceByName(provinceName).collectLatest {

                it.firstOrNull()?.apply {

                    provinceId = id.toString()
                    provinceCode = survey_no_code.toString()
                    loadHealthZonesWithProvinceId()


                }
                dashboardRepository.getTerritoriesList(provinceId)
                    .collectLatest {
                        _territories.value = it
                    }

            }
        }
    }

    fun loadCommunitiesWithName(territoryName: String) {
        viewModelScope.launch {
            dashboardRepository.getTerritoryByName(territoryName).collectLatest {
                it.firstOrNull()?.apply {
                    territoryId = id.toString()

                    territoryCode = survey_no_code.toString()

                }
                dashboardRepository.getCommunitiesList(territoryId)
                    .collectLatest {
                        _communities.value = it
                    }
            }

        }
    }

    fun loadGroupmentsWithName(communityName: String) {
        viewModelScope.launch {
            dashboardRepository.getCommunityByName(communityName).collectLatest {


                it.firstOrNull()?.apply {
                    communityId = id.toString()
                    communityCode = survey_no_code.toString()


                }
                dashboardRepository.getGroupmentsList(communityId)
                    .collectLatest {
                        _groupments.value = it
                    }
            }

        }
    }

    fun loadHealthZonesWithProvinceId() {
        viewModelScope.launch {
            dashboardRepository.getHealthZonesList(provinceId)
                .collectLatest {
                    _healthZones.value = it
                }
        }
    }

    fun loadHealthAreasWithName() {
        viewModelScope.launch {
            dashboardRepository.getHealthZoneByName(healthZone).collectLatest {
                healthZoneId = it.firstOrNull()?.id.toString()

                dashboardRepository.getHealthAreasList(healthZoneId)
                    .collectLatest {
                        _healthAreas.value = it
                    }
            }

        }
    }

    fun getHealthAreaId() {
        viewModelScope.launch {
            dashboardRepository.getHealthAreaByName(healthArea).collectLatest {
                healthAreaId = it.firstOrNull()?.id.toString()
            }
        }
    }


    //    step three
    //=============================
    var headIsRespondent: String = ""
    var headFirstName = ""
    var headMiddleName = ""
    var headLastName = ""
    var headAgeKnown = ""
    var headAge = ""
    var headDOB = ""
    var headVoterId = ""
    var headPhoneNo = ""
    var headBirthCert = ""
    var headEduLevel = ""
    var headSocioProfessionalCategory = ""
    var headSchoolAttendance = ""
    var headSectorOfWork = ""
    var headDisability = ""
    var headPregnancyStatus = ""
    var headMaritalStatus = ""
    var headDOBKnown = ""
    var headSex = ""


    //    step four household
//    ============================

    var isIncomeRegular = ""
    var bankCardOrAccount = ""
    var benefitFromSocialAssistanceProgram = ""
    var migrationStatus = ""
    var unitOfMigrationDuration = ""
    var nameOfSocialAssistanceProgram = ""
    var otherMigrationStatus = ""
    var durationDisplacedReturnedRepatriatedRefugee = ""
    var householdMonthlyIncome = ""
    var minimumMonthlyIncomeNecessaryLiveWithoutDifficulties = ""
    var mobileMoneyUsername = ""
    var mobileMoneyPhoneNumber = ""
    var affectedByConflict = ""
    var affectedByEpidemic = ""
    var affectedByClimateShock = ""
    var affectedByOtherShock = ""
    var takeChildrenOutOfSchool = ""
    var useOfChildLabour = ""
    var useOfEarlyMarriage = ""
    var gaveUpHealthCare = ""
    var saleOfProductionAssets = ""
    var daysReducedMealsConsumed = ""
    var daysReducedMealsAdult = ""
    var daysReducedAmountConsumed = ""
    var daysEatLessExpensively = ""
    var daysWithoutEating = ""
    var daysConsumedWildFood = ""
    var daysBorrowFood = ""
    var daysBeggingFood = ""
    var daysOtherCoping = ""
    var numberOfMealsChildren6To17 = ""
    var numberOfMealsChildren2To5 = ""
    var numberOfMealsAdults18plus = ""
    var daysConsumedSugarOrSweets = ""
    var daysConsumedStapleFoods = ""
    var daysConsumedVegetables = ""
    var daysConsumedMeat = ""
    var daysConsumedLegumes = ""
    var daysConsumedFruits = ""
    var daysConsumedDiary = ""
    var daysConsumedCookingOils = ""


    /*      Form Six    */
    var hasLiveStock = ""
    var hasHouseholdGoods = ""
    var accessToCultivableLand = ""
    var ownerOfCultivableLand = ""
    var cashCropOrCommercialFarming = ""
    var mosquitoNets: String = ""
    var guineaPigsOwned: String = ""
    var poultryOwned: String = ""
    var bicycleOwned: String = ""
    var oilStoveOwned: String = ""
    var rabbitOwned: String = ""
    var sheepOwned: String = ""
    var motorbikeOwned: String = ""
    var roomsUsedForSleeping: String = ""
    var wheelBarrowOwned: String = ""
    var radioOwned: String = ""
    var stoveOrOvenOwned: String = ""
    var rickShawOwned: String = ""
    var solarPlateOwned: String = ""
    var sewingMachineOwned: String = ""
    var mattressOwned: String = ""
    var televisionOwned: String = ""
    var tableOwned: String = ""
    var sofaOwned: String = ""
    var handsetOrPhoneOwned: String = ""
    var electricIronOwned: String = ""
    var plowOwned: String = ""
    var goatOwned: String = ""
    var fridgeOwned: String = ""
    var dvdDriverOwned: String = ""
    var pigsOwned: String = ""
    var cableTVOwned: String = ""
    var cowOwned: String = ""
    var charcoalIron: String = ""
    var computerOwned: String = ""
    var chairOwned: String = ""
    var cartsOwned: String = ""
    var carsOwned: String = ""
    var bedOwned: String = ""
    var airConditionerOwned: String = ""
    var cultivatedLandOwned: String = ""
    var fanOwned: String = ""


    private val _SectionBHasBlank = MutableStateFlow(true)
    val SectionBHasBlank: StateFlow<Boolean>
        get() = _SectionBHasBlank

    fun sectionBHasBlankFields() {
        _SectionBHasBlank.value = hasBlank(
            address,
            cac,
            villageOrDistrict,
            placeOfResidence,
            registrationType

        )
    }


    //    section C: Identification
    private val _SectionCHasBlank = MutableStateFlow(true)
    val SectionCHasBlank: StateFlow<Boolean>
        get() = _SectionCHasBlank

    fun sectionCHasBlankFields() {
        _SectionCHasBlank.value = hasBlank(
            headFirstName,
            headLastName,
            headAge,
            respondentFirstName,
            respondentLastName,
            migrationStatus

        )
    }

    private val _SectionEHasBlank = MutableStateFlow(true)
    val SectionEHasBlank: StateFlow<Boolean>
        get() = _SectionEHasBlank

    fun sectionEHasBlankFields() {
        _SectionEHasBlank.value = hasBlank(
            occupancyStatus,
            roomsUsedForSleeping,
            exteriorWalls,
            soilMaterial,
            cookingFuel
        )
    }

    private val _SectionFHasBlank = MutableStateFlow(true)
    val SectionFHasBlank: StateFlow<Boolean>
        get() = _SectionFHasBlank

    fun sectionFHasBlankFields() {
        _SectionFHasBlank.value = hasBlank(
            sourceOfDrinkingWater,
            typeOfToilet,
            wasteDisposal,
            placeForHandWashing
        )
    }

    private val _SectionGHasBlank = MutableStateFlow(true)
    val SectionGHasBlank: StateFlow<Boolean>
        get() = _SectionGHasBlank

    fun sectionGHasBlankFields() {
        _SectionGHasBlank.value = hasBlank(
            minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            householdMonthlyIncome

        )
    }

    private val _SectionHHasBlank = MutableStateFlow(true)
    val SectionHHasBlank: StateFlow<Boolean>
        get() = _SectionHHasBlank

    fun sectionHHasBlankFields() {
        _SectionHHasBlank.value = hasBlank(
            accessToCultivableLand
        )
    }

    private val _SectionIHasBlank = MutableStateFlow(true)
    val SectionIHasBlank: StateFlow<Boolean>
        get() = _SectionIHasBlank

    fun sectionIHasBlankFields() {
        _SectionIHasBlank.value = hasBlank(
            benefitFromSocialAssistanceProgram
        )
    }


    val randomID = List(8) { Random.nextInt(0, 10) }.joinToString(separator = "")

    var startTime: String = ""

    fun setStartTime() {
        startTime = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis())
    }


    fun onRegisterClicked() {
        if (household != null) {
            updateHousehold()

        } else {
            registerHousehold()
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun registerHousehold() {
        HouseholdModel(

            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = registrationType,
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
            household_member_access_to_cultivable_land = accessToCultivableLand,
            household_member_owner_of_cultivable_land = ownerOfCultivableLand,
            practice_of_cash_crop_farming_or_commercial_farming = cashCropOrCommercialFarming,
            has_livestock = hasLiveStock,
            has_household_goods = hasHouseholdGoods,
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
            start_time = startTime,
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = address,
            mobile_money_username = mobileMoneyUsername,
            mobile_money_phone_number = mobileMoneyPhoneNumber,
            village_or_quartier = villageOrDistrict,
            other_household_migration_status = otherMigrationStatus,
            household_member_with_benefit_from_social_assistance_program = benefitFromSocialAssistanceProgram,
            name_of_social_assistance_program = nameOfSocialAssistanceProgram,
            affected_by_other_shock = affectedByOtherShock,
            duration_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_months_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_meals_eaten_by_children_6_to_17_yesterday = numberOfMealsChildren6To17,
            number_of_days_in_week_consumed_sugar_or_sweet_products = daysConsumedSugarOrSweets,
            number_of_days_in_week_consumed_vegetables = daysConsumedVegetables,
            number_of_days_in_week_consumed_staple_foods = daysConsumedStapleFoods,
            number_of_days_in_week_consumed_meat = daysConsumedMeat,
            number_of_days_in_week_consumed_legumes_or_nuts = daysConsumedLegumes,
            number_of_days_in_week_consumed_fruits = daysConsumedFruits,
            number_of_days_in_week_consumed_dairy_products = daysConsumedDiary,
            number_of_days_in_week_consumed_cooking_oils = daysConsumedCookingOils,
            number_of_meals_eaten_by_children_2_to_5_yesterday = numberOfMealsChildren2To5,
            number_of_bicycle_owned = bicycleOwned,
            number_of_mosquito_nets_owned = mosquitoNets,
            number_of_guinea_pig_owned = guineaPigsOwned,
            number_of_poultry_owned = poultryOwned,
            number_of_oil_stove_owned = oilStoveOwned,
            number_of_rabbit_owned = rabbitOwned,
            number_of_sheep_owned = sheepOwned,
            number_of_motorbike_owned = motorbikeOwned,
            number_of_rooms_used_for_sleeping = roomsUsedForSleeping,
            number_of_wheelbarrow_owned = wheelBarrowOwned,
            number_of_radio_owned = radioOwned,
            number_of_stove_or_oven_owned = stoveOrOvenOwned,
            number_of_rickshaw_owned = rickShawOwned,
            number_of_solar_plate_owned = solarPlateOwned,
            number_of_sewing_machine_owned = sewingMachineOwned,
            number_of_mattress_owned = mattressOwned,
            number_of_television_owned = televisionOwned,
            number_of_table_owned = tableOwned,
            number_of_sofa_owned = sofaOwned,
            number_of_handset_or_phone_owned = handsetOrPhoneOwned,
            number_of_electric_iron_owned = electricIronOwned,
            number_of_plow_owned = plowOwned,
            number_of_goat_owned = goatOwned,
            number_of_fridge_owned = fridgeOwned,
            number_of_dvd_driver_owned = dvdDriverOwned,
            number_of_pigs_owned = pigsOwned,
            number_of_fan_owned = fanOwned,
            number_of_canal_tnt_tv_cable_owned = cableTVOwned,
            number_of_cow_owned = cowOwned,
            number_of_meals_eaten_by_adults_18_plus_yesterday = numberOfMealsAdults18plus,
            number_of_charcoal_iron_owned = charcoalIron,
            number_of_computer_owned = computerOwned,
            number_of_chair_owned = chairOwned,
            number_of_cart_owned = cartsOwned,
            number_of_cars_owned = carsOwned,
            number_of_bed_owned = bedOwned,
            number_of_air_conditioner_owned = airConditionerOwned,
            days_spent_reduce_meals_consumed_coping_strategy = daysReducedMealsConsumed,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = daysReducedMealsAdult,
            days_spent_reduce_amount_consumed_coping_strategy = daysReducedAmountConsumed,
            days_spent_eat_less_expensively_coping_strategy = daysEatLessExpensively,
            days_spent_days_without_eating_coping_strategy = daysWithoutEating,
            days_spent_consume_wild_food_coping_strategy = daysConsumedWildFood,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = daysBorrowFood,
            days_spent_begging_coping_strategy = daysBeggingFood,
            amount_of_cultivable_land_owned = cultivatedLandOwned,
            minimum_monthly_income_necessary_live_without_difficulties = minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            household_monthly_income = householdMonthlyIncome,
            consent = consent,
            household_migration_status = migrationStatus,

            survey_no = "$provinceCode$territoryCode$communityCode$groupmentCode$randomID",

            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,

            temp_survey_no = "",
            other_occupation_status_of_current_accommodation = otherOccupancyStatus,
            other_main_material_of_exterior_walls = otherMaterialExterial,
            other_main_soil_material = otherSoilMaterial,
            other_type_of_fuel_used_for_household_cooking = otherCookingFuelType,
            other_main_source_of_household_drinking_water = otherSourceOfDrinkingWater,
            other_type_of_household_toilet = otherTypeOfToilet,
            other_method_of_waste_disposal = otherWasteDisposal,
            other_livestock_owned = otherLivestockQty,
            other_household_activities_in_past_12_months = "",
            comments = "",
            profile_picture = "",
            method_of_waste_disposal = wasteDisposal,
            place_to_wash_hands = placeForHandWashing,
            household_status = "",
            main_material_of_exterior_walls = exteriorWalls,
            occupation_status_of_current_accommodation = occupancyStatus,
            type_of_fuel_used_for_household_cooking = cookingFuel,
            main_soil_material = soilMaterial,
            main_source_of_household_drinking_water = sourceOfDrinkingWater,
            type_of_household_toilet = typeOfToilet,
            cac = cac,
            area_of_residence = placeOfResidence,
            health_area_id = healthAreaId,
            health_zone_id = healthZoneId,
            respondent_type = "",
            remote_id = "",
            status = "",
            respondent_sex = respondentSex
        ).also {
            addHousehold(it)
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateHousehold() {
        HouseholdModel(
            id = id,
            survey_date = SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss").format(System.currentTimeMillis()),
            initial_registration_type = registrationType,
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
            household_member_access_to_cultivable_land = accessToCultivableLand,
            household_member_owner_of_cultivable_land = ownerOfCultivableLand,
            practice_of_cash_crop_farming_or_commercial_farming = cashCropOrCommercialFarming,
            has_livestock = hasLiveStock,
            has_household_goods = hasHouseholdGoods,
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
            start_time = startTime,
            finish_time = SimpleDateFormat("HH:mm:ss").format(System.currentTimeMillis()),
            address = address,
            mobile_money_username = mobileMoneyUsername,
            mobile_money_phone_number = mobileMoneyPhoneNumber,
            village_or_quartier = villageOrDistrict,
            other_household_migration_status = otherMigrationStatus,
            household_member_with_benefit_from_social_assistance_program = benefitFromSocialAssistanceProgram,
            name_of_social_assistance_program = nameOfSocialAssistanceProgram,
            affected_by_other_shock = affectedByOtherShock,
            duration_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_months_displaced_returned_repatriated_refugee = durationDisplacedReturnedRepatriatedRefugee,
            number_of_meals_eaten_by_children_6_to_17_yesterday = numberOfMealsChildren6To17,
            number_of_days_in_week_consumed_sugar_or_sweet_products = daysConsumedSugarOrSweets,
            number_of_days_in_week_consumed_vegetables = daysConsumedVegetables,
            number_of_days_in_week_consumed_staple_foods = daysConsumedStapleFoods,
            number_of_days_in_week_consumed_meat = daysConsumedMeat,
            number_of_days_in_week_consumed_legumes_or_nuts = daysConsumedLegumes,
            number_of_days_in_week_consumed_fruits = daysConsumedFruits,
            number_of_days_in_week_consumed_dairy_products = daysConsumedDiary,
            number_of_days_in_week_consumed_cooking_oils = daysConsumedCookingOils,
            number_of_meals_eaten_by_children_2_to_5_yesterday = numberOfMealsChildren2To5,
            number_of_bicycle_owned = bicycleOwned,
            number_of_mosquito_nets_owned = mosquitoNets,
            number_of_guinea_pig_owned = guineaPigsOwned,
            number_of_poultry_owned = poultryOwned,
            number_of_oil_stove_owned = oilStoveOwned,
            number_of_rabbit_owned = rabbitOwned,
            number_of_sheep_owned = sheepOwned,
            number_of_motorbike_owned = motorbikeOwned,
            number_of_rooms_used_for_sleeping = roomsUsedForSleeping,
            number_of_wheelbarrow_owned = wheelBarrowOwned,
            number_of_radio_owned = radioOwned,
            number_of_stove_or_oven_owned = stoveOrOvenOwned,
            number_of_rickshaw_owned = rickShawOwned,
            number_of_solar_plate_owned = solarPlateOwned,
            number_of_sewing_machine_owned = sewingMachineOwned,
            number_of_mattress_owned = mattressOwned,
            number_of_television_owned = televisionOwned,
            number_of_table_owned = tableOwned,
            number_of_sofa_owned = sofaOwned,
            number_of_handset_or_phone_owned = handsetOrPhoneOwned,
            number_of_electric_iron_owned = electricIronOwned,
            number_of_plow_owned = plowOwned,
            number_of_goat_owned = goatOwned,
            number_of_fridge_owned = fridgeOwned,
            number_of_dvd_driver_owned = dvdDriverOwned,
            number_of_pigs_owned = pigsOwned,
            number_of_fan_owned = fanOwned,
            number_of_canal_tnt_tv_cable_owned = cableTVOwned,
            number_of_cow_owned = cowOwned,
            number_of_meals_eaten_by_adults_18_plus_yesterday = numberOfMealsAdults18plus,
            number_of_charcoal_iron_owned = charcoalIron,
            number_of_computer_owned = computerOwned,
            number_of_chair_owned = chairOwned,
            number_of_cart_owned = cartsOwned,
            number_of_cars_owned = carsOwned,
            number_of_bed_owned = bedOwned,
            number_of_air_conditioner_owned = airConditionerOwned,
            days_spent_reduce_meals_consumed_coping_strategy = daysReducedMealsConsumed,
            days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = daysReducedMealsAdult,
            days_spent_reduce_amount_consumed_coping_strategy = daysReducedAmountConsumed,
            days_spent_eat_less_expensively_coping_strategy = daysEatLessExpensively,
            days_spent_days_without_eating_coping_strategy = daysWithoutEating,
            days_spent_consume_wild_food_coping_strategy = daysConsumedWildFood,
            days_spent_borrow_food_or_rely_on_family_help_coping_strategy = daysBorrowFood,
            days_spent_begging_coping_strategy = daysBeggingFood,
            amount_of_cultivable_land_owned = cultivatedLandOwned,
            minimum_monthly_income_necessary_live_without_difficulties = minimumMonthlyIncomeNecessaryLiveWithoutDifficulties,
            household_monthly_income = householdMonthlyIncome,
            consent = consent,
            household_migration_status = migrationStatus,
            survey_no = "$provinceCode$territoryCode$communityCode$groupmentCode$randomID",

            province_id = provinceId,
            territory_id = territoryId,
            community_id = communityId,
            groupment_id = groupmentId,
            temp_survey_no = "",
            other_occupation_status_of_current_accommodation = otherOccupancyStatus,
            other_main_material_of_exterior_walls = otherMaterialExterial,
            other_main_soil_material = otherSoilMaterial,
            other_type_of_fuel_used_for_household_cooking = otherCookingFuelType,
            other_main_source_of_household_drinking_water = otherSourceOfDrinkingWater,
            other_type_of_household_toilet = otherTypeOfToilet,
            other_method_of_waste_disposal = otherWasteDisposal,
            other_livestock_owned = otherLivestockQty,
            other_household_activities_in_past_12_months = "",
            comments = "",
            profile_picture = null,
            method_of_waste_disposal = wasteDisposal,
            place_to_wash_hands = placeForHandWashing,
            household_status = "",
            main_material_of_exterior_walls = exteriorWalls,
            occupation_status_of_current_accommodation = occupancyStatus,
            type_of_fuel_used_for_household_cooking = cookingFuel,
            main_soil_material = soilMaterial,
            main_source_of_household_drinking_water = sourceOfDrinkingWater,
            type_of_household_toilet = typeOfToilet,
            cac = cac,
            area_of_residence = placeOfResidence,
            health_area_id = healthAreaId,
            health_zone_id = healthZoneId,
            respondent_type = "",
            status = status,
            respondent_sex = respondentSex,
            remote_id = remoteId
        ).also {

            updateHousehold(it)
        }
    }


    private fun addHousehold(newHouseholdModel: HouseholdModel) = viewModelScope.launch {
        householdRepository.insertHousehold(newHouseholdModel)

        val lastInsertRowId = householdRepository.getLastInsertedRowId()

        saveMembers(lastInsertRowId)

//        uploadHousehold(lastInsertRowId)

        addHouseholdEventChannel.send(
            AddDevelopmentalHouseholdEvent.NavigateBackWithResults(
                ADD_HOUSEHOLD_RESULT_OK
            )
        )
    }


    //    Member rg
    var isMemberRespondent = ""
    var isMemberHead = ""
    var memberDOBKnown: String = ""
    var memberAgeKnown: String = ""
    var memberSex = ""
    var memberPregnancyStatus = ""
    var memberMaritalStatus = ""

    // tils
    var memberFirstname = ""
    var memberMiddleName = ""
    var memberLastname = ""
    var memberAge = ""
    var memberDob = ""

    var photoUri = ""
    var memberBirthCertificate = ""
    var memberEducational = ""
    var memberSocioProfessionalCategory = ""
    var memberSchoolAttendance = ""
    var memberDisability = ""
    var memberRelationship: String = ""
    var memberOccupation: String = ""


    private val _memberHasBlankFields = MutableStateFlow(true)
    val memberHasBlankFields: StateFlow<Boolean>
        get() = _memberHasBlankFields

    fun memberHasBlankFields() {
        _memberHasBlankFields.value = hasBlank(
            memberSex,
            memberAge,
            memberFirstname,
            memberLastname,
            memberSchoolAttendance,
            memberDob,
            memberBirthCertificate,
            memberEducational,
            memberSchoolAttendance,
            memberDisability,
            memberSocioProfessionalCategory,
            memberRelationship,
            memberOccupation

        )
    }

    private val _members = MutableLiveData<List<MemberModel>>()
    val members: LiveData<List<MemberModel>>
        get() = _members

    val memberList = mutableListOf<MemberModel>()

    fun createMember() = viewModelScope.launch {
        val member = MemberModel(
            id = null,
            firstname = memberFirstname,
            middlename = memberMiddleName,
            lastname = memberLastname,
            sex = memberSex,
            age = memberAge,
            dob = memberDob,
            dob_known = memberDOBKnown,
            age_known = memberAgeKnown,
            is_member_respondent = isMemberRespondent,
            is_head = isMemberHead,
            marital_status_id = memberMaritalStatus,
            birth_certificate = memberBirthCertificate,
            educational_level_id = memberEducational,
            pregnancy_status = memberPregnancyStatus,
            school_attendance_id = memberSchoolAttendance,
            disability_id = memberDisability,
            socio_professional_category_id = memberSocioProfessionalCategory,
            sector_of_work_id = memberOccupation,
            remote_id = "",
            family_bond_id = memberRelationship,
            profile_picture = photoUri,
            household_id = "",
        )
        memberList.add(member)
        _members.value = memberList

    }

    fun clearMemberFields() {
        memberFirstname = ""
        memberMiddleName = ""
        memberLastname = ""
        memberSex = ""
        memberAge = ""
        memberDob = ""
        memberAgeKnown = ""
        memberDOBKnown = ""
        isMemberRespondent = ""
        isMemberHead = ""
        memberMaritalStatus = ""
        memberBirthCertificate = ""
        memberEducational = ""
        memberPregnancyStatus = ""
        memberSchoolAttendance = ""
        memberDisability = ""
        memberSocioProfessionalCategory = ""
        memberOccupation = ""
        memberRelationship = ""
        photoUri = ""
    }

    private fun saveMembers(householdEntityId: Long) = viewModelScope.launch {
        memberList.map { item: MemberModel ->
            item.copy(
                household_id = householdEntityId.toString()
            )
        }.also {
            memberRepository.insertMembers(it)
        }
    }


    fun loadMembers() {
        viewModelScope.launch {
            memberRepository.getMemberByHousehold(id.toString()).map { memberEntities ->
                memberEntities.map {
                    MemberModel(
                        id = it.id,
                        remote_id = it.remote_id,
                        firstname = it.firstname,
                        middlename = it.middlename,
                        lastname = it.lastname,
                        sex = it.sex,
                        age = it.age,
                        dob_known = it.dob_known,
                        dob = it.date_of_birth,
                        age_known = it.age_known,
                        profile_picture = it.profile_picture,
                        is_head = it.is_head,
                        is_member_respondent = it.is_member_respondent,
                        family_bond_id = it.family_bond_id,
                        marital_status_id = it.marital_status_id,
                        birth_certificate = it.birth_certificate,
                        educational_level_id = it.educational_level_id,
                        school_attendance_id = it.school_attendance_id,
                        pregnancy_status = it.pregnancy_status,
                        disability_id = it.disability_id,
                        socio_professional_category_id = it.socio_professional_category_id,
                        sector_of_work_id = it.sector_of_work_id,
                        household_id = it.household_id,
                        status = it.status
                    )
                }
            }.collectLatest {
                _members.value = it
                it.forEach {
                    Log.d(TAG, "${it.profile_picture}")
                }
            }
        }
    }

    private fun updateHousehold(newHouseholdModel: HouseholdModel) = viewModelScope.launch {

        householdRepository.updateHousehold(newHouseholdModel)

//        //update household with work manager
//        if (newHouseholdModel.status == "submitted") {
//            updateHousehold(id!!)
//        }

        addHouseholdEventChannel.send(
            AddDevelopmentalHouseholdEvent.NavigateBackWithResults(
                ADD_HOUSEHOLD_RESULT_OK
            )
        )
    }


    private fun uploadHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<HouseholdUploadWorker>()
            .setConstraints(constraints)
            .setInputData(createInputDataForId(itemId))
            .build()
        workManager.enqueue(uploadRequest)

    }

    private fun updateHousehold(itemId: Long) = viewModelScope.launch {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val uploadRequest = OneTimeWorkRequestBuilder<HouseholdUpdateWorker>()
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

    companion object {
        private const val TAG = "SharedVM"
    }
}