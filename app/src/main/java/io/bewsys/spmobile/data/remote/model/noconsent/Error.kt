package io.bewsys.spmobile.data.remote.model.noconsent

import kotlinx.serialization.Serializable

@Serializable
data class Error(
    val groupement_id: List<String>,
    val territory_id: List<String>
)