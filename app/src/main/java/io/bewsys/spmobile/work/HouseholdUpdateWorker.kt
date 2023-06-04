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
                        remote_id = item.remote_id,
                        survey_no = item.survey_no!!,
                        temp_survey_no = item.temp_survey_no,
                        survey_date = item.survey_date!!,
                        initial_registration_type = item.initial_registration_type,
                        user_id = item.user_id!!.toInt(),
                        start_time = item.start_time!!,
                        finish_time = item.finish_time,
                        area_of_residence = MapUtil.intMappings[item.area_of_residence],
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
                        respondent_family_bond_to_head = MapUtil.intMappings[item.respondent_family_bond_to_head],
                        respondent_voter_id = item.respondent_voter_id,
                        respondent_phone_number = item.respondent_phone_number,
                        household_migration_status = MapUtil.intMappings[item.household_migration_status] ?: 160,
                        other_household_migration_status = item.other_household_migration_status,
                        duration_displaced_returned_repatriated_refugee = if (item.duration_displaced_returned_repatriated_refugee.isNullOrEmpty()) 0 else item.duration_displaced_returned_repatriated_refugee.toInt(),
                        unit_of_migration_duration = item.unit_of_migration_duration,
                        occupation_status_of_current_accommodation = MapUtil.intMappings[item.occupation_status_of_current_accommodation] ?: 75,
                        other_occupation_status_of_current_accommodation = item.other_occupation_status_of_current_accommodation,
                        main_material_of_exterior_walls = MapUtil.intMappings[item.main_material_of_exterior_walls] ?: 91,
                        other_main_material_of_exterior_walls = item.other_main_material_of_exterior_walls,
                        main_soil_material = MapUtil.intMappings[item.main_soil_material] ?: 101,
                        other_main_soil_material = item.other_main_soil_material,
                        number_of_rooms_used_for_sleeping = if (item.number_of_rooms_used_for_sleeping.isNullOrEmpty()) 0 else item.number_of_rooms_used_for_sleeping.toInt(),
                        type_of_fuel_used_for_household_cooking = MapUtil.intMappings[item.type_of_fuel_used_for_household_cooking] ?: 114,
                        other_type_of_fuel_used_for_household_cooking = item.other_type_of_fuel_used_for_household_cooking,
                        main_source_of_household_drinking_water = MapUtil.intMappings[item.main_source_of_household_drinking_water] ?: 129,
                        other_main_source_of_household_drinking_water = item.other_main_source_of_household_drinking_water,
                        type_of_household_toilet = MapUtil.intMappings[item.type_of_household_toilet] ?: 161,
                        other_type_of_household_toilet = item.other_type_of_household_toilet,
                        method_of_waste_disposal = MapUtil.intMappings[item.method_of_waste_disposal] ?: 144,
                        other_method_of_waste_disposal = item.other_method_of_waste_disposal,
                        place_to_wash_hands = MapUtil.intMappings[item.place_to_wash_hands],
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