package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface AlreadyViewedDao {
    @Query("SELECT EXISTS (SELECT 1 FROM alreadyviewed WHERE kinopoiskId = :id)")
    suspend fun exists(id: Int): Boolean

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: AlreadyViewedEntity)

    @Query("SELECT*FROM alreadyviewed ")
    fun getAllAlreadyViewedFilmsBase(): Flow<List<AlreadyViewedEntity>>


    @Query("SELECT*FROM alreadyviewed  WHERE kinopoiskId=:kinopoiskId")
    fun getAlreadyViewedFilmById(kinopoiskId:Int): Flow<AlreadyViewedEntity>

    @Update
    suspend fun updateAlreadyViewedFilm(film: AlreadyViewedEntity)

    @Delete
    suspend fun removeAlreadyViewedFilm(film: AlreadyViewedEntity)

    @Query("DELETE FROM alreadyviewed   WHERE kinopoiskId=:kinopoiskId")
    suspend fun removeAlreadyViewedFilmById(kinopoiskId: Int)
    @Query("DELETE FROM alreadyviewed")
    suspend fun removeAllViewedFilms()
}