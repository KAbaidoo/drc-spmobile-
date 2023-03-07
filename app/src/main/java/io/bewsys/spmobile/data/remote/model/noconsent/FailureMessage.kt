package io.bewsys.spmobile.data.remote.model.noconsent

import kotlinx.serialization.Serializable

@Serializable
data class FailureMessage(
    val error: Error
)