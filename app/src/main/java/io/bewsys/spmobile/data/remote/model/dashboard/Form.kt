package io.bewsys.spmobile.data.remote.model.dashboard

import kotlinx.serialization.Serializable

@Serializable
data class Form(
    val active: String,
    val collective: String,
    val created_at: String,
    val description: String,
    val fields: String,
    val form_collective_forms: String,
    val id: Int,
    val is_cbt: String,
    val is_program_validation: String,
    val rules: String,
    val slug: String,
    val sub_forms: String,
    val table_name: String,
    val title: String,
    val updated_at: String,
    val workflow: String
)