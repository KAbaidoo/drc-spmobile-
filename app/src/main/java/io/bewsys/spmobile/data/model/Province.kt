package io.bewsys.spmobile.data.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Province(
    val id: Long? = null,
    val name: String?,
    val capital: String?
):Parcelable

