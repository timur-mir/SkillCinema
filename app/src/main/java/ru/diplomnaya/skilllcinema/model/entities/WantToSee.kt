package ru.diplomnaya.skilllcinema.model.entities

import androidx.room.PrimaryKey

data class WantToSee(
var kinopoiskId: Int,
val imdbId:String?,
val nameRu: String?,
val posterUrlPreview: String,
val genres: String,
val premiereRu: String?,
val countries: String,
val ratingImdb: String?,
val viewed: Boolean
)