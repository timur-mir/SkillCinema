package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.reflect.TypeToken
import ru.diplomnaya.skilllcinema.utilits.CollectionConvertersX

@Entity
    (tableName ="itemsOfCollectionFilm",
    foreignKeys = [ForeignKey(
    entity = Collections::class,
    parentColumns = ["collectionID"],
    childColumns = ["parentCollectionID"]
)])
 data class ItemCollection (
    @PrimaryKey(autoGenerate = true)
    var itemID: Int,
    @TypeConverters(CollectionConvertersX::class)
    val collectionFilmProfile: CollectionFilm,
    var parentCollectionID: Int
    )