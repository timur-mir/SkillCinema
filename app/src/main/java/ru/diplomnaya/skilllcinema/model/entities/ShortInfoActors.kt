package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShortInfoActors(
    val actorName:String,
    val genre:String?,
    val urlFilmPic:String?,
    var filmId: Int? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var rating: String? = null,
    val sex:String?=null,
    var professionText: String?,
    var professionKey: String? = null
):Parcelable
