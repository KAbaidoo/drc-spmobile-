package io.bewsys.spmobile.data.local
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class CommunityModel(
    val id: Long? = null,
    val name: String?,
    val territory_id: Long?,
    val household_count: Long?,
    val member_count: Long?
):Parcelable

