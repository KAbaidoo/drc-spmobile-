package io.bewsys.spmobile.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import io.bewsys.spmobile.Database
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.local.HouseholdModel
import io.bewsys.spmobile.data.prefsstore.PreferencesManager
import io.bewsys.spmobile.data.remote.HouseholdApi
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.remote.model.login.ErrorResponse
import io.bewsys.spmobile.util.Resource
import io.ktor.client.call.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext


class HouseholdRepository(
    db: Database,
    private val api: HouseholdApi,
    private val preferencesManager: PreferencesManager
) {
    private val queries = db.householdQueries

    suspend fun getAllHouseholds(): Flow<List<HouseholdEntity>> =
        queries.getAllHousholds().asFlow().mapToList(context = Dispatchers.Default)


    suspend fun getHousehold(id: Long): HouseholdEntity? =
        withContext(Dispatchers.IO) {
            queries.getById(id).executeAsOneOrNull()
        }

    suspend fun insertHousehold(
        householdModel: HouseholdModel
    ): Unit = withContext(Dispatchers.IO) {

        val userPref = preferencesManager.preferencesFlow.first()

        householdModel.apply {
            queries.insertHousehold(
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
                user_id = userPref.id,
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
                days_spent_reduce_meals_consumed_coping_strategy = days_spent_reduce_meals_consumed_coping_strategy
            )
        }

    }

    suspend fun getLastInsertedRowId(): Long = withContext(Dispatchers.IO) {
        queries.lastInsertRowId().executeAsOne()

    }

    suspend fun updateStatus(status: String, id: Long) {
        queries.updateStatus(status, id)
    }

    //    ================================================================
    //   *********************** network calls  ************************
    suspend fun uploadHousehold(payload: HouseholdPayload) = flow {

        try {
            val userPref = preferencesManager.preferencesFlow.first()
            val response = api.uploadHousehold(payload, userPref.token)

            if (response.status.value in 200..299) {
                emit(Resource.Success<Any>(response.body()))
            } else {
                emit(Resource.Failure<ErrorResponse>(response.body()))
            }
        } catch (throwable: Throwable) {
            emit(Resource.Exception(throwable, null))
        }
    }.flowOn(Dispatchers.IO)

}