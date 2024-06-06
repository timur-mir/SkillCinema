package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow


@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewCollectionFilm(newCollection: Collections)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNewItems(newItems: ItemCollection)

    @Query("SELECT*FROM itemsOfCollectionFilm")
    fun getAllItemsOffCollections(): Flow<List<ItemCollection>>
    @Query("SELECT * from collectionNameTable where collectionId =:id")
    fun getCollectionsById(id: Int): Flow<CollectionsWithItemCollection>
    @Query("SELECT * from collectionNameTable where CollectionName =:name")
    fun getCollectionsByName(name: String): Flow<List<Collections>>
    @Query("SELECT EXISTS (SELECT * FROM itemsOfCollectionFilm WHERE parentCollectionID = :id)")
    suspend fun existsItem(id: Int): Boolean
    @Query("SELECT*FROM collectionNameTable")
    fun getCollections(): Flow<List<CollectionsWithItemCollection>>

    @Transaction
    @Query("DELETE FROM collectionNameTable WHERE collectionID=:id")
    suspend fun removeCollectionById(id: Int)
    @Query("DELETE FROM itemsOfCollectionFilm WHERE parentCollectionID=:id And collectionFilmProfile=:film")
    suspend fun removeItemsOfCollectionFilmById(id: Int,film:CollectionFilm)

    @Query("DELETE FROM itemsOfCollectionFilm WHERE parentCollectionID=:id" )
    suspend fun removeItemsOfCollectionFilmByParentId(id: Int)

    @Query("DELETE FROM itemsOfCollectionFilm WHERE collectionFilmProfile=:film")
    suspend fun removeFilm(film:CollectionFilm)

     @Query("SELECT COUNT(collectionID) FROM collectionNameTable")
    suspend fun getRowCount(): Int

}