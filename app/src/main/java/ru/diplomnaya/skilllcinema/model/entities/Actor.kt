package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Actor(
    var personId: Int? = null,
    var webUrl: String? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var sex: String? = null,
    var posterUrl: String? = null,
    var growth: String? = null,
    var birthday: String? = null,
    var death: String? = null,
    var age: Int? = null,
    var birthplace: String? = null,
    var deathplace: String? = null,
    var hasAwards: Int? = null,
    var profession: String? = null,
    var facts: ArrayList<String> = arrayListOf(),
    var spouses: ArrayList<Spouses> = arrayListOf(),
    var films: ArrayList<Films> = arrayListOf()
) : Parcelable

@Parcelize
data class Spouses(
    var personId: Int? = null,
    var name: String? = null,
    var divorced: Boolean? = null,
    var divorcedReason: String? = null,
    var sex: String? = null,
    var children: Int? = null,
    var webUrl: String? = null,
    var relation: String? = null

) : Parcelable

@Parcelize
data class Films(
    var filmId: Int? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var rating: String? = null,
    var general: Boolean? = null,
    var description: String? = null,
    var professionKey: String? = null
) : Parcelable