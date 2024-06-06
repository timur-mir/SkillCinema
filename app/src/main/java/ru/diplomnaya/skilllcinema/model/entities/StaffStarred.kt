                    package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StaffStarred(
    val staffId: Int,
    val nameRu: String,
    val nameEn: String,
    val description: String?,
    val posterUrl: String,
    val professionText: String,
    val professionKey: String
): Parcelable