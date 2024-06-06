package ru.diplomnaya.skilllcinema.model.database

data class CollectionFilm (
    val kinopoiskId: Int,
    val imdbId:String?,
    val nameRu: String?,
    val nameOriginal:String?,
    val year:Int,
    val posterUrlPreview: String,
//    val genres: ArrayList<Genre>?,
    val genres: String,
    val ratingImdb:String?,
    val filmLength:Int,
    val ratingAgeLimits:String?,
    val serial:Boolean,
//    val countries:ArrayList<Country>?,
    val countries:String,
    val description:String?,
    val type:String

)
//data class Country(
//    val country:String?
//)
//
//data class Genre(
//    val genre:String?
//)
//        )
