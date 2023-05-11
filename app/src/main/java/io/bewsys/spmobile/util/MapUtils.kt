package io.bewsys.spmobile.util

import android.content.Context
import io.bewsys.spmobile.R


/*class Mapping(context: Context) {
    var mapping: Map<String, Any>? = null

    fun createMap(context: Context) {
        context.apply {
            mapping = mapOf(
                getString(R.string.male) to "M",
                getString(R.string.female) to "F",
                getString(R.string.urban) to 1,
                getString(R.string.rural) to 2,
                getString(R.string.urban_rural) to 3,
                getString(R.string.resident) to 3,
                getString(R.string.repatriated) to 3,
                getString(R.string.urban_rural) to 3,
                getString(R.string.urban_rural) to 3,


            )

        }
    }

}*/

object MapUtil {
    val mapping = mapOf<String, Any>(
        "Male" to "M",
        "Female" to "F",
        "Urban" to 1,
        "Rural" to 2,
        "Urban-rural" to 3,
        "Resident" to 1,
        "Repatriated" to 2,
        "Displaced" to 3,
        "Refugee" to 4,
        "Returned" to 5,

        "Head of household" to 1,
        "Wife or Husband" to 2,
        "Son/Daughter" to 3,
        "Son-in-law/Daughter-in-law" to 4,
        "Grandson/Granddaughter" to 5,
        "Father/Mother" to 6,
        "Parents-in-law" to 7,
        "Brother/Sister" to 8,
        "Nephew/Niece" to 9,
        "Nephew/Niece by marriage" to 10,
        "Other relative" to 11,
        "Adopted child/Cared for" to 12,
        "Unrelated" to 12,
        "Don't know" to 98,

        "Bachelor" to 1,
        "Married monogamously" to 2,
        "Married polygamous" to 3,
        "Widow /widower" to 4,
        "Divorce/separated" to 5,
        "Common-law union" to 6,

        "Yes" to 1,
        "No" to 2,
        "Don't know" to 3,

        "Pre-school" to 1,
        "Primary" to 2,
        "Secondary" to 3,
        "Superior" to 4,
        "Technical/Vocational training" to 5,
        "None" to 6,
        "Not concerned" to 7,
        "Don't know" to 8,

        "Pre-school" to 0,
        "1st year" to 1,
        "Grade 2" to 2,
        "Grade 3" to 3,
        "Grade 4" to 4,
        "Grade 5" to 5,
        "Grade 6" to 6,
        "Grade 7" to 7,
        "Grade 8" to 8,
        "Secondary" to 10,
        "Superior" to 11,
        "School dropout" to 12,
        "Not concerned" to 13,
        "Studies completed" to 97,
        "Don't know" to 98,


        "Motor" to 1,
        "Visual" to 2,
        "Auditory" to 3,
        "Mental" to 4,
        "Chronic disease" to 5,
        "Multiple disabilities" to 6,
        "None" to 8,

        "Senior Management/Executive" to 1,
        "Employee/Skilled or semi-skilled worker" to 2,
        "Manual worker/unskilled worker" to 3,
        "Employer" to 4,
        "Self-employed" to 5,
        "Family helper" to 6,
        "Paid or unpaid apprentice" to 7,
        "Unemployed" to 96,
        "Inactive" to 97,

        "Owner" to 1,
        "Housed by relative/friend" to 2,
        "Tenant" to 3,
        "Housed by employer" to 4,
        "Site guard" to 5,
        "Sub-housed" to 6,
        "Other" to 96,

        "No wall" to 1,
        "Bamboo/Cane/Palm/Trunk" to 2,
        "Earth" to 3,
        "Bamboo with mud" to 4,
        "Stones with mud" to 5,
        "Adobe not covered" to 6,
        "Plywood" to 7,
        "Cardboard" to 8,
        "Salvage timber" to 9,
        "Cement" to 10,
        "Stones with lime/cement timber" to 11,
        "Bricks" to 12,
        "Cement blocks" to 13,
        "Adobe covered" to 14,
        "Wooden board/Shingles" to 15,


        "Earth/Sand" to 1,
        "Dung" to 2,
        "Wooden boards" to 3,
        "Fins/bamboo" to 4,
        "Parquet or waxed wood" to 5,
        "Vynille/Asphalt strips" to 6,
        "Tiling" to 7,
        "Cement" to 8,
        "Carpet" to 9,


        "Electricity" to 1,
        "Liquefied Propane Gas (LPG)" to 2,
        "Natural gas" to 3,
        "Biogas" to 4,
        "Kerosene" to 5,
        "Coal/Ignite" to 6,
        "Charcoal" to 7,
        "Wood" to 8,
        "Straw/Branches/Grass" to 9,
        "Agricultural residues" to 10,
        "Dung" to 11,
        "No meal prepared in the household" to 12,















        )
}
