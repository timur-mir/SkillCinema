package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alreadyviewed")
data class AlreadyViewedEntity(
    @PrimaryKey(autoGenerate = false)
    var kinopoiskId: Int,
    var imdbId:String?,
    val nameRu: String?,
    val posterUrlPreview: String,
    val genres:String,
    val premiereRu: String?,
    val countries: String,
    val ratingImdb: String?,
    var viewed: Boolean
)