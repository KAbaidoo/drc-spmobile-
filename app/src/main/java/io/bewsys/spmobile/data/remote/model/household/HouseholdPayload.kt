package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdPayload(
    val survey_no: String?=null,
//    val supervisor_id: String?=null,
    val team_leader_id: String?=null,
    val user_id: Long?=null,
    val survey_date: String?=null,
    val initial_registration_type: String?=null,
//    val respondent_type: Long?=null,
    val respondent_firstname: String?=null,
    val respondent_middlename: String?=null,
    val respondent_lastname: String?=null,
    val respondent_dob: String?=null,
    val respondent_family_bond_to_head: Long?=null,
    val respondent_voter_id: String?=null,
    val respondent_phone_number: String?=null,
    val household_head_firstname: String?=null,
    val household_head_lastname: String?=null,
    val household_head_dob: String?=null,
    val household_head_sex: String?=null,
    val head_age_known: String?=null,
    val head_dob_known: String?=null,
    val household_head_voter_id_card: String?=null,
    val household_head_phone_number: String?=null,
    val household_head_age: String?=null,
    val household_head_middlename: String?=null,
    val household_head_marital_status_id: String?=null,
    val household_head_birth_certificate: String?=null,
    val household_head_educational_level_id: String?=null,
    val household_head_disability_id: String?=null,
    val household_head_socio_professional_category_id: String?=null,
    val household_head_school_attendance_id: String?=null,
    val household_head_sector_of_work_id: String?=null,
    val household_head_pregnancy_status: String?=null,
    val household_migration_status: Long?=null,
    val is_head_respondent: Long?=null,
    val area_of_residence: Long?=null,
    val province_id: Long?=null,
    val community_id: Long?=null,
    val territory_id: Long?=null,
    val health_zone_id: Long?=null,
    val health_area_id: Long?=null,
    val groupment_id: Long?=null,
    val duration_displaced_returned_repatriated_refugee: Long?=null,
    val unit_of_migration_duration: String?=null,
    val territory_or_town: String?=null,
    val household_status: String?=null,
    val status: String?=null,
    val bank_account_or_bank_card_available: String?=null,
    val is_income_regular: String?=null,
    val household_member_access_to_cultivable_land: String?=null,
    val household_member_owner_of_cultivable_land: String?=null,
    val practice_of_cash_crop_farming_or_commercial_farming: String?=null,
    val has_livestock: String?=null,
    val has_household_goods: String?=null,
    val affected_by_conflict: String?=null,
    val affected_by_epidemic: String?=null,
    val affected_by_climate_shock: String?=null,
    val take_children_out_of_school: String?=null,
    val sale_of_production_assets: String?=null,
    val use_of_child_labor: String?=null,
    val use_of_early_marriage: String?=null,
    val give_up_health_care: String?=null,
    val days_spent_other_coping_strategy: String?=null,
    val number_of_months_displaced_returned_repatriated_refugee: Long?=null,
    val gps_longitude: String?=null,
    val gps_latitude: String?=null,
    val start_time: String?=null,
    val finish_time: String?=null,
    val address: String?=null,
    val comments: String?=null,
    val profile_picture: String?=null,
    val mobile_money_username: String?=null,
    val mobile_money_phone_number: String?=null,
    val village_or_quartier: String?=null,
    val other_household_migration_status: String?=null,
    val other_occupation_status_of_current_accommodation: String?=null,
    val other_main_material_of_exterior_walls: String?=null,
    val other_main_soil_material: String?=null,
    val occupation_status_of_current_accommodation: Long?=null,
    val main_material_of_exterior_walls: Long?=null,
    val main_soil_material: Long?=null,
    val number_of_rooms_used_for_sleeping: Long?=null,
    val type_of_fuel_used_for_household_cooking: Long?=null,
    val other_type_of_fuel_used_for_household_cooking: String?=null,
    val main_source_of_household_drinking_water: Long?=null,
    val other_main_source_of_household_drinking_water: String?=null,
    val type_of_household_toilet: Long?=null,
    val other_type_of_household_toilet: String?=null,
    val method_of_waste_disposal: Long?=null,
    val place_to_wash_hands: Long?=null,
    val minimum_monthly_income_necessary_live_without_difficulties: Long?=null,
    val household_monthly_income: Long?=null,
    val number_of_goat_owned: Long?=null,
    val number_of_sheep_owned: Long?=null,
    val number_of_cow_owned: Long?=null,
    val number_of_rabbit_owned: Long?=null,
    val number_of_pigs_owned: Long?=null,
    val number_of_poultry_owned: Long?=null,
    val number_of_guinea_pig_owned: Long?=null,
    val number_of_cars_owned: Long?=null,
    val number_of_motorbike_owned: Long?=null,
    val number_of_bicycle_owned: Long?=null,
    val number_of_cart_owned: Long?=null,
    val number_of_plow_owned: Long?=null,
    val number_of_wheelbarrow_owned: Long?=null,
    val number_of_rickshaw_owned: Long?=null,
    val number_of_mattress_owned: Long?=null,
    val number_of_fridge_owned: Long?=null,
    val number_of_fan_owned: Long?=null,
    val number_of_air_conditioner_owned: Long?=null,
    val number_of_radio_owned: Long?=null,
    val number_of_television_owned: Long?=null,
    val number_of_canal_tnt_tv_cable_owned: Long?=null,
    val number_of_dvd_driver_owned: Long?=null,
    val number_of_handset_or_phone_owned: Long?=null,
    val number_of_computer_owned: Long?=null,
    val number_of_stove_or_oven_owned: Long?=null,
    val number_of_oil_stove_owned: Long?=null,
    val number_of_solar_plate_owned: Long?=null,
    val number_of_sewing_machine_owned: Long?=null,
    val number_of_electric_iron_owned: Long?=null,
    val number_of_charcoal_iron_owned: Long?=null,
    val number_of_bed_owned: Long?=null,
    val number_of_table_owned: Long?=null,
    val number_of_chair_owned: Long?=null,
    val number_of_sofa_owned: Long?=null,
    val number_of_mosquito_nets_owned: Long?=null,
    val amount_of_cultivable_land_owned: Long?=null,
    val number_of_meals_eaten_by_adults_18_plus_yesterday: Long?=null,
    val number_of_meals_eaten_by_children_6_to_17_yesterday: Long?=null,
    val number_of_meals_eaten_by_children_2_to_5_yesterday: Long?=null,
    val number_of_days_in_week_consumed_staple_foods: Long?=null,
    val number_of_days_in_week_consumed_legumes_or_nuts: Long?=null,
    val number_of_days_in_week_consumed_dairy_products: Long?=null,
    val number_of_days_in_week_consumed_meat: Long?=null,
    val number_of_days_in_week_consumed_vegetables: Long?=null,
    val number_of_days_in_week_consumed_fruits: Long?=null,
    val number_of_days_in_week_consumed_cooking_oils: Long?=null,
    val number_of_days_in_week_consumed_sugar_or_sweet_products: Long?=null,
    val days_spent_reduce_amount_consumed_coping_strategy: Long?=null,
    val days_spent_reduce_meals_consumed_coping_strategy: Long?=null,
    val days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy: Long?=null,
    val days_spent_eat_less_expensively_coping_strategy: Long?=null,
    val days_spent_borrow_food_or_rely_on_family_help_coping_strategy: Long?=null,
    val days_spent_days_without_eating_coping_strategy: Long?=null,
    val days_spent_begging_coping_strategy: Long?=null,
    val days_spent_consume_wild_food_coping_strategy: Long?=null,
    val other_household_activities_in_past_12_months: String?=null,
    val other_method_of_waste_disposal: String?=null,
    val other_livestock_owned: String?=null,
    val household_member_with_benefit_from_social_assistance_program: String?=null,
    val name_of_social_assistance_program: String?=null,
    val affected_by_other_shock: String?=null,
    val temp_survey_no: String? = null,
    )

/*
@Serializable
data class HouseholdPayload(
    val id: Long?=null,
    val remote_id: Long?=null,
    val survey_no: String?=null,
    val cac: String?=null,
    val temp_survey_no: String?=null,
    val supervisor_id: String?=null,
    val team_leader_id: String?=null,
    val user_id: Long?=null,
    val survey_date: String?=null,
    val consent: String?=null,
    val CBT_score: Long?=null,
    val initial_registration_type: String?=null,
    val respondent_type: Long?=null,

    val respondent_firstname: String?=null,
    val respondent_middlename: String?=null,
    val respondent_lastname: String?=null,
    val respondent_dob: String?=null,
    val respondent_family_bond_to_head: Long?=null,
    val respondent_voter_id: String?=null,
    val respondent_phone_number: String?=null,
    val household_head_firstname: String?=null,
    val household_head_lastname: String?=null,
    val household_head_dob: String?=null,
    val household_head_sex: String?=null,
    val head_age_known: String?=null,
    val head_dob_known: String?=null,
    val household_head_voter_id_card: String?=null,
    val household_head_phone_number: String?=null,
    val household_head_age: String?=null,
    val household_head_middlename: String?=null,
    val household_head_marital_status_id: String?=null,
    val household_head_birth_certificate: String?=null,
    val household_head_educational_level_id: String?=null,
    val household_head_disability_id: String?=null,
    val household_head_socio_professional_category_id: String?=null,
    val household_head_school_attendance_id: String?=null,
    val household_head_sector_of_work_id: String?=null,
    val household_head_pregnancy_status: String?=null,

    val household_migration_status: Long?=null,
    val is_head_respondent: Long?=null,
    val area_of_residence: Long?=null,
    val province_id: Long?=null,
    val community_id: Long?=null,
    val territory_id: Long?=null,
    val health_zone_id: Long?=null,
    val health_area_id: Long?=null,
    val groupment_id: Long?=null,
    val duration_displaced_returned_repatriated_refugee: Long?=null,
    val unit_of_migration_duration: String?=null,
    val territory_or_town: String?=null,
    val household_status: String?=null,
    val status: String?=null,
    val bank_account_or_bank_card_available: String?=null,
    val is_income_regular: String?=null,
    val household_member_access_to_cultivable_land: String?=null,
    val household_member_owner_of_cultivable_land: String?=null,
    val practice_of_cash_crop_farming_or_commercial_farming: String?=null,
    val has_livestock: String?=null,
    val has_household_goods: String?=null,
    val affected_by_conflict: String?=null,
    val affected_by_epidemic: String?=null,
    val affected_by_climate_shock: String?=null,
    val take_children_out_of_school: String?=null,
    val sale_of_production_assets: String?=null,
    val use_of_child_labor: String?=null,
    val use_of_early_marriage: String?=null,
    val give_up_health_care: String?=null,
    val days_spent_other_coping_strategy: String?=null,
    val number_of_months_displaced_returned_repatriated_refugee: Long?=null,
    val gps_longitude: String?=null,
    val gps_latitude: String?=null,
    val start_time: String?=null,
    val finish_time: String?=null,
    val address: String?=null,
    val comments: String?=null,
    val profile_picture: String?=null,
    val mobile_money_username: String?=null,
    val mobile_money_phone_number: String?=null,
    val village_or_quartier: String?=null,
    val other_household_migration_status: String?=null,
    val other_occupation_status_of_current_accommodation: String?=null,
    val other_main_material_of_exterior_walls: String?=null,
    val other_main_soil_material: String?=null,
    val occupation_status_of_current_accommodation: Long?=null,
    val main_material_of_exterior_walls: Long?=null,
    val main_soil_material: Long?=null,
    val number_of_rooms_used_for_sleeping: Long?=null,
    val type_of_fuel_used_for_household_cooking: Long?=null,
    val other_type_of_fuel_used_for_household_cooking: String?=null,
    val main_source_of_household_drinking_water: Long?=null,
    val other_main_source_of_household_drinking_water: String?=null,
    val type_of_household_toilet: Long?=null,
    val other_type_of_household_toilet: String?=null,
    val method_of_waste_disposal: Long?=null,
    val place_to_wash_hands: Long?=null,
    val minimum_monthly_income_necessary_live_without_difficulties: Long?=null,
    val household_monthly_income: Long?=null,
    val number_of_goat_owned: Long?=null,
    val number_of_sheep_owned: Long?=null,
    val number_of_cow_owned: Long?=null,
    val number_of_rabbit_owned: Long?=null,
    val number_of_pigs_owned: Long?=null,
    val number_of_poultry_owned: Long?=null,
    val number_of_guinea_pig_owned: Long?=null,
    val number_of_cars_owned: Long?=null,
    val number_of_motorbike_owned: Long?=null,
    val number_of_bicycle_owned: Long?=null,
    val number_of_cart_owned: Long?=null,
    val number_of_plow_owned: Long?=null,
    val number_of_wheelbarrow_owned: Long?=null,
    val number_of_rickshaw_owned: Long?=null,
    val number_of_mattress_owned: Long?=null,
    val number_of_fridge_owned: Long?=null,
    val number_of_fan_owned: Long?=null,
    val number_of_air_conditioner_owned: Long?=null,
    val number_of_radio_owned: Long?=null,
    val number_of_television_owned: Long?=null,
    val number_of_canal_tnt_tv_cable_owned: Long?=null,
    val number_of_dvd_driver_owned: Long?=null,
    val number_of_handset_or_phone_owned: Long?=null,
    val number_of_computer_owned: Long?=null,
    val number_of_stove_or_oven_owned: Long?=null,
    val number_of_oil_stove_owned: Long?=null,
    val number_of_solar_plate_owned: Long?=null,
    val number_of_sewing_machine_owned: Long?=null,
    val number_of_electric_iron_owned: Long?=null,
    val number_of_charcoal_iron_owned: Long?=null,
    val number_of_bed_owned: Long?=null,
    val number_of_table_owned: Long?=null,
    val number_of_chair_owned: Long?=null,
    val number_of_sofa_owned: Long?=null,
    val number_of_mosquito_nets_owned: Long?=null,
    val amount_of_cultivable_land_owned: Long?=null,
    val number_of_meals_eaten_by_adults_18_plus_yesterday: Long?=null,
    val number_of_meals_eaten_by_children_6_to_17_yesterday: Long?=null,
    val number_of_meals_eaten_by_children_2_to_5_yesterday: Long?=null,
    val number_of_days_in_week_consumed_staple_foods: Long?=null,
    val number_of_days_in_week_consumed_legumes_or_nuts: Long?=null,
    val number_of_days_in_week_consumed_dairy_products: Long?=null,
    val number_of_days_in_week_consumed_meat: Long?=null,
    val number_of_days_in_week_consumed_vegetables: Long?=null,
    val number_of_days_in_week_consumed_fruits: Long?=null,
    val number_of_days_in_week_consumed_cooking_oils: Long?=null,
    val number_of_days_in_week_consumed_sugar_or_sweet_products: Long?=null,
    val days_spent_reduce_amount_consumed_coping_strategy: Long?=null,
    val days_spent_reduce_meals_consumed_coping_strategy: Long?=null,
    val days_spent_reduce_meals_adult_forfeit_meal_for_child_coping_strategy: Long?=null,
    val days_spent_eat_less_expensively_coping_strategy: Long?=null,
    val days_spent_borrow_food_or_rely_on_family_help_coping_strategy: Long?=null,
    val days_spent_days_without_eating_coping_strategy: Long?=null,
    val days_spent_begging_coping_strategy: Long?=null,
    val days_spent_consume_wild_food_coping_strategy: Long?=null,
    val other_household_activities_in_past_12_months: String?=null,
    val other_method_of_waste_disposal: String?=null,
    val other_livestock_owned: String?=null,
    val household_member_with_benefit_from_social_assistance_program: String?=null,
    val name_of_social_assistance_program: String?=null,
    val affected_by_other_shock: String?=null,

    )

*/