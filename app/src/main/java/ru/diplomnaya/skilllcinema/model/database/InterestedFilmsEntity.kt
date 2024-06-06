package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interestedFilms")
data class InterestedFilmsEntity(
    @PrimaryKey(autoGenerate = false)
    val kinopoiskId: Int?,
    val filmId:Int?,
    val nameRu: String,
    val rating:String?,
    val posterUrl: String?,
    val genres:String?
    )