package io.bewsys.spmobile.data.remote.model.login

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdsSurveyed(
    val CBT_observation: String?,
    val CBT_score: String?,
    val PMT_score: String?,
    val address: String?,
    val adults_18_plus: String?,
    val affected_by_climate_shock: String?,
    val affected_by_conflict: String?,
    val affected_by_epidemic: String?,
    val affected_by_other_shock: String?,
    val amount_of_cultivable_land_owned: String?,
    val archived_at: String?,
    val area_of_residence: String?,
    val bank_account_or_bank_card_available: String?,
    val cac: String?,
    val children_2_to_5: String?,
    val children_6_to_17: String?,
    val comments: String?,
    val community_id: String?,
    val correspondence_code: String?,
    val created_at: String?,
    val days_spent_adult_forfeit_meal_for_child_coping_strategy: String?,
    val days_spent_begging_coping_strategy: String?,
    val days_spent_borrow_food_or_rely_on_family_help_coping_strategy: String?,
    val days_spent_consume_wild_food_coping_strategy: String?,
    val days_spent_days_without_eating_coping_strategy: String?,
    val days_spent_eat_less_expensively_coping_strategy: String?,
    val days_spent_other_coping_strategy: String?,
    val days_spent_reduce_amount_consumed_coping_strategy: String?,
    val days_spent_reduce_meals_consumed_coping_strategy: String?,
    val deleted_at: String?,
    val duration_displaced_returned_repatriated_refugee: String?,
    val enumerator_id: String?,
    val finish_time: String?,
    val give_up_health_care: String?,
    val gps_latitude: String?,
    val gps_longitude: String?,
    val groupment_id: String?,
    val head_age_known: String?,
    val head_dob_known: String?,
    val health_area_id: String?,
    val health_zone_id: String?,
    val household_data_export_id: String?,
    val household_head: HouseholdHead?,
    val household_head_age: String?,
    val household_head_dob: String?,
    val household_head_firstname: String?,
    val household_head_lastname: String?,
    val household_head_middlename: String?,
    val household_head_phone_number: String?,
    val household_head_sex: String?,
    val household_head_voter_id_card: String?,
    val household_member_access_to_cultivable_land: String?,
    val household_member_owner_of_cultivable_land: String?,
    val household_member_with_benefit_from_social_assistance_program: String?,
    val household_migration_status: String?,
    val household_monthly_income: String?,
    val household_status: String?,
    val id: Long?,
    val initial_registration_type: String?,
    val is_income_regular: String?,
    val main_material_of_exterior_walls: String?,
    val main_soil_material: String?,
    val main_source_of_household_drinking_water: String?,
    val method_of_household_waste_disposal: String?,
    val minimum_monthly_income_necessary_live_without_difficulties: String?,
    val mobile_money_phone_number: String?,
    val mobile_money_username: String?,
    val name_of_social_assistance_program: String?,
    val number_of_air_conditioner_owned: String?,
    val number_of_bed_owned: String?,
    val number_of_bicycle_owned: String?,
    val number_of_canal_tnt_tvCable_owned: String?,
    val number_of_cars_owned: String?,
    val number_of_cart_owned: String?,
    val number_of_chair_owned: String?,
    val number_of_charcoal_iron_owned: String?,
    val number_of_computer_owned: String?,
    val number_of_cow_owned: String?,
    val number_of_days_in_week_consumed_cooking_oils: String?,
    val number_of_days_in_week_consumed_diary_products: String?,
    val number_of_days_in_week_consumed_fruits: String?,
    val number_of_days_in_week_consumed_legumes_or_nuts: String?,
    val number_of_days_in_week_consumed_meat: String?,
    val number_of_days_in_week_consumed_staple_foods: String?,
    val number_of_days_in_week_consumed_sugar_or_sweet_products: String?,
    val number_of_days_in_week_consumed_vegetables: String?,
    val number_of_dvd_driver_owned: String?,
    val number_of_electric_iron_owned: String?,
    val number_of_fan_owned: String?,
    val number_of_fridge_owned: String?,
    val number_of_goat_owned: String?,
    val number_of_guinea_pig_owned: String?,
    val number_of_handset_or_phone_owned: String?,
    val number_of_mattress_owned: String?,
    val number_of_meals_eaten_by_adults_18_plus_yesterday: String?,
    val number_of_meals_eaten_by_children_2_to_5_yesterday: String?,
    val number_of_meals_eaten_by_children_6_to_17_yesterday: String?,
    val number_of_members: String?,
    val number_of_mosquito_nets_owned: String?,
    val number_of_motorbike_owned: String?,
    val number_of_oil_stove_owned: String?,
    val number_of_pigs_owned: String?,
    val number_of_plow_owned: String?,
    val number_of_poultry_owned: String?,
    val number_of_rabbit_owned: String?,
    val number_of_radio_owned: String?,
    val number_of_rickshaw_owned: String?,
    val number_of_rooms_used_for_sleeping: String?,
    val number_of_sewing_machine_owned: String?,
    val number_of_sheep_owned: String?,
    val number_of_sofa_owned: String?,
    val number_of_solar_plate_owned: String?,
    val number_of_stove_or_oven_owned: String?,
    val number_of_table_owned: String?,
    val number_of_television_owned: String?,
    val number_of_wheelbarrow_owned: String?,
    val occupation_status_of_current_accommodation: String?,
    val other_household_migration_status: String?,
    val other_livestock_owned: String?,
    val other_main_material_of_exterior_walls: String?,
    val other_main_soil_material: String?,
    val other_main_source_of_household_drinking_water: String?,
    val other_method_of_household_waste_disposal: String?,
    val other_occupation_status_of_current_accommodation: String?,
    val other_type_of_fuel_used_for_household_cooking: String?,
    val other_type_of_household_toilet: String?,
    val others_household_activities_in_past_12_months: String?,
    val place_to_wash_hands: String?,
    val practice_of_cash_crop_farming_or_commercial_farming: String?,
    val profile_picture: String?,
    val province_id: String?,
    val refugee_code: String?,
    val respondent_dob: String?,
    val respondent_family_bond_to_head: String?,
    val respondent_firstname: String?,
    val respondent_lastname: String?,
    val respondent_middlename: String?,
    val respondent_phone_number: String?,
    val respondent_type: String?,
    val respondent_voter_id: String?,
    val sale_of_production_assets: String?,
    val shard_key: String?,
    val start_time: String?,
    val supervisor_id: String?,
    val survey_date: String?,
    val survey_no: String?,
    val take_children_out_of_school: String?,
    val team_leader_id: String?,
    val temp_survey_no: String?,
    val territory_id: String?,
    val type_of_fuel_used_for_household_cooking: String?,
    val type_of_household_toilet: String?,
    val unit_of_cultivable_land_owned: String?,
    val unit_of_migration_duration: String?,
    val updated_at: String?,
    val use_of_child_labor: String?,
    val use_of_early_marriage: String?,
    val vac_assistant_id: String?,
    val vac_team_leader_id: String?,
    val validation_description: String?,
    val validator_user_id: String?,
    val village_or_quartier: String?
)