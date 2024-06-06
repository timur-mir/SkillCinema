package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritesFilmsDao {
    @Query("SELECT EXISTS (SELECT 1 FROM favourites WHERE kinopoiskId = :id)")
    suspend fun exists(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: FavouritesEntity)

    @Query("SELECT*FROM favourites")
    fun getAlFavouritesFilmsBase(): Flow<List<FavouritesEntity>>


    @Query("SELECT*FROM favourites WHERE kinopoiskId=:kinopoiskId")
    fun getFavouriteFilmById(kinopoiskId: Int): Flow<FavouritesEntity>


    @Update
    suspend fun updateFavouriteFilm(film: FavouritesEntity)

    @Delete
    suspend fun removeFavouriteFilm(film: FavouritesEntity)

    @Query("DELETE FROM favourites  WHERE kinopoiskId=:kinopoiskId")
    suspend fun removeFavouriteFilmById(kinopoiskId: Int)

    @Query("SELECT COUNT(kinopoiskId) FROM favourites")
    suspend fun getRowCount(): Int

}