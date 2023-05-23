package io.bewsys.spmobile.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.MapUtil
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HouseholdUpdateWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params), KoinComponent {

    val repository: HouseholdRepository by inject()

    override suspend fun doWork(): Result {
        val id = inputData.getLong(KEY_DATA_ID, -1)
        return withContext(Dispatchers.IO) {

            try {
                if (id < 0) {
                    Log.e(TAG, "Invalid input id")
                    throw IllegalArgumentException("Invalid input id")
                }
                val item = getItem(id)
                uploadItem(item!!)


            } catch (throwable: Throwable) {
                Log.e(TAG, "Error uploading data")
                Result.failure()
            }!!

        }

    }


    private suspend fun uploadItem(item: HouseholdEntity): Result {
        return withContext(Dispatchers.IO) {
            var result: Result = Result.failure()

            item.apply {
                repository.updateHouseholdRemote(item.id,
                    HouseholdPayload(
                        remote_id = remote_id,
                        survey_no = survey_no!!,
                        temp_survey_no = temp_survey_no,
                        survey_date = survey_date!!,
                        initial_registration_type = initial_registration_type,
                        user_id = user_id!!.toInt(),
                        start_time = start_time!!,
                        finish_time = finish_time,
                        area_of_residence = MapUtil.mapping[area_of_residence],
                        province_id = if (province_id.isNullOrEmpty()) null else province_id.toInt(),
                        community_id = if (community_id.isNullOrEmpty()) null else community_id.toInt(),
                        territory_id = if (territory_id.isNullOrEmpty()) null else territory_id.toInt(),
                        groupment_id = if (groupment_id.isNullOrEmpty()) null else groupment_id.toInt(),
                        health_zone_id = if (health_zone_id.isNullOrEmpty()) null else health_zone_id.toInt(),
                        health_area_id = if (health_area_id.isNullOrEmpty()) null else health_area_id.toInt(),
                        village_or_quartier = village_or_quartier,
                        address = address,
                        cac = cac,
                        gps_longitude = gps_longitude,
                        gps_latitude = gps_latitude,
                        number_of_members = 0,
                        household_head_firstname = household_head_firstname,
                        household_head_lastname = household_head_lastname,
                        household_head_middlename = household_head_middlename,
                        household_head_voter_id_card = household_head_voter_id_card,
                        household_head_phone_number = household_head_phone_number,
                        head_age_known = head_age_known,
                        household_head_age = if (household_head_age.isNullOrEmpty()) null else household_head_age.toInt(),
                        head_dob_known = head_dob_known,
                        household_head_dob = household_head_dob,
                        household_head_sex = household_head_sex,

                        respondent_firstname = respondent_firstname,
                        respondent_middlename = respondent_firstname,
                        respondent_lastname = respondent_lastname,
                        respondent_dob = respondent_dob,
                        respondent_family_bond_to_head = MapUtil.mapping[respondent_family_bond_to_head],
                        respondent_voter_id = respondent_voter_id,
                        respondent_phone_number = respondent_phone_number,
                        household_migration_status = MapUtil.mapping[household_migration_status],
                        other_household_migration_status = other_household_migration_status,
                        duration_displaced_returned_repatriated_refugee = if (duration_displaced_returned_repatriated_refugee.isNullOrEmpty()) 0 else duration_displaced_returned_repatriated_refugee.toInt(),
                        unit_of_migration_duration = unit_of_migration_duration,
                        occupation_status_of_current_accommodation = MapUtil.mapping[occupation_status_of_current_accommodation],
                        other_occupation_status_of_current_accommodation = other_occupation_status_of_current_accommodation,
                        main_material_of_exterior_walls = MapUtil.mapping[main_material_of_exterior_walls],
                        other_main_material_of_exterior_walls = other_main_material_of_exterior_walls,
                        main_soil_material = MapUtil.mapping[main_soil_material],
                        other_main_soil_material = other_main_soil_material,
                        number_of_rooms_used_for_sleeping = if (number_of_rooms_used_for_sleeping.isNullOrEmpty()) 0 else number_of_rooms_used_for_sleeping.toInt(),
                        type_of_fuel_used_for_household_cooking = MapUtil.mapping[type_of_fuel_used_for_household_cooking],
                        other_type_of_fuel_used_for_household_cooking = other_type_of_fuel_used_for_household_cooking,
                        main_source_of_household_drinking_water = MapUtil.mapping[main_source_of_household_drinking_water],
                        other_main_source_of_household_drinking_water = other_main_source_of_household_drinking_water,
                        type_of_household_toilet = MapUtil.mapping[type_of_household_toilet],
                        other_type_of_household_toilet = other_type_of_household_toilet,
                        method_of_waste_disposal = MapUtil.mapping[method_of_waste_disposal],
                        other_method_of_waste_disposal = other_method_of_waste_disposal,
                        place_to_wash_hands = MapUtil.mapping[place_to_wash_hands],
                        minimum_monthly_income_necessary_live_without_difficulties = if (minimum_monthly_income_necessary_live_without_difficulties.isNullOrEmpty()) 0 else minimum_monthly_income_necessary_live_without_difficulties.toLong(),
                        household_monthly_income = if (household_monthly_income.isNullOrEmpty()) 0 else household_monthly_income.toLong(),
                        is_income_regular = is_income_regular,
                        bank_account_or_bank_card_available = bank_account_or_bank_card_available,
                        mobile_money_username = mobile_money_username,
                        mobile_money_phone_number = mobile_money_phone_number,
                        household_member_access_to_cultivable_land = household_member_access_to_cultivable_land,
                        household_member_owner_of_cultivable_land = household_member_owner_of_cultivable_land,
                        amount_of_cultivable_land_owned = if (amount_of_cultivable_land_owned.isNullOrEmpty()) 0 else amount_of_cultivable_land_owned.toInt(),
                        practice_of_cash_crop_farming_or_commercial_farming = practice_of_cash_crop_farming_or_commercial_farming,
                        number_of_goat_owned = if (number_of_goat_owned.isNullOrEmpty()) 0 else number_of_goat_owned.toInt(),
                        number_of_sheep_owned = if (number_of_sheep_owned.isNullOrEmpty()) 0 else number_of_sheep_owned.toInt(),
                        number_of_cow_owned = if (number_of_cow_owned.isNullOrEmpty()) 0 else number_of_cow_owned.toInt(),
                        number_of_rabbit_owned = if (number_of_rabbit_owned.isNullOrEmpty()) 0 else number_of_rabbit_owned.toInt(),
                        number_of_pigs_owned = if (number_of_pigs_owned.isNullOrEmpty()) 0 else number_of_pigs_owned.toInt(),
                        number_of_poultry_owned = if (number_of_poultry_owned.isNullOrEmpty()) 0 else number_of_poultry_owned.toInt(),
                        number_of_guinea_pig_owned = if (number_of_guinea_pig_owned.isNullOrEmpty()) 0 else number_of_guinea_pig_owned.toInt(),
                        other_livestock_owned = other_livestock_owned,
                        number_of_cars_owned = if (number_of_cars_owned.isNullOrEmpty()) 0 else number_of_cars_owned.toInt(),
                        number_of_motorbike_owned = if (number_of_motorbike_owned.isNullOrEmpty()) 0 else number_of_motorbike_owned.toInt(),
                        number_of_bicycle_owned = if (number_of_bicycle_owned.isNullOrEmpty()) 0 else number_of_bicycle_owned.toInt(),
                        number_of_cart_owned = if (number_of_cart_owned.isNullOrEmpty()) 0 else number_of_cart_owned.toInt(),
                        number_of_plow_owned = if (number_of_plow_owned.isNullOrEmpty()) 0 else number_of_plow_owned.toInt(),
                        number_of_wheelbarrow_owned = if (number_of_wheelbarrow_owned.isNullOrEmpty()) 0 else number_of_wheelbarrow_owned.toInt(),
                        number_of_rickshaw_owned = if (number_of_rickshaw_owned.isNullOrEmpty()) 0 else number_of_rickshaw_owned.toInt(),
                        number_of_mattress_owned = if (number_of_mattress_owned.isNullOrEmpty()) 0 else number_of_mattress_owned.toInt(),
                        number_of_fridge_owned = if (number_of_fridge_owned.isNullOrEmpty()) 0 else number_of_fridge_owned.toInt(),
                        number_of_fan_owned = if (number_of_fan_owned.isNullOrEmpty()) 0 else number_of_fan_owned.toInt(),
                        number_of_air_conditioner_owned = if (number_of_air_conditioner_owned.isNullOrEmpty()) 0 else number_of_air_conditioner_owned.toInt(),
                        number_of_radio_owned = if (number_of_radio_owned.isNullOrEmpty()) 0 else number_of_radio_owned.toInt(),

                        number_of_television_owned = if (number_of_television_owned.isNullOrEmpty()) 0 else number_of_television_owned.toInt(),
                        number_of_canal_tnt_tvCable_owned = if (number_of_canal_tnt_tv_cable_owned.isNullOrEmpty()) 0 else number_of_canal_tnt_tv_cable_owned.toInt(),
                        number_of_dvd_driver_owned = if (number_of_dvd_driver_owned.isNullOrEmpty()) 0 else number_of_dvd_driver_owned.toInt(),
                        number_of_handset_or_phone_owned = if (number_of_handset_or_phone_owned.isNullOrEmpty()) 0 else number_of_handset_or_phone_owned.toInt(),
                        number_of_computer_owned = if (number_of_computer_owned.isNullOrEmpty()) 0 else number_of_computer_owned.toInt(),
                        number_of_stove_or_oven_owned = if (number_of_stove_or_oven_owned.isNullOrEmpty()) 0 else number_of_stove_or_oven_owned.toInt(),
                        number_of_oil_stove_owned = if (number_of_oil_stove_owned.isNullOrEmpty()) 0 else number_of_oil_stove_owned.toInt(),
                        number_of_solar_plate_owned = if (number_of_solar_plate_owned.isNullOrEmpty()) 0 else number_of_solar_plate_owned.toInt(),
                        number_of_sewing_machine_owned = if (number_of_sewing_machine_owned.isNullOrEmpty()) 0 else number_of_sewing_machine_owned.toInt(),
                        number_of_electric_iron_owned = if (number_of_electric_iron_owned.isNullOrEmpty()) 0 else number_of_electric_iron_owned.toInt(),

                        number_of_charcoal_iron_owned = if (number_of_charcoal_iron_owned.isNullOrEmpty()) 0 else number_of_charcoal_iron_owned.toInt(),
                        number_of_bed_owned = if (number_of_bed_owned.isNullOrEmpty()) 0 else number_of_bed_owned.toInt(),
                        number_of_table_owned = if (number_of_table_owned.isNullOrEmpty()) 0 else number_of_table_owned.toInt(),
                        number_of_chair_owned = if (number_of_chair_owned.isNullOrEmpty()) 0 else number_of_chair_owned.toInt(),
                        number_of_sofa_owned = if (number_of_sofa_owned.isNullOrEmpty()) 0 else number_of_sofa_owned.toInt(),

                        number_of_mosquito_nets_owned = if (number_of_mosquito_nets_owned.isNullOrEmpty()) 0 else number_of_mosquito_nets_owned.toInt(),
                        number_of_meals_eaten_by_adults_18_plus_yesterday = if (number_of_meals_eaten_by_adults_18_plus_yesterday.isNullOrEmpty()) 0 else number_of_meals_eaten_by_adults_18_plus_yesterday.toInt(),
                        number_of_meals_eaten_by_children_6_to_17_yesterday = if (number_of_meals_eaten_by_children_6_to_17_yesterday.isNullOrEmpty()) 0 else number_of_meals_eaten_by_children_6_to_17_yesterday.toInt(),
                        number_of_meals_eaten_by_children_2_to_5_yesterday = if (number_of_meals_eaten_by_children_2_to_5_yesterday.isNullOrEmpty()) 0 else number_of_meals_eaten_by_children_2_to_5_yesterday.toInt(),
                        number_of_days_in_week_consumed_staple_foods = if (number_of_days_in_week_consumed_staple_foods.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_staple_foods.toInt(),
                        number_of_days_in_week_consumed_legumes_or_nuts = if (number_of_days_in_week_consumed_legumes_or_nuts.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_legumes_or_nuts.toInt(),
                        number_of_days_in_week_consumed_dairy_products = if (number_of_days_in_week_consumed_dairy_products.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_dairy_products.toInt(),
                        number_of_days_in_week_consumed_meat = if (number_of_days_in_week_consumed_meat.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_meat.toInt(),

                        number_of_days_in_week_consumed_vegetables = if (number_of_days_in_week_consumed_vegetables.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_vegetables.toInt(),
                        number_of_days_in_week_consumed_fruits = if (number_of_days_in_week_consumed_fruits.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_fruits.toInt(),
                        number_of_days_in_week_consumed_cooking_oils = if (number_of_days_in_week_consumed_cooking_oils.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_cooking_oils.toInt(),
                        number_of_days_in_week_consumed_sugar_or_sweet_products = if (number_of_days_in_week_consumed_sugar_or_sweet_products.isNullOrEmpty()) 0 else number_of_days_in_week_consumed_sugar_or_sweet_products.toInt(),
                        household_member_with_benefit_from_social_assistance_program = household_member_with_benefit_from_social_assistance_program,
                        name_of_social_assistance_program = name_of_social_assistance_program,
                        affected_by_conflict = affected_by_conflict,
                        affected_by_epidemic = affected_by_epidemic,
                        affected_by_climate_shock = affected_by_climate_shock,
                        affected_by_other_shock = affected_by_other_shock,
                        days_spent_reduce_amount_consumed_coping_strategy = if (days_spent_reduce_amount_consumed_coping_strategy.isNullOrEmpty()) 0 else days_spent_reduce_amount_consumed_coping_strategy.toInt(),
                        days_spent_reduce_meals_consumed_coping_strategy = if (days_spent_reduce_meals_consumed_coping_strategy.isNullOrEmpty()) 0 else days_spent_reduce_meals_consumed_coping_strategy.toInt(),
                        days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy = if (days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy.isNullOrEmpty()) 0 else days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy.toInt(),
                        days_spent_eat_less_expensively_coping_strategy = if (days_spent_eat_less_expensively_coping_strategy.isNullOrEmpty()) 0 else days_spent_eat_less_expensively_coping_strategy.toInt(),
                        days_spent_borrow_food_or_rely_on_family_help_coping_strategy = if (days_spent_borrow_food_or_rely_on_family_help_coping_strategy.isNullOrEmpty()) 0 else days_spent_borrow_food_or_rely_on_family_help_coping_strategy.toInt(),
                        days_spent_days_without_eating_coping_strategy = if (days_spent_days_without_eating_coping_strategy.isNullOrEmpty()) 0 else days_spent_days_without_eating_coping_strategy.toInt(),
                        days_spent_begging_coping_strategy = if (days_spent_begging_coping_strategy.isNullOrEmpty()) 0 else days_spent_begging_coping_strategy.toInt(),
                        days_spent_consume_wild_food_coping_strategy = if (days_spent_consume_wild_food_coping_strategy.isNullOrEmpty()) 0 else days_spent_consume_wild_food_coping_strategy.toInt(),
                        days_spent_other_coping_strategy = days_spent_other_coping_strategy,
                        take_children_out_of_school = take_children_out_of_school,
                        sale_of_production_assets = sale_of_production_assets,
                        use_of_child_labor = use_of_child_labor,
                        use_of_early_marriage = use_of_early_marriage,
                        give_up_health_care = give_up_health_care,
                        other_household_activities_in_past_12_months = other_household_activities_in_past_12_months,
                        comments = comments,
                        household_status = household_status
                    )
                ).collectLatest { response ->
                    result = when (response) {
                        is Resource.Success -> {
                            Result.success()
                        }
                        is Resource.Exception -> {
                            response.throwable.localizedMessage?.let { Log.d(TAG, it) }
                            Result.failure()
                        }

                        else -> {
                            val res = response as Resource.Failure
                            Log.d(TAG, "${res.error}")
                            Result.failure()
                        }
                    }
                }
            }
            result
        }
    }




    private suspend fun getItem(id: Long): HouseholdEntity? =
        repository.getHousehold(id)
companion object{
    private const val TAG = "HouseholdUpdateWorker"
}

}