package io.bewsys.spmobile.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Community(
    val id: Long? = null,
    val name: String?,
    val territory_id: Long?,
    val household_count: Long?,
    val member_count: Long?
):Parcelable

