package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class DashboardResponse(

    val territories: List<Territory>,
    val communities: List<Community>,
    val provinces: List<Province>,
    val groupments: List<Groupment>,
    val healthZones: List<HealthZone>,
    val healthAreas: List<HealthArea>,
    @Transient
    val cbt_area_assignments: List<CbtAreaAssignment>? = null,
    @Transient
    val forms: List<Form>? = null,
    @Transient
    val non_consent_households: List<NonConsentHousehold>? = null,
    @Transient
    val permissions: List<String>? = null,
    @Transient
    val targets: List<String>? = null

)

@Serializable
data class ProvinceResponse(
    val provinces: List<Province>
)

@Serializable
data class TerritoryResponse(
    val territories: List<Territory>
)

@Serializable
data class CommunitiesResponse(
    val communities: List<Community>,
)

@Serializable
data class GroupmentResponse(
    val groupments: List<Groupment>,
)

@Serializable
data class HealthZoneResponse(
    val healthZones: List<HealthZone>,
)

@Serializable
data class HealthAreaResponse(
    val healthAreas: List<HealthArea>,
)