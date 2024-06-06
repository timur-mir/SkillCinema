package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.diplomnaya.skilllcinema.utilits.CountryConverters
import ru.diplomnaya.skilllcinema.utilits.GenreConverters

@Entity(tableName = "wanttosee")
data class WantToSeeEntity(
    @PrimaryKey(autoGenerate = false)
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