package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "premieres")
data class PremieresEntity (
    @PrimaryKey(autoGenerate = false)
    val kinopoiskId: Int?,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val genres: String,
    val premiereRu: String?,
    val countries: String,
    val rating: String?,
    val viewed:Boolean,
    val filmId:Int?
        )