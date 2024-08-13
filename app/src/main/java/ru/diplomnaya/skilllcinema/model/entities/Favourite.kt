package ru.diplomnaya.skilllcinema.model.entities

data class Favourite(
    var kinopoiskId: Int,
    var imdbId:String?,
    val nameRu: String?,
    val posterUrlPreview: String,
    val genres: String,
    val premiereRu: String?,
    val countries: String,
    val ratingImdb: String?,
    val viewed: Boolean
)