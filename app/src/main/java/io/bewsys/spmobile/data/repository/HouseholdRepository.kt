package io.bewsys.spmobile.data.repository

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.MemberEntity
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.local.toPayLoad
import io.bewsys.spmobile.data.local.toPayload
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.HouseholdApi
import io.bewsys.spmobile.data.remote.MemberApi
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.remote.model.auth.login.ErrorResponse
import io.bewsys.spmobile.data.remote.model.household.BulkUploadResponse
import io.bewsys.spmobile.data.remote.model.household.HouseholdResponse
import io.bewsys.spmobile.data.remote.model.member.MemberPayload
import io.bewsys.spmobile.data.remote.model.member.MemberResponse
import io.bewsys.spmobile.util.ApplicationScope
import io.bewsys.spmobile.util.MapUtil
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File


class HouseholdRepository(
    db: Database,
    private val api: HouseholdApi,
    private val memberApi: MemberApi,
    private val preferencesManager: PreferencesManager,
    @ApplicationScope private val applicationScope: CoroutineScope
) {
    private val householdQueries = db.householdQueries
    private val membersQueries = db.householdMemberQueries


    fun getAllHouseholds(): Flow<List<HouseholdEntity>> =
        householdQueries.getAllHouseholds().asFlow().mapToList(context = Dispatchers.Default)

    val getHouseHoldCount =
        householdQueries.getHouseholdCount().asFlow().mapToOne(Dispatchers.Default)


    suspend fun getHousehold(id: Long): HouseholdEntity? =
        withContext(Dispatchers.IO) {
            householdQueries.getById(id).executeAsOneOrNull()
        }

    suspend fun insertHousehold(
        householdModel: HouseholdModel
    ): Unit = withContext(Dispatchers.IO) {

        val userPref = preferencesManager.preferencesFlow.first()

        householdModel.apply {
            householdQueries.insertHousehold(
                id = null,
                survey_date = survey_date,
                respondent_firstname = respondent_firstname,
                respondent_middlename = respondent_middlename,
                respondent_lastname = respondent_lastname,
                respondent_dob = respondent_dob,
                respondent_family_bond_to_head = respondent_family_bond_to_head,
                respondent_voter_id = respondent_voter_id,
                respondent_phone_number = respondent_phone_number,
                household_head_firstname = household_head_firstname,
                household_head_lastname = household_head_lastname,
                household_head_middlename = household_head_middlename,
                household_head_dob = household_head_dob,
                household_head_sex = household_head_sex,
                head_age_known = head_age_known,
                head_dob_known = head_dob_known,
                household_head_voter_id_card = household_head_voter_id_card,
                household_head_phone_number = household_head_phone_number,
                household_head_age = household_head_age,
                household_head_marital_status_id = household_head_marital_status_id,
                household_head_birth_certificate = household_head_birth_certificate,
                household_head_educational_level_id = household_head_educational_level_id,
                household_head_disability_id = household_head_disability_id,
                household_head_socio_professional_category_id = household_head_socio_professional_category_id,
                household_head_school_attendance_id = household_head_school_attendance_id,
                household_head_sector_of_work_id = household_head_sector_of_work_id,
                household_head_pregnancy_status = household_head_pregnancy_status,
                unit_of_migration_duration = unit_of_migration_duration,
                territory_or_town = territory_or_town,
                bank_account_or_bank_card_available = bank_account_or_bank_card_available,
                is_income_regular = is_income_regular,
                household_member_access_to_cultivable_land = household_member_access_to_cultivable_land,
                household_member_owner_of_cultivable_land = household_member_owner_of_cultivable_land,
                practice_of_cash_crop_farming_or_commercial_farming = practice_of_cash_crop_farming_or_commercial_farming,
                has_livestock = has_livestock,
                has_household_goods = has_household_goods,
                affected_by_conflict = affected_by_conflict,
                affected_by_epidemic = affected_by_epidemic,
                affected_by_climate_shock = affected_by_climate_shock,
                take_children_out_of_school = take_children_out_of_school,
                sale_of_production_assets = sale_of_production_assets,
                use_of_child_labor = use_of_child_labor,
                use_of_early_marriage = use_of_early_marriage,
                give_up_health_care = give_up_health_care,
                days_spent_other_coping_strategy = days_spent_other_coping_strategy,
                gps_longitude = gps_longitude,
                gps_latitude = gps_latitude,
                start_time = start_time,
                finish_time = finish_time,
                address = address,
                comments = comments,
                profile_picture = profile_picture,
                mobile_money_username = mobile_money_username,
                mobile_money_phone_number = mobile_money_phone_number,
                village_or_quartier = village_or_quartier,
                other_household_migration_status = other_household_migration_status,
                other_occupation_status_of_current_accommodation = other_occupation_status_of_current_accommodation,
                other_main_material_of_exterior_walls = other_main_material_of_exterior_walls,
                other_main_soil_material = other_main_soil_material,
                other_type_of_fuel_used_for_household_cooking = other_type_of_fuel_used_for_household_cooking,
                other_main_source_of_household_drinking_water = other_main_source_of_household_drinking_water,
                other_type_of_household_toilet = other_type_of_household_toilet,
                other_method_of_waste_disposal = other_method_of_waste_disposal,
                other_livestock_owned = other_livestock_owned,
                household_member_with_benefit_from_social_assistance_program = household_member_with_benefit_from_social_assistance_program,
                name_of_social_assistance_program = name_of_social_assistance_program,
                affected_by_other_shock = affected_by_other_shock,
                other_household_activities_in_past_12_months = other_household_activities_in_past_12_months,
                duration_displaced_returned_repatriated_refugee = duration_displaced_returned_repatriated_refugee,
                number_of_months_displaced_returned_repatriated_refugee = number_of_months_displaced_returned_repatriated_refugee,
                supervisor_id = userPref.supervisorId,
                temp_survey_no = temp_survey_no,
                survey_no = survey_no,
                remote_id = null,
                cac = cac,
                team_leader_id = userPref.teamLeaderId,
                user_id = userPref.id.toString(),
                consent = consent,
                CBT_score = CBT_score,
                initial_registration_type = initial_registration_type,
                respondent_type = respondent_type,
                household_migration_status = household_migration_status,
                is_head_respondent = is_head_respondent,
                area_of_residence = area_of_residence,
                province_id = province_id,
                community_id = community_id,
                territory_id = territory_id,
                health_zone_id = health_zone_id,
                health_area_id = health_area_id,
                groupment_id = groupment_id,
                household_status = household_status,
                status = status,
                occupation_status_of_current_accommodation = occupation_status_of_current_accommodation,
                main_material_of_exterior_walls = main_material_of_exterior_walls,
                main_soil_material = main_soil_material,
                main_source_of_household_drinking_water = main_source_of_household_drinking_water,
                number_of_air_conditioner_owned = number_of_air_conditioner_owned,
                number_of_bed_owned = number_of_bed_owned,
                number_of_bicycle_owned = number_of_bicycle_owned,
                number_of_cars_owned = number_of_cars_owned,
                number_of_cart_owned = number_of_cart_owned,
                number_of_chair_owned = number_of_chair_owned,
                number_of_computer_owned = number_of_computer_owned,
                number_of_charcoal_iron_owned = number_of_charcoal_iron_owned,
                number_of_cow_owned = number_of_cow_owned,
                number_of_meals_eaten_by_adults_18_plus_yesterday = number_of_meals_eaten_by_adults_18_plus_yesterday,
                number_of_canal_tnt_tv_cable_owned = number_of_canal_tnt_tv_cable_owned,
                number_of_fan_owned = number_of_fan_owned,
                number_of_pigs_owned = number_of_pigs_owned,
                number_of_dvd_driver_owned = number_of_dvd_driver_owned,
                number_of_fridge_owned = number_of_fridge_owned,
                number_of_goat_owned = number_of_goat_owned,
                number_of_plow_owned = number_of_plow_owned,
                number_of_electric_iron_owned = number_of_electric_iron_owned,
                number_of_handset_or_phone_owned = number_of_handset_or_phone_owned,
                number_of_sofa_owned = number_of_sofa_owned,
                number_of_table_owned = number_of_table_owned,
                number_of_television_owned = number_of_television_owned,
                number_of_mattress_owned = number_of_mattress_owned,
                number_of_sewing_machine_owned = number_of_sewing_machine_owned,
                number_of_solar_plate_owned = number_of_solar_plate_owned,
                number_of_rickshaw_owned = number_of_rickshaw_owned,
                number_of_stove_or_oven_owned = number_of_stove_or_oven_owned,
                number_of_radio_owned = number_of_radio_owned,
                number_of_wheelbarrow_owned = number_of_wheelbarrow_owned,
                number_of_rooms_used_for_sleeping = number_of_rooms_used_for_sleeping,
                number_of_motorbike_owned = number_of_motorbike_owned,
                number_of_sheep_owned = number_of_sheep_owned,
                number_of_rabbit_owned = number_of_rabbit_owned,
                number_of_oil_stove_owned = number_of_oil_stove_owned,
                type_of_household_toilet = type_of_household_toilet,
                type_of_fuel_used_for_household_cooking = type_of_fuel_used_for_household_cooking,
                method_of_waste_disposal = method_of_waste_disposal,
                place_to_wash_hands = place_to_wash_hands,
                minimum_monthly_income_necessary_live_without_difficulties = minimum_monthly_income_necessary_live_without_difficulties,
                household_monthly_income = household_monthly_income,
                number_of_poultry_owned = number_of_poultry_owned,
                number_of_guinea_pig_owned = number_of_guinea_pig_owned,
                number_of_mosquito_nets_owned = number_of_mosquito_nets_owned,
                number_of_meals_eaten_by_children_2_to_5_yesterday = number_of_meals_eaten_by_children_2_to_5_yesterday,
                number_of_days_in_week_consumed_cooking_oils = number_of_days_in_week_consumed_cooking_oils,
                number_of_days_in_week_consumed_dairy_products = number_of_days_in_week_consumed_dairy_products,
                number_of_days_in_week_consumed_fruits = number_of_days_in_week_consumed_fruits,
                number_of_days_in_week_consumed_legumes_or_nuts = number_of_days_in_week_consumed_legumes_or_nuts,
                number_of_days_in_week_consumed_meat = number_of_days_in_week_consumed_meat,
                number_of_days_in_week_consumed_staple_foods = number_of_days_in_week_consumed_staple_foods,
                number_of_days_in_week_consumed_vegetables = number_of_days_in_week_consumed_vegetables,
                number_of_days_in_week_consumed_sugar_or_sweet_products = number_of_days_in_week_consumed_sugar_or_sweet_products,
                number_of_meals_eaten_by_children_6_to_17_yesterday = number_of_meals_eaten_by_children_6_to_17_yesterday,
                amount_of_cultivable_land_owned = amount_of_cultivable_land_owned,
                days_spent_begging_coping_strategy = days_spent_begging_coping_strategy,
                days_spent_borrow_food_or_rely_on_family_help_coping_strategy = days_spent_borrow_food_or_rely_on_family_help_coping_strategy,
                days_spent_consume_wild_food_coping_strategy = days_spent_consume_wild_food_coping_strategy,
                days_spent_days_without_eating_coping_strategy = days_spent_days_without_eating_coping_strategy,
                days_spent_eat_less_expensively_coping_strategy = days_spent_eat_less_expensively_coping_strategy,
                days_spent_reduce_amount_consumed_coping_strategy = days_spent_reduce_amount_consumed_coping_strategy,
                days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy,
                days_spent_reduce_meals_consumed_coping_strategy = days_spent_reduce_meals_consumed_coping_strategy,
                respondent_sex = respondent_sex
            )
        }

    }

    suspend fun updateHousehold(
        householdModel: HouseholdModel
    ): Unit = withContext(Dispatchers.IO) {

        val userPref = preferencesManager.preferencesFlow.first()

        householdModel.apply {
            householdQueries.insertHousehold(
                id = id,
                survey_date = survey_date,
                respondent_firstname = respondent_firstname,
                respondent_middlename = respondent_middlename,
                respondent_lastname = respondent_lastname,
                respondent_dob = respondent_dob,
                respondent_family_bond_to_head = respondent_family_bond_to_head,
                respondent_voter_id = respondent_voter_id,
                respondent_phone_number = respondent_phone_number,
                household_head_firstname = household_head_firstname,
                household_head_lastname = household_head_lastname,
                household_head_middlename = household_head_middlename,
                household_head_dob = household_head_dob,
                household_head_sex = household_head_sex,
                head_age_known = head_age_known,
                head_dob_known = head_dob_known,
                household_head_voter_id_card = household_head_voter_id_card,
                household_head_phone_number = household_head_phone_number,
                household_head_age = household_head_age,
                household_head_marital_status_id = household_head_marital_status_id,
                household_head_birth_certificate = household_head_birth_certificate,
                household_head_educational_level_id = household_head_educational_level_id,
                household_head_disability_id = household_head_disability_id,
                household_head_socio_professional_category_id = household_head_socio_professional_category_id,
                household_head_school_attendance_id = household_head_school_attendance_id,
                household_head_sector_of_work_id = household_head_sector_of_work_id,
                household_head_pregnancy_status = household_head_pregnancy_status,
                unit_of_migration_duration = unit_of_migration_duration,
                territory_or_town = territory_or_town,
                bank_account_or_bank_card_available = bank_account_or_bank_card_available,
                is_income_regular = is_income_regular,
                household_member_access_to_cultivable_land = household_member_access_to_cultivable_land,
                household_member_owner_of_cultivable_land = household_member_owner_of_cultivable_land,
                practice_of_cash_crop_farming_or_commercial_farming = practice_of_cash_crop_farming_or_commercial_farming,
                has_livestock = has_livestock,
                has_household_goods = has_household_goods,
                affected_by_conflict = affected_by_conflict,
                affected_by_epidemic = affected_by_epidemic,
                affected_by_climate_shock = affected_by_climate_shock,
                take_children_out_of_school = take_children_out_of_school,
                sale_of_production_assets = sale_of_production_assets,
                use_of_child_labor = use_of_child_labor,
                use_of_early_marriage = use_of_early_marriage,
                give_up_health_care = give_up_health_care,
                days_spent_other_coping_strategy = days_spent_other_coping_strategy,
                gps_longitude = gps_longitude,
                gps_latitude = gps_latitude,
                start_time = start_time,
                finish_time = finish_time,
                address = address,
                comments = comments,
                profile_picture = profile_picture,
                mobile_money_username = mobile_money_username,
                mobile_money_phone_number = mobile_money_phone_number,
                village_or_quartier = village_or_quartier,
                other_household_migration_status = other_household_migration_status,
                other_occupation_status_of_current_accommodation = other_occupation_status_of_current_accommodation,
                other_main_material_of_exterior_walls = other_main_material_of_exterior_walls,
                other_main_soil_material = other_main_soil_material,
                other_type_of_fuel_used_for_household_cooking = other_type_of_fuel_used_for_household_cooking,
                other_main_source_of_household_drinking_water = other_main_source_of_household_drinking_water,
                other_type_of_household_toilet = other_type_of_household_toilet,
                other_method_of_waste_disposal = other_method_of_waste_disposal,
                other_livestock_owned = other_livestock_owned,
                household_member_with_benefit_from_social_assistance_program = household_member_with_benefit_from_social_assistance_program,
                name_of_social_assistance_program = name_of_social_assistance_program,
                affected_by_other_shock = affected_by_other_shock,
                other_household_activities_in_past_12_months = other_household_activities_in_past_12_months,
                duration_displaced_returned_repatriated_refugee = duration_displaced_returned_repatriated_refugee,
                number_of_months_displaced_returned_repatriated_refugee = number_of_months_displaced_returned_repatriated_refugee,
                supervisor_id = userPref.supervisorId,
                temp_survey_no = temp_survey_no,
                survey_no = survey_no,
                remote_id = remote_id,
                cac = cac,
                team_leader_id = userPref.teamLeaderId,
                user_id = userPref.id.toString(),
                consent = consent,
                CBT_score = CBT_score,
                initial_registration_type = initial_registration_type,
                respondent_type = respondent_type,
                household_migration_status = household_migration_status,
                is_head_respondent = is_head_respondent,
                area_of_residence = area_of_residence,
                province_id = province_id,
                community_id = community_id,
                territory_id = territory_id,
                health_zone_id = health_zone_id,
                health_area_id = health_area_id,
                groupment_id = groupment_id,
                household_status = household_status,
                status = status,
                occupation_status_of_current_accommodation = occupation_status_of_current_accommodation,
                main_material_of_exterior_walls = main_material_of_exterior_walls,
                main_soil_material = main_soil_material,
                main_source_of_household_drinking_water = main_source_of_household_drinking_water,
                number_of_air_conditioner_owned = number_of_air_conditioner_owned,
                number_of_bed_owned = number_of_bed_owned,
                number_of_bicycle_owned = number_of_bicycle_owned,
                number_of_cars_owned = number_of_cars_owned,
                number_of_cart_owned = number_of_cart_owned,
                number_of_chair_owned = number_of_chair_owned,
                number_of_computer_owned = number_of_computer_owned,
                number_of_charcoal_iron_owned = number_of_charcoal_iron_owned,
                number_of_cow_owned = number_of_cow_owned,
                number_of_meals_eaten_by_adults_18_plus_yesterday = number_of_meals_eaten_by_adults_18_plus_yesterday,
                number_of_canal_tnt_tv_cable_owned = number_of_canal_tnt_tv_cable_owned,
                number_of_fan_owned = number_of_fan_owned,
                number_of_pigs_owned = number_of_pigs_owned,
                number_of_dvd_driver_owned = number_of_dvd_driver_owned,
                number_of_fridge_owned = number_of_fridge_owned,
                number_of_goat_owned = number_of_goat_owned,
                number_of_plow_owned = number_of_plow_owned,
                number_of_electric_iron_owned = number_of_electric_iron_owned,
                number_of_handset_or_phone_owned = number_of_handset_or_phone_owned,
                number_of_sofa_owned = number_of_sofa_owned,
                number_of_table_owned = number_of_table_owned,
                number_of_television_owned = number_of_television_owned,
                number_of_mattress_owned = number_of_mattress_owned,
                number_of_sewing_machine_owned = number_of_sewing_machine_owned,
                number_of_solar_plate_owned = number_of_solar_plate_owned,
                number_of_rickshaw_owned = number_of_rickshaw_owned,
                number_of_stove_or_oven_owned = number_of_stove_or_oven_owned,
                number_of_radio_owned = number_of_radio_owned,
                number_of_wheelbarrow_owned = number_of_wheelbarrow_owned,
                number_of_rooms_used_for_sleeping = number_of_rooms_used_for_sleeping,
                number_of_motorbike_owned = number_of_motorbike_owned,
                number_of_sheep_owned = number_of_sheep_owned,
                number_of_rabbit_owned = number_of_rabbit_owned,
                number_of_oil_stove_owned = number_of_oil_stove_owned,
                type_of_household_toilet = type_of_household_toilet,
                type_of_fuel_used_for_household_cooking = type_of_fuel_used_for_household_cooking,
                method_of_waste_disposal = method_of_waste_disposal,
                place_to_wash_hands = place_to_wash_hands,
                minimum_monthly_income_necessary_live_without_difficulties = minimum_monthly_income_necessary_live_without_difficulties,
                household_monthly_income = household_monthly_income,
                number_of_poultry_owned = number_of_poultry_owned,
                number_of_guinea_pig_owned = number_of_guinea_pig_owned,
                number_of_mosquito_nets_owned = number_of_mosquito_nets_owned,
                number_of_meals_eaten_by_children_2_to_5_yesterday = number_of_meals_eaten_by_children_2_to_5_yesterday,
                number_of_days_in_week_consumed_cooking_oils = number_of_days_in_week_consumed_cooking_oils,
                number_of_days_in_week_consumed_dairy_products = number_of_days_in_week_consumed_dairy_products,
                number_of_days_in_week_consumed_fruits = number_of_days_in_week_consumed_fruits,
                number_of_days_in_week_consumed_legumes_or_nuts = number_of_days_in_week_consumed_legumes_or_nuts,
                number_of_days_in_week_consumed_meat = number_of_days_in_week_consumed_meat,
                number_of_days_in_week_consumed_staple_foods = number_of_days_in_week_consumed_staple_foods,
                number_of_days_in_week_consumed_vegetables = number_of_days_in_week_consumed_vegetables,
                number_of_days_in_week_consumed_sugar_or_sweet_products = number_of_days_in_week_consumed_sugar_or_sweet_products,
                number_of_meals_eaten_by_children_6_to_17_yesterday = number_of_meals_eaten_by_children_6_to_17_yesterday,
                amount_of_cultivable_land_owned = amount_of_cultivable_land_owned,
                days_spent_begging_coping_strategy = days_spent_begging_coping_strategy,
                days_spent_borrow_food_or_rely_on_family_help_coping_strategy = days_spent_borrow_food_or_rely_on_family_help_coping_strategy,
                days_spent_consume_wild_food_coping_strategy = days_spent_consume_wild_food_coping_strategy,
                days_spent_days_without_eating_coping_strategy = days_spent_days_without_eating_coping_strategy,
                days_spent_eat_less_expensively_coping_strategy = days_spent_eat_less_expensively_coping_strategy,
                days_spent_reduce_amount_consumed_coping_strategy = days_spent_reduce_amount_consumed_coping_strategy,
                days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy,
                days_spent_reduce_meals_consumed_coping_strategy = days_spent_reduce_meals_consumed_coping_strategy,
                respondent_sex = respondent_sex
            )
        }

    }

    suspend fun deleteHousehold(id: Long) = withContext(Dispatchers.IO) {
        householdQueries.deleteHousehold(id)
        val list = membersQueries.getByHouseholdId(id.toString()).asFlow()
            .mapToList(context = Dispatchers.Default).collectLatest {
                it.forEach {
                    membersQueries.transaction {
                        membersQueries.deleteMember(it.id)
                    }
                }
            }
    }

    suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
        householdQueries.lastInsertRowId().executeAsOne()
    }


    //    ================================================================
    //   *********************** network calls  ************************
    suspend fun uploadHousehold(itemId: Long, payload: HouseholdPayload) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()

            val response = api.uploadHousehold(payload, userPref.token)

            if (response.status.value in 200..299) {

                val body = response.body<HouseholdResponse>()

                householdQueries.updateStatus(
                    "submitted", id = itemId,
                    remoteId = body.household!!.id.toString(),
                    surveyNo = body.household.survey_no
                )
                uploadMembersItem(itemId.toString(), body.household.id.toString())

                emit(Resource.Success(body))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun updateHouseholdRemote(itemId: Long, payload: HouseholdPayload) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = api.updateHousehold(payload, userPref.token)

            if (response.status.value in 200..299) {
                val body = response.body<HouseholdResponse>()

                householdQueries.updateStatus(
                    "submitted", id = itemId,
                    remoteId = body.household!!.id.toString(),
                    surveyNo = body.household.survey_no
                )
//                updateMembersItem(itemId.toString())

                emit(Resource.Success(body))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMemberByHouseholdId(householdId: String): Flow<List<MemberEntity>> =
        membersQueries.getByHouseholdId(householdId).asFlow()
            .mapToList(context = Dispatchers.Default)

    private suspend fun uploadMembersItem(householdId: String, householdRemoteId: String) {

        return withContext(Dispatchers.IO) {

            getMemberByHouseholdId(householdId).firstOrNull()?.forEach { memberEntity ->
                memberEntity.apply {
                    uploadMember(
                        toPayLoad(), householdRemoteId
                    ).collectLatest { response ->
                        when (response) {
                            is Resource.Success -> {
                            }

                            is Resource.Exception -> {

                                response.throwable.localizedMessage?.let {
                                    Log.d(
                                        "M_TAG", it
                                    )
                                }
                            }

                            else -> {
                                val res = response as ErrorResponse
                                Log.d("M_TAG", "${res.msg}")


                            }
                        }
                    }
                }
            }


        }
    }

    suspend fun uploadMember(payload: MemberPayload, householdId: String) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = memberApi.uploadMember(payload, userPref.token, householdId)


            if (response.status.value in 200..299) {
//                val body = response.body<MemberResponse>()

                emit(Resource.Success<Any>(response.body()))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun updateMember(payload: MemberPayload) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = memberApi.updateMember(payload, userPref.token)


            if (response.status.value in 200..299) {
                val body = response.body<MemberResponse>()

//                membersQueries.updateStatus(
//                    status = "submitted",
//                    id = payload.id!!,
//                    remoteId = body.member?.id.toString(),
//                )

                emit(Resource.Success<Any>(response.body()))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun updateMembersItem(householdId: String) {

        return withContext(Dispatchers.IO) {

            getMemberByHouseholdId(householdId).firstOrNull()?.forEach { memberEntity ->
                memberEntity.apply {
                    updateMember(

                        MemberPayload(
                            id = if (remote_id.isNullOrEmpty()) null else remote_id.toLong(),
                            remote_id = remote_id,
                            firstname = firstname,
                            middlename = middlename,
                            lastname = lastname,
                            sex = sex,
                            age = age,
                            date_of_birth = dob,
                            age_known = age_known?.lowercase()?:"",
                            dob_known = dob_known?.lowercase()?:"",
                            profile_picture = profile_picture,
                            is_member_respondent = MapUtil.intMappings[is_member_respondent] ?: 0,
                            is_head = MapUtil.intMappings[is_head] ?: 0,
                            family_bond_id = MapUtil.intMappings[family_bond_id],
                            marital_status_id = MapUtil.intMappings[marital_status_id],
                            birth_certificate = MapUtil.intMappings[birth_certificate],
                            educational_level_id = MapUtil.intMappings[educational_level_id] ?: 31,
                            school_attendance_id = MapUtil.intMappings[school_attendance_id] ?: 46,
                            pregnancy_status = MapUtil.intMappings[pregnancy_status],
                            disability_id = MapUtil.intMappings[disability_id] ?: 55,
                            socio_professional_category_id = MapUtil.intMappings[socio_professional_category_id]
                                ?: 174,
                            sector_of_work_id = MapUtil.intMappings[sector_of_work_id] ?: 175,
                            household_id = household_id

                        )
                    ).collectLatest { response ->
                        when (response) {
                            is Resource.Success -> {
                            }

                            is Resource.Exception -> {

                                response.throwable.localizedMessage?.let {
                                    Log.d(
                                        "M_TAG", it
                                    )
                                }

                            }

                            else -> {
                                val res = response as ErrorResponse
                                Log.d("M_TAG", "${res.msg}")


                            }
                        }
                    }
                }
            }


        }
    }


    suspend fun bulkUploadHouseholdsFlow() = flow {
        emit(Resource.Loading)

        getAllHouseholds().map { list ->


            list.filter { e -> e.status != "submitted" }.map {
                val members = getMemberByHouseholdId(it.id.toString()).firstOrNull()
                Pair(it, members)
            }.map { pair ->
                val item = pair.first

                item.toPayload().apply {

                    members = pair.second?.map {
                        it.toPayLoad()
                    }
                    number_of_members = members?.size ?: 0
                }

            }
        }.map { householdPayloadList ->

            val multiPartFormDataContent = withContext(Dispatchers.Default) {

                MultiPartFormDataContent(
                    parts =
                    formData {


                        this@formData.append(
                            FormPart(
                                "households",
                                Json.encodeToString(householdPayloadList)
                            )
                        )



                        householdPayloadList?.forEach { householdPayload ->
                            householdPayload.members?.forEach { member ->


                                if (member.profile_picture?.isNotEmpty() == true) {
                                    val file = File(member.profile_picture)
                                    this@formData.appendInput(
                                        key = "members_${member.id}",
                                        headers = Headers.build {
                                            append(HttpHeaders.ContentType, "image/jpeg")
                                            append(
                                                HttpHeaders.ContentDisposition,
                                                "filename= \"${file.name}\""
                                            )
                                        },
                                        size = file.length()
                                    ) { buildPacket { writeFully(file.readBytes()) } }
                                }


                            }


                        }


                    },
                    boundary = "WebAppBoundary"

                )
            }






            try {
                val userPref = preferencesManager.preferencesFlow.first()

                val response = api.uploadHouseholdBulk(multiPartFormDataContent, userPref.token)

                if (response.status.value in 200..299) {

                    val body = response.body<BulkUploadResponse>()

                    withContext(Dispatchers.IO) {

                        householdQueries.transaction {
                            body.data?.households?.forEach {

                                householdQueries.updateBulkStatus(
                                    "submitted", id = it.local!!.toLong(),
                                    remoteId = it.remote_id.toString()
                                )
                            }
                        }

                    }

                    emit(Resource.Success<BulkUploadResponse>(body))
                } else {
                    emit(Resource.Failure<ErrorResponse>(response.body()))
                }
            } catch (throwable: Throwable) {
                emit(Resource.Exception(throwable, null))
            }

        }.first()


    }.flowOn(Dispatchers.IO)




    companion object {
        const val TAG = "HouseholdRepository"
    }

}

