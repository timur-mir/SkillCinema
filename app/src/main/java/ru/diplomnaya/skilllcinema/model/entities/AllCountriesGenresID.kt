package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AllCountriesGenresID(
    var genres: ArrayList<Genres> = arrayListOf(),
    var countries: ArrayList<Countries> = arrayListOf()
) : Parcelable

@Parcelize
data class Countries(
    var id: Int? = null,
    var country: String? = null
) : Parcelable

@Parcelize
data class Genres(
    var id: Int? = null,
    var genre: String? = null
) : Parcelable