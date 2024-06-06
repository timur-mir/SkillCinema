package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Embedded
import androidx.room.Relation

data class CollectionsWithItemCollection(
    @Embedded val collections:Collections,
    @Relation(parentColumn = "collectionID", entityColumn = "parentCollectionID")
    val itemCollectionsList: List<ItemCollection>
)


