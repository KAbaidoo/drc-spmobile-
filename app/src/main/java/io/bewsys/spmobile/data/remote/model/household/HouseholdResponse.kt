package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class HouseholdResponse(
    val household: HouseholdX?
)