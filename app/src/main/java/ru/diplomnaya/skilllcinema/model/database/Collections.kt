package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collectionNameTable")
data class Collections (
    @PrimaryKey(autoGenerate = true)
    val collectionID: Int,
    val CollectionName: String
        )