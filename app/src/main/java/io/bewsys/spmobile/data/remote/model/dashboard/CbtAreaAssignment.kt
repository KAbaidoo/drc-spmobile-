package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class CbtAreaAssignment(
    val assigned_to: String,
    val assigned_to_type: String,
    val cbt_form_id: String,
    val created_at: String,
    val grading_criteria: String,
    val id: Int,
    val status: String,
    val updated_at: String
)