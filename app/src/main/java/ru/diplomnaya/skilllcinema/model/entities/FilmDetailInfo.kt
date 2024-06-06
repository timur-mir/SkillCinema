package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import java.util.ArrayList
@Parcelize
data class FilmDetailInfo(
    val kinopoiskId: Int,
    val imdbId:String,
    val nameRu: String,
    val nameOriginal:String?,
    val year:Int,
    val posterUrlPreview: String,
    val genres: ArrayList<Genre>?,
    val ratingImdb:String?,
    val filmLength:Int,
    val ratingAgeLimits:String,
    val serial:Boolean,
    val countries:ArrayList<Country>?,
    val description:String,
    val type:String

):Parcelable
//@Parcelize
//data class Country(
//    val country:String?
//):Parcelable
//
//@Parcelize
//data class Genre(
//    val genre:String?
//):Parcelable