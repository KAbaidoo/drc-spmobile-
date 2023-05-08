package io.bewsys.spmobile.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.bewsys.spmobile.KEY_DATA_ID
import io.bewsys.spmobile.data.HouseholdEntity
import io.bewsys.spmobile.data.remote.model.household.HouseholdPayload
import io.bewsys.spmobile.data.repository.HouseholdRepository
import io.bewsys.spmobile.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
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
                item?.let { uploadItem(item) }


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
                repository.updateHousehold(
                    HouseholdPayload(
                        survey_no,
                        supervisor_id,
                        team_leader_id,
                        user_id,
                        survey_date,
                        initial_registration_type,
                        respondent_type.toString(),
                        respondent_firstname,
                        respondent_middlename,
                        respondent_lastname,
                        respondent_voter_id,
                        respondent_phone_number,
                        household_head_firstname,
                        household_head_lastname,
                        household_head_dob,
                        household_head_sex,
                        head_age_known,
                        head_dob_known,
                        household_head_voter_id_card,
                        household_head_phone_number,
                        household_head_age,
                        household_head_middlename,
                        household_head_marital_status_id,
                        household_head_birth_certificate,
                        household_head_educational_level_id,
                        household_head_disability_id,
                        household_head_socio_professional_category_id,
                        household_head_school_attendance_id,
                        household_head_sector_of_work_id,
                        household_head_pregnancy_status,
                        household_migration_status,
                        area_of_residence,
                        province_id,
                        community_id,
                        territory_id,
                        health_zone_id,
                        health_area_id,
                        groupment_id,
                        duration_displaced_returned_repatriated_refugee,
                        unit_of_migration_duration,
                        territory_or_town,
                        household_status,
                        status,
                        bank_account_or_bank_card_available,
                        is_income_regular,
                        household_member_access_to_cultivable_land,
                        household_member_owner_of_cultivable_land,
                        practice_of_cash_crop_farming_or_commercial_farming,
                        has_livestock,
                        has_household_goods,
                        affected_by_conflict,
                        affected_by_epidemic,
                        affected_by_climate_shock,
                        take_children_out_of_school,
                        sale_of_production_assets,
                        use_of_child_labor,
                        use_of_early_marriage,
                        give_up_health_care,
                        days_spent_other_coping_strategy,
                        number_of_months_displaced_returned_repatriated_refugee,
                        gps_longitude,
                        gps_latitude,
                        start_time,
                        finish_time,
                        address,
                        comments,
                        profile_picture,
                        mobile_money_username,
                        mobile_money_phone_number,
                        village_or_quartier,
                        other_household_migration_status,
                        other_occupation_status_of_current_accommodation,
                        other_main_material_of_exterior_walls,
                        other_main_soil_material,
                        occupation_status_of_current_accommodation,
                        main_material_of_exterior_walls,
                        main_soil_material,
                        number_of_rooms_used_for_sleeping,
                        type_of_fuel_used_for_household_cooking,
                        other_type_of_fuel_used_for_household_cooking,
                        main_source_of_household_drinking_water,
                        other_main_source_of_household_drinking_water,
                        type_of_household_toilet,
                        other_type_of_household_toilet,
                        method_of_waste_disposal,
                        place_to_wash_hands,
                        minimum_monthly_income_necessary_live_without_difficulties,
                        household_monthly_income,
                        number_of_goat_owned,
                        number_of_sheep_owned,
                        number_of_cow_owned,
                        number_of_rabbit_owned,
                        number_of_pigs_owned,
                        number_of_poultry_owned,
                        number_of_guinea_pig_owned,
                        number_of_cars_owned,
                        number_of_motorbike_owned,
                        number_of_bicycle_owned,
                        number_of_cart_owned,
                        number_of_plow_owned,
                        number_of_wheelbarrow_owned,
                        number_of_rickshaw_owned,
                        number_of_mattress_owned,
                        number_of_fridge_owned,
                        number_of_fan_owned,
                        number_of_air_conditioner_owned,
                        number_of_radio_owned,
                        number_of_television_owned,
                        number_of_canal_tnt_tv_cable_owned,
                        number_of_dvd_driver_owned,
                        number_of_handset_or_phone_owned,
                        number_of_computer_owned,
                        number_of_stove_or_oven_owned,
                        number_of_oil_stove_owned,
                        number_of_solar_plate_owned,
                        number_of_sewing_machine_owned,
                        number_of_electric_iron_owned,
                        number_of_charcoal_iron_owned,
                        number_of_bed_owned,
                        number_of_table_owned,
                        number_of_chair_owned,
                        number_of_sofa_owned,
                        number_of_mosquito_nets_owned,
                        amount_of_cultivable_land_owned,
                        number_of_meals_eaten_by_adults_18_plus_yesterday,
                        number_of_meals_eaten_by_children_6_to_17_yesterday,
                        number_of_meals_eaten_by_children_2_to_5_yesterday,
                        number_of_days_in_week_consumed_staple_foods,
                        number_of_days_in_week_consumed_legumes_or_nuts,
                        number_of_days_in_week_consumed_dairy_products,
                        number_of_days_in_week_consumed_meat,
                        number_of_days_in_week_consumed_vegetables,
                        number_of_days_in_week_consumed_fruits,
                        number_of_days_in_week_consumed_cooking_oils,
                        number_of_days_in_week_consumed_sugar_or_sweet_products,
                        days_spent_reduce_amount_consumed_coping_strategy,
                        days_spent_reduce_meals_consumed_coping_strategy,
                        days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy,
                        days_spent_eat_less_expensively_coping_strategy,
                        days_spent_borrow_food_or_rely_on_family_help_coping_strategy,
                        days_spent_days_without_eating_coping_strategy,
                        days_spent_begging_coping_strategy,
                        days_spent_consume_wild_food_coping_strategy,
                        other_household_activities_in_past_12_months,
                        other_method_of_waste_disposal,
                        other_livestock_owned,
                        household_member_with_benefit_from_social_assistance_program,
                        name_of_social_assistance_program,
                        affected_by_other_shock,
                        remote_id,
                        temp_survey_no,
                        cac,
                    )
                ).collectLatest { response ->
                    result = when (response) {
                        is Resource.Success -> {

//                            val householdResponse = response.data as HouseholdResponse
//                            householdResponse.household.let {
//                                updateItem(item.id, remoteId =it.id.toString(), surveyNo = it.survey_no )
//                            }
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


//    private suspend fun updateItem(id: Long, remoteId:String,surveyNo:String) {
//        delay(2000L)
//        repository.updateStatus("submitted", id, remoteId, surveyNo)
//    }

    private suspend fun getItem(id: Long): HouseholdEntity? =
        repository.getHousehold(id)
companion object{
    private const val TAG = "HouseholdUpdateWorker"
}

}