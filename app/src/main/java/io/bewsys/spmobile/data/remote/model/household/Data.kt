package io.bewsys.spmobile.data.remote.model.household

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val households: List<Household>?,
    val members: List<Member>?
)