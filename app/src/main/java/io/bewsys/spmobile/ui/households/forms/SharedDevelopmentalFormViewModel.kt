package io.bewsys.spmobile.ui.households.forms

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.bewsys.spmobile.data.Household
import io.bewsys.spmobile.data.model.HouseholdModel
import io.bewsys.spmobile.ui.nonconsenting.form.AddNonConsentingHouseholdViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.DateFormat


class SharedDevelopmentalFormViewModel : ViewModel() {

//    private val _entries = MutableLiveData<Map<String,String>>()
//    val entries: LiveData<Map<String,String>> = _entries

    private val AddDevelopmentalHouseholdEventChannel = Channel<AddDevelopmentalHouseholdEvent>()
    val addDevelopmentalHouseholdEvent = AddDevelopmentalHouseholdEventChannel.receiveAsFlow()


    private val _entriesMap = mutableMapOf<String, String>()

    fun saveEntry(title: String, answer: String) {
            val key = title.lowercase().split(' ').joinToString("_")
        _entriesMap[key] = answer
//            printResults(key, answer)
    }
    fun getEntry(title: String): String {
        val key = title.lowercase().split(' ').joinToString("_")
        return _entriesMap[key]?:""
    }

    fun clearEntries(){
        _entriesMap.clear()
    }

    fun printResults(title: String, str: String) {
        viewModelScope.launch {
            Log.d("FORM_VIEW_MODEL", "$title: $str")
        }
    }

    fun onRegisterClicked(){

        HouseholdModel(
            survey_date = DateFormat.getDateInstance().format(System.currentTimeMillis()),
            respondent_firstname = _entriesMap["respondent_firstname"],
            respondent_middlename = _entriesMap["respondent_middlename"],
            respondent_lastname = _entriesMap["respondent_lastname"],
            respondent_dob = _entriesMap["respondent_dob"],
            respondent_family_bond_to_head = if (_entriesMap["respondent_family_bond_to_head"] == "yes") 1 else 0,
            respondent_voter_id =  _entriesMap["respondent_voter_id"],
            respondent_phone_number =  _entriesMap["respondent_phone_number"],
            household_head_firstname =  _entriesMap["household_head_firstname"],
            household_head_lastname =  _entriesMap["household_head_lastname"],
            household_head_middlename =  _entriesMap["household_head_middlename"],
            household_head_dob =  _entriesMap["household_head_dob"],
            household_head_sex =  _entriesMap["household_head_sex"],
            head_age_known =  _entriesMap["head_age_known"],
            head_dob_known =  _entriesMap["head_dob_known"],
            household_head_voter_id_card =  _entriesMap["household_head_voter_id_card"],
            household_head_phone_number =  _entriesMap["household_head_phone_number"],
            household_head_age =  _entriesMap["household_head_age"],
            household_head_marital_status_id =  _entriesMap["household_head_marital_status_id"],
            household_head_birth_certificate =  _entriesMap["household_head_birth_certificate"],
            household_head_educational_level_id =  _entriesMap["household_head_educational_level_id"],
            household_head_disability_id =  _entriesMap["household_head_disability_id"],
            household_head_socio_professional_category_id =  _entriesMap["household_head_socio_professional_category_id"],
            household_head_school_attendance_id =  _entriesMap["household_head_school_attendance_id"],
            household_head_sector_of_work_id =  _entriesMap["household_head_sector_of_work_id"],
            household_head_pregnancy_status =  _entriesMap["household_head_pregnancy_status"],
            unit_of_migration_duration =  _entriesMap["unit_of_migration_duration"],
            territory_or_town =  _entriesMap["territory_or_town"],
            bank_account_or_bank_card_available =  _entriesMap["bank_account_or_bank_card_available"],
            is_income_regular =  _entriesMap["is_income_regular"],
            household_member_access_to_cultivable_land =  _entriesMap["household_member_access_to_cultivable_land"],
            household_member_owner_of_cultivable_land =  _entriesMap["household_member_owner_of_cultivable_land"],
            practice_of_cash_crop_farming_or_commercial_farming =  _entriesMap["practice_of_cash_crop_farming_or_commercial_farming"],
            has_livestock =  _entriesMap["has_livestock"],
            has_household_goods =  _entriesMap["has_household_goods"],
            affected_by_conflict =  _entriesMap["affected_by_conflict"],
            affected_by_epidemic =  _entriesMap["affected_by_epidemic"],
            affected_by_climate_shock =  _entriesMap["affected_by_climate_shock"],
            take_children_out_of_school =  _entriesMap["take_children_out_of_school"],
            sale_of_production_assets =  _entriesMap["sale_of_production_assets"],
            use_of_child_labor =  _entriesMap["use_of_child_labor"],
            use_of_early_marriage =  _entriesMap["use_of_early_marriage"],
            give_up_health_care =  _entriesMap["give_up_health_care"],
            days_spent_other_coping_strategy =  _entriesMap["days_spent_other_coping_strategy"],
            gps_longitude =  _entriesMap["gps_longitude"],
            gps_latitude =  _entriesMap["gps_latitude"],
            start_time =  _entriesMap["start_time"],
            finish_time =  _entriesMap["finish_time"],
            address =  _entriesMap["address"],
            comments =  _entriesMap["comments"],
            profile_picture =  _entriesMap["profile_picture"],
            mobile_money_username =  _entriesMap["mobile_money_username"],
            mobile_money_phone_number =  _entriesMap["mobile_money_phone_number"],
            village_or_quartier =  _entriesMap["village_or_quartier"],
            other_household_migration_status =  _entriesMap["other_household_migration_status"],
            other_occupation_status_of_current_accommodation =  _entriesMap["other_occupation_status_of_current_accommodation"],
            other_main_material_of_exterior_walls =  _entriesMap["other_main_material_of_exterior_walls"],
            other_main_soil_material =  _entriesMap["other_main_soil_material"],
            other_type_of_fuel_used_for_household_cooking =  _entriesMap["other_type_of_fuel_used_for_household_cooking"],
            other_main_source_of_household_drinking_water =  _entriesMap["other_main_source_of_household_drinking_water"],
            other_type_of_household_toilet =  _entriesMap["other_type_of_household_toilet"],
            other_method_of_waste_disposal =  _entriesMap["other_method_of_waste_disposal"],
            other_livestock_owned =  _entriesMap["other_livestock_owned"],
            household_member_with_benefit_from_social_assistance_program =  _entriesMap["household_member_with_benefit_from_social_assistance_program"],
            name_of_social_assistance_program =  _entriesMap["name_of_social_assistance_program"],
            affected_by_other_shock =  _entriesMap["affected_by_other_shock"],
            other_household_activities_in_past_12_months =  _entriesMap["other_household_activities_in_past_12_months"],
            duration_displaced_returned_repatriated_refugee = _entriesMap["duration_displaced_returned_repatriated_refugee"]?.toLong(),
            number_of_months_displaced_returned_repatriated_refugee = _entriesMap["number_of_months_displaced_returned_repatriated_refugee"]?.toLong(),
//            number_of_months_displaced_returned_repatriated_refugee = _entriesMap["number_of_months_displaced_returned_repatriated_refugee"]?.toLong(),


        ).also {
            addHousehold(it)
        }

    }

    private fun addHousehold(household: HouseholdModel) = viewModelScope.launch {

    }

    sealed class AddDevelopmentalHouseholdEvent{
        data class ShowInvalidInputMessage(val msg: String) : AddDevelopmentalHouseholdEvent()
        data class NavigateBackWithResults(val results: Int) : AddDevelopmentalHouseholdEvent()
        data class NavigateBack(val results: Int) : AddDevelopmentalHouseholdEvent()
    }
}