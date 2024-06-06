package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.diplomnaya.skilllcinema.model.database.CollectionsFilmsDatabase.Companion.DB_VERSION
import ru.diplomnaya.skilllcinema.utilits.CollectionConvertersX
import ru.diplomnaya.skilllcinema.utilits.CountryConverters
import ru.diplomnaya.skilllcinema.utilits.GenreConverters

@Database(
    entities = [FavouritesEntity::class, WantToSeeEntity::class, AlreadyViewedEntity::class,
        Collections::class, ItemCollection::class,PremieresEntity::class,InterestedFilmsEntity::class,InterestedStaffEntity::class],
    version = DB_VERSION
)
@TypeConverters(CollectionConvertersX::class,GenreConverters::class)
abstract class CollectionsFilmsDatabase : RoomDatabase() {

    abstract fun favouritesDao(): FavouritesFilmsDao
    abstract fun alreadyViewedDao(): AlreadyViewedDao
    abstract fun wantToSeeDao(): WantToSeeDao
    abstract fun collectionDao(): CollectionDao
    abstract fun premieresDao():PremieresDao
    abstract fun interestedFilmsDao():InterestedFilmsDao
    abstract fun interestedStaffDao():InterestedStaffDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "SkillcinemaFilmsDouble"
    }
}
