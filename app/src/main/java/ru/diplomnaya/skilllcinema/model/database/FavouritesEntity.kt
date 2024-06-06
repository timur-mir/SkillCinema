package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

import ru.diplomnaya.skilllcinema.utilits.CountryConverters
import ru.diplomnaya.skilllcinema.utilits.GenreConverters


@Entity(tableName = "favourites")
data class FavouritesEntity(

    @PrimaryKey(autoGenerate = false)
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


data class Genre(
    val genre: String?
)


data class Country(
    val country: String?
)
