package io.bewsys.spmobile.data.local
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class ProvinceModel(
    val id: Long? = null,
    val name: String?,
    val capital: String?
):Parcelable

