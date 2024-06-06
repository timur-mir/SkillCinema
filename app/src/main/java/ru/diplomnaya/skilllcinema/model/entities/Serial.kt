package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serial(
    var total: Int? = null,
    var items: ArrayList<Items> = arrayListOf()
) : Parcelable

@Parcelize
data class Items(
    var number: Int? = null,
    var episodes: ArrayList<Episodes> = arrayListOf()

) : Parcelable

@Parcelize
data class Episodes(
    var seasonNumber: Int? = null,
    var episodeNumber: Int? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var synopsis: String? = null,
    var releaseDate: String? = null

) : Parcelable
