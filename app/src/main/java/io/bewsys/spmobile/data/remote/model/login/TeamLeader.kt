package io.bewsys.spmobile.data.remote.model.login

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable




@Serializable
data class TeamLeader(
    @SerialName("id")
    val id: String,
    @SerialName("supervisor")
    val supervisor: String,
    @SerialName("name")
    val name: String

    )