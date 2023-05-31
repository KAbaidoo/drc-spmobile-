package io.bewsys.spmobile.data.repository

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.MemberEntity
import io.bewsys.spmobile.data.local.HouseholdModel
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
import io.ktor.http.content.PartData
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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


    suspend fun getAllHouseholds(): Flow<List<HouseholdEntity>> =
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
                id = remote_id?.toLong(),
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
       val members = membersQueries.getByHouseholdId(id.toString()).asFlow().mapToList(context = Dispatchers.Default).collectLatest { list->
           membersQueries.transaction {
               list.onEach {
                   membersQueries.deleteMember(it.id)
               }
           }
       }

        householdQueries.deleteHousehold(id)
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
                updateMembersItem(itemId.toString())

                emit(Resource.Success(body))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun getMemberByHouseholdId(householdId: String): Flow<List<MemberEntity>> =
        membersQueries.getByHouseholdId(householdId).asFlow()
            .mapToList(context = Dispatchers.Default)

    private suspend fun uploadMembersItem(householdId: String, householdRemoteId: String) {

        return withContext(Dispatchers.IO) {

            getMemberByHouseholdId(householdId).firstOrNull()?.forEach { memberEntity ->
                memberEntity.apply {
                    uploadMember(
                        MemberPayload(
                            id = id,
                            remote_id = remote_id,
                            firstname = firstname,
                            middlename = middlename,
                            lastname = lastname,
                            sex = sex,
                            age = age,
                            date_of_birth = dob,
                            age_known = age_known,
                            dob_known = dob_known,
                            profile_picture = profile_picture,
                            is_member_respondent = MapUtil.mapping[is_member_respondent] ?: 0,
                            is_head = MapUtil.mapping[is_head] ?: 0,
                            family_bond_id = MapUtil.mapping[family_bond_id],
                            marital_status_id = MapUtil.mapping[marital_status_id],
                            birth_certificate = birth_certificate,
                            educational_level_id = MapUtil.mapping[educational_level_id],
                            school_attendance_id = MapUtil.mapping[school_attendance_id],
                            pregnancy_status = pregnancy_status,
                            disability_id = MapUtil.mapping[disability_id],
                            socio_professional_category_id = MapUtil.mapping[socio_professional_category_id],
                            sector_of_work_id = MapUtil.mapping[sector_of_work_id],
                            household_id = household_id
                        ), householdRemoteId
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

    private suspend fun uploadMember(payload: MemberPayload, householdId: String) = flow {

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
                            age_known = age_known,
                            dob_known = dob_known,
                            profile_picture = profile_picture,
                            is_member_respondent = MapUtil.mapping[is_member_respondent] ?: 0,
                            is_head = MapUtil.mapping[is_head] ?: 0,
                            family_bond_id = MapUtil.mapping[family_bond_id],
                            marital_status_id = MapUtil.mapping[marital_status_id],
                            birth_certificate = birth_certificate,
                            educational_level_id = MapUtil.mapping[educational_level_id],
                            school_attendance_id = MapUtil.mapping[school_attendance_id],
                            pregnancy_status = pregnancy_status,
                            disability_id = MapUtil.mapping[disability_id],
                            socio_professional_category_id = MapUtil.mapping[socio_professional_category_id],
                            sector_of_work_id = MapUtil.mapping[sector_of_work_id],
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
            list.map {
                val members = getMemberByHouseholdId(it.id.toString()).firstOrNull()
                Pair(it, members)
            }.map { pair ->
                val item = pair.first

                HouseholdPayload(
                    id = item.id,
                    remote_id = "",
                    survey_no = item.survey_no!!,
                    temp_survey_no = item.temp_survey_no,
                    survey_date = item.survey_date!!,
                    initial_registration_type = item.initial_registration_type,
                    user_id = item.user_id!!.toInt(),
                    start_time = item.start_time!!,
                    finish_time = item.finish_time,
                    area_of_residence = MapUtil.mapping[item.area_of_residence],
                    province_id = if (item.province_id.isNullOrEmpty()) null else item.province_id.toInt(),
                    community_id = if (item.community_id.isNullOrEmpty()) null else item.community_id.toInt(),
                    territory_id = if (item.territory_id.isNullOrEmpty()) null else item.territory_id.toInt(),
                    groupment_id = if (item.groupment_id.isNullOrEmpty()) null else item.groupment_id.toInt(),
                    health_zone_id = if (item.health_zone_id.isNullOrEmpty()) null else item.health_zone_id.toInt(),
                    health_area_id = if (item.health_area_id.isNullOrEmpty()) null else item.health_area_id.toInt(),
                    village_or_quartier = item.village_or_quartier,
                    address = item.address,
                    cac = item.cac,
                    gps_longitude = item.gps_longitude,
                    gps_latitude = item.gps_latitude,
                    number_of_members = 0,
                    household_head_firstname = item.household_head_firstname,
                    household_head_lastname = item.household_head_lastname,
                    household_head_middlename = item.household_head_middlename,
                    household_head_voter_id_card = item.household_head_voter_id_card,
                    household_head_phone_number = item.household_head_phone_number,
                    head_age_known = item.head_age_known,
                    household_head_age = if (item.household_head_age.isNullOrEmpty()) null else item.household_head_age.toInt(),
                    head_dob_known = item.head_dob_known,
                    household_head_dob = item.household_head_dob,
                    household_head_sex = item.household_head_sex,

                    respondent_firstname = item.respondent_firstname,
                    respondent_middlename = item.respondent_firstname,
                    respondent_lastname = item.respondent_lastname,
                    respondent_dob = item.respondent_dob,
                    respondent_family_bond_to_head = MapUtil.mapping[item.respondent_family_bond_to_head],
                    respondent_voter_id = item.respondent_voter_id,
                    respondent_phone_number = item.respondent_phone_number,
                    household_migration_status = MapUtil.mapping[item.household_migration_status]
                        ?: 160,
                    other_household_migration_status = item.other_household_migration_status,
                    duration_displaced_returned_repatriated_refugee = if (item.duration_displaced_returned_repatriated_refugee.isNullOrEmpty()) 0 else item.duration_displaced_returned_repatriated_refugee.toInt(),
                    unit_of_migration_duration = item.unit_of_migration_duration,
                    occupation_status_of_current_accommodation = MapUtil.mapping[item.occupation_status_of_current_accommodation]
                        ?: 75,
                    other_occupation_status_of_current_accommodation = item.other_occupation_status_of_current_accommodation,
                    main_material_of_exterior_walls = MapUtil.mapping[item.main_material_of_exterior_walls]
                        ?: 91,
                    other_main_material_of_exterior_walls = item.other_main_material_of_exterior_walls,
                    main_soil_material = MapUtil.mapping[item.main_soil_material] ?: 101,
                    other_main_soil_material = item.other_main_soil_material,
                    number_of_rooms_used_for_sleeping = if (item.number_of_rooms_used_for_sleeping.isNullOrEmpty()) 0 else item.number_of_rooms_used_for_sleeping.toInt(),
                    type_of_fuel_used_for_household_cooking = MapUtil.mapping[item.type_of_fuel_used_for_household_cooking]
                        ?: 114,
                    other_type_of_fuel_used_for_household_cooking = item.other_type_of_fuel_used_for_household_cooking,
                    main_source_of_household_drinking_water = MapUtil.mapping[item.main_source_of_household_drinking_water]
                        ?: 129,
                    other_main_source_of_household_drinking_water = item.other_main_source_of_household_drinking_water,
                    type_of_household_toilet = MapUtil.mapping[item.type_of_household_toilet]
                        ?: 161,
                    other_type_of_household_toilet = item.other_type_of_household_toilet,
                    method_of_waste_disposal = MapUtil.mapping[item.method_of_waste_disposal]
                        ?: 144,
                    other_method_of_waste_disposal = item.other_method_of_waste_disposal,
                    place_to_wash_hands = MapUtil.mapping[item.place_to_wash_hands],
                    minimum_monthly_income_necessary_live_without_difficulties = if (item.minimum_monthly_income_necessary_live_without_difficulties.isNullOrEmpty()) 0 else item.minimum_monthly_income_necessary_live_without_difficulties.toLong(),
                    household_monthly_income = if (item.household_monthly_income.isNullOrEmpty()) 0 else item.household_monthly_income.toLong(),
                    is_income_regular = item.is_income_regular,
                    bank_account_or_bank_card_available = item.bank_account_or_bank_card_available,
                    mobile_money_username = item.mobile_money_username,
                    mobile_money_phone_number = item.mobile_money_phone_number,
                    household_member_access_to_cultivable_land = item.household_member_access_to_cultivable_land,
                    household_member_owner_of_cultivable_land = item.household_member_owner_of_cultivable_land,
                    amount_of_cultivable_land_owned = if (item.amount_of_cultivable_land_owned.isNullOrEmpty()) 0 else item.amount_of_cultivable_land_owned.toInt(),
                    practice_of_cash_crop_farming_or_commercial_farming = item.practice_of_cash_crop_farming_or_commercial_farming,
                    number_of_goat_owned = if (item.number_of_goat_owned.isNullOrEmpty()) 0 else item.number_of_goat_owned.toInt(),
                    number_of_sheep_owned = if (item.number_of_sheep_owned.isNullOrEmpty()) 0 else item.number_of_sheep_owned.toInt(),
                    number_of_cow_owned = if (item.number_of_cow_owned.isNullOrEmpty()) 0 else item.number_of_cow_owned.toInt(),
                    number_of_rabbit_owned = if (item.number_of_rabbit_owned.isNullOrEmpty()) 0 else item.number_of_rabbit_owned.toInt(),
                    number_of_pigs_owned = if (item.number_of_pigs_owned.isNullOrEmpty()) 0 else item.number_of_pigs_owned.toInt(),
                    number_of_poultry_owned = if (item.number_of_poultry_owned.isNullOrEmpty()) 0 else item.number_of_poultry_owned.toInt(),
                    number_of_guinea_pig_owned = if (item.number_of_guinea_pig_owned.isNullOrEmpty()) 0 else item.number_of_guinea_pig_owned.toInt(),
                    other_livestock_owned = item.other_livestock_owned,
                    number_of_cars_owned = if (item.number_of_cars_owned.isNullOrEmpty()) 0 else item.number_of_cars_owned.toInt(),
                    number_of_motorbike_owned = if (item.number_of_motorbike_owned.isNullOrEmpty()) 0 else item.number_of_motorbike_owned.toInt(),
                    number_of_bicycle_owned = if (item.number_of_bicycle_owned.isNullOrEmpty()) 0 else item.number_of_bicycle_owned.toInt(),
                    number_of_cart_owned = if (item.number_of_cart_owned.isNullOrEmpty()) 0 else item.number_of_cart_owned.toInt(),
                    number_of_plow_owned = if (item.number_of_plow_owned.isNullOrEmpty()) 0 else item.number_of_plow_owned.toInt(),
                    number_of_wheelbarrow_owned = if (item.number_of_wheelbarrow_owned.isNullOrEmpty()) 0 else item.number_of_wheelbarrow_owned.toInt(),
                    number_of_rickshaw_owned = if (item.number_of_rickshaw_owned.isNullOrEmpty()) 0 else item.number_of_rickshaw_owned.toInt(),
                    number_of_mattress_owned = if (item.number_of_mattress_owned.isNullOrEmpty()) 0 else item.number_of_mattress_owned.toInt(),
                    number_of_fridge_owned = if (item.number_of_fridge_owned.isNullOrEmpty()) 0 else item.number_of_fridge_owned.toInt(),
                    number_of_fan_owned = if (item.number_of_fan_owned.isNullOrEmpty()) 0 else item.number_of_fan_owned.toInt(),
                    number_of_air_conditioner_owned = if (item.number_of_air_conditioner_owned.isNullOrEmpty()) 0 else item.number_of_air_conditioner_owned.toInt(),
                    number_of_radio_owned = if (item.number_of_radio_owned.isNullOrEmpty()) 0 else item.number_of_radio_owned.toInt(),

                    number_of_television_owned = if (item.number_of_television_owned.isNullOrEmpty()) 0 else item.number_of_television_owned.toInt(),
                    number_of_canal_tnt_tvCable_owned = if (item.number_of_canal_tnt_tv_cable_owned.isNullOrEmpty()) 0 else item.number_of_canal_tnt_tv_cable_owned.toInt(),
                    number_of_dvd_driver_owned = if (item.number_of_dvd_driver_owned.isNullOrEmpty()) 0 else item.number_of_dvd_driver_owned.toInt(),
                    number_of_handset_or_phone_owned = if (item.number_of_handset_or_phone_owned.isNullOrEmpty()) 0 else item.number_of_handset_or_phone_owned.toInt(),
                    number_of_computer_owned = if (item.number_of_computer_owned.isNullOrEmpty()) 0 else item.number_of_computer_owned.toInt(),
                    number_of_stove_or_oven_owned = if (item.number_of_stove_or_oven_owned.isNullOrEmpty()) 0 else item.number_of_stove_or_oven_owned.toInt(),
                    number_of_oil_stove_owned = if (item.number_of_oil_stove_owned.isNullOrEmpty()) 0 else item.number_of_oil_stove_owned.toInt(),
                    number_of_solar_plate_owned = if (item.number_of_solar_plate_owned.isNullOrEmpty()) 0 else item.number_of_solar_plate_owned.toInt(),
                    number_of_sewing_machine_owned = if (item.number_of_sewing_machine_owned.isNullOrEmpty()) 0 else item.number_of_sewing_machine_owned.toInt(),
                    number_of_electric_iron_owned = if (item.number_of_electric_iron_owned.isNullOrEmpty()) 0 else item.number_of_electric_iron_owned.toInt(),

                    number_of_charcoal_iron_owned = if (item.number_of_charcoal_iron_owned.isNullOrEmpty()) 0 else item.number_of_charcoal_iron_owned.toInt(),
                    number_of_bed_owned = if (item.number_of_bed_owned.isNullOrEmpty()) 0 else item.number_of_bed_owned.toInt(),
                    number_of_table_owned = if (item.number_of_table_owned.isNullOrEmpty()) 0 else item.number_of_table_owned.toInt(),
                    number_of_chair_owned = if (item.number_of_chair_owned.isNullOrEmpty()) 0 else item.number_of_chair_owned.toInt(),
                    number_of_sofa_owned = if (item.number_of_sofa_owned.isNullOrEmpty()) 0 else item.number_of_sofa_owned.toInt(),

                    number_of_mosquito_nets_owned = if (item.number_of_mosquito_nets_owned.isNullOrEmpty()) 0 else item.number_of_mosquito_nets_owned.toInt(),
                    number_of_meals_eaten_by_adults_18_plus_yesterday = if (item.number_of_meals_eaten_by_adults_18_plus_yesterday.isNullOrEmpty()) 0 else item.number_of_meals_eaten_by_adults_18_plus_yesterday.toInt(),
                    number_of_meals_eaten_by_children_6_to_17_yesterday = if (item.number_of_meals_eaten_by_children_6_to_17_yesterday.isNullOrEmpty()) 0 else item.number_of_meals_eaten_by_children_6_to_17_yesterday.toInt(),
                    number_of_meals_eaten_by_children_2_to_5_yesterday = if (item.number_of_meals_eaten_by_children_2_to_5_yesterday.isNullOrEmpty()) 0 else item.number_of_meals_eaten_by_children_2_to_5_yesterday.toInt(),
                    number_of_days_in_week_consumed_staple_foods = if (item.number_of_days_in_week_consumed_staple_foods.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_staple_foods.toInt(),
                    number_of_days_in_week_consumed_legumes_or_nuts = if (item.number_of_days_in_week_consumed_legumes_or_nuts.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_legumes_or_nuts.toInt(),
                    number_of_days_in_week_consumed_dairy_products = if (item.number_of_days_in_week_consumed_dairy_products.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_dairy_products.toInt(),
                    number_of_days_in_week_consumed_meat = if (item.number_of_days_in_week_consumed_meat.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_meat.toInt(),

                    number_of_days_in_week_consumed_vegetables = if (item.number_of_days_in_week_consumed_vegetables.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_vegetables.toInt(),
                    number_of_days_in_week_consumed_fruits = if (item.number_of_days_in_week_consumed_fruits.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_fruits.toInt(),
                    number_of_days_in_week_consumed_cooking_oils = if (item.number_of_days_in_week_consumed_cooking_oils.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_cooking_oils.toInt(),
                    number_of_days_in_week_consumed_sugar_or_sweet_products = if (item.number_of_days_in_week_consumed_sugar_or_sweet_products.isNullOrEmpty()) 0 else item.number_of_days_in_week_consumed_sugar_or_sweet_products.toInt(),
                    household_member_with_benefit_from_social_assistance_program = item.household_member_with_benefit_from_social_assistance_program,
                    name_of_social_assistance_program = item.name_of_social_assistance_program,
                    affected_by_conflict = item.affected_by_conflict,
                    affected_by_epidemic = item.affected_by_epidemic,
                    affected_by_climate_shock = item.affected_by_climate_shock,
                    affected_by_other_shock = item.affected_by_other_shock,
                    days_spent_reduce_amount_consumed_coping_strategy = if (item.days_spent_reduce_amount_consumed_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_reduce_amount_consumed_coping_strategy.toInt(),
                    days_spent_reduce_meals_consumed_coping_strategy = if (item.days_spent_reduce_meals_consumed_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_reduce_meals_consumed_coping_strategy.toInt(),
                    days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = if (item.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy.toInt(),
                    days_spent_eat_less_expensively_coping_strategy = if (item.days_spent_eat_less_expensively_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_eat_less_expensively_coping_strategy.toInt(),
                    days_spent_borrow_food_or_rely_on_family_help_coping_strategy = if (item.days_spent_borrow_food_or_rely_on_family_help_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_borrow_food_or_rely_on_family_help_coping_strategy.toInt(),
                    days_spent_days_without_eating_coping_strategy = if (item.days_spent_days_without_eating_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_days_without_eating_coping_strategy.toInt(),
                    days_spent_begging_coping_strategy = if (item.days_spent_begging_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_begging_coping_strategy.toInt(),
                    days_spent_consume_wild_food_coping_strategy = if (item.days_spent_consume_wild_food_coping_strategy.isNullOrEmpty()) 0 else item.days_spent_consume_wild_food_coping_strategy.toInt(),
                    days_spent_other_coping_strategy = item.days_spent_other_coping_strategy,
                    take_children_out_of_school = item.take_children_out_of_school,
                    sale_of_production_assets = item.sale_of_production_assets,
                    use_of_child_labor = item.use_of_child_labor,
                    use_of_early_marriage = item.use_of_early_marriage,
                    give_up_health_care = item.give_up_health_care,
                    other_household_activities_in_past_12_months = item.other_household_activities_in_past_12_months,
                    comments = item.comments,
                    household_status = item.household_status
                ).apply {

                    members = pair.second?.map {
                        MemberPayload(
                            id = it.id,
                            remote_id = it.remote_id,
                            firstname = it.firstname,
                            middlename = it.middlename,
                            lastname = it.lastname,
                            sex = it.sex,
                            age = it.age,
                            date_of_birth = it.dob,
                            age_known = it.age_known,
                            dob_known = it.dob_known,
                            profile_picture = it.profile_picture,
                            is_member_respondent = MapUtil.mapping[it.is_member_respondent] ?: 0,
                            is_head = MapUtil.mapping[it.is_head] ?: 0,
                            family_bond_id = MapUtil.mapping[it.family_bond_id],
                            marital_status_id = MapUtil.mapping[it.marital_status_id],
                            birth_certificate = it.birth_certificate,
                            educational_level_id = MapUtil.mapping[it.educational_level_id],
                            school_attendance_id = MapUtil.mapping[it.school_attendance_id],
                            pregnancy_status = it.pregnancy_status,
                            disability_id = MapUtil.mapping[it.disability_id] ?: 1,
                            socio_professional_category_id = MapUtil.mapping[it.socio_professional_category_id],
                            sector_of_work_id = MapUtil.mapping[it.sector_of_work_id],
                            household_id = it.household_id,
                        )
                    }
                    number_of_members = members?.size ?: 0
                }

            }
        }.map { householdPayloadList ->

            val multiPartFormDataContent = MultiPartFormDataContent(
                formData {
                    applicationScope.launch {
                        householdPayloadList?.map { householdPayload ->

                            householdPayload.members?.map { member ->


                                async(Dispatchers.IO){
                                    append(
                                        FormPart("members_${member.id}",
                                            buildPacket { writeFully(File(member.profile_picture).readBytes()) },
                                            Headers.build {
                                                append(HttpHeaders.ContentType, "image/jpeg")
                                                append(
                                                    HttpHeaders.ContentDisposition,
                                                    "filename=\"members_${member.id}\""
                                                )
                                            }
                                        )
                                    )
                                }
                            }?.awaitAll()

                        }
                    }
                    append(FormPart("households", Json.encodeToString(householdPayloadList)))
                },
                boundary = "WebAppBoundary"
            )





            try {
                val userPref = preferencesManager.preferencesFlow.first()

                val response = api.uploadHouseholdBulk(multiPartFormDataContent, userPref.token)


                if (response.status.value in 200..299) {

                    emit(Resource.Success<BulkUploadResponse>(response.body()))
                } else {
                    emit(Resource.Failure<ErrorResponse>(response.body()))
                }
            } catch (throwable: Throwable) {
                emit(Resource.Exception(throwable, null))
            }

        }.collect()

    }.flowOn(Dispatchers.IO)


    companion object {
        const val TAG = "HouseholdRepository"
    }

}
//                                  val file = File(membersPayload.profile_picture)
//                                    appendInput(
//                                        key= index.toString(),
//                                        headers = Headers.build {
//                                            append(HttpHeaders.ContentType, "image/jpeg")
//                                            append(HttpHeaders.ContentDisposition,
//                                                "filename=members_${membersPayload.id}")
//                                        },
//                                        size = file.length()
//                                    ){
//                                        buildPacket { writeFully(file.readBytes()) }
//                                    }