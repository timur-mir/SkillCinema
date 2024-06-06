package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface WantToSeeDao {
    @Query("SELECT EXISTS (SELECT 1 FROM wanttosee WHERE kinopoiskId = :id)")
    suspend fun exists(id: Int): Boolean

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertFilm(film: WantToSeeEntity)

    @Query("SELECT*FROM wanttosee")
    fun getAlWantToSeeFilmsBase(): Flow<List<WantToSeeEntity>>


    @Query("SELECT*FROM wanttosee WHERE kinopoiskId=:kinopoiskId")
    fun getWantToSeeFilmById(kinopoiskId:Int): Flow<WantToSeeEntity>


    @Update
    suspend fun updateWantToSee(film: WantToSeeEntity)

    @Delete
    suspend fun removeWantToSeeFilm(film: WantToSeeEntity)

    @Query("DELETE FROM wanttosee  WHERE kinopoiskId=:kinopoiskId")
    suspend fun removeWantToSeeFilmById(kinopoiskId: Int)
    @Query("SELECT COUNT(kinopoiskId) FROM wanttosee")
    suspend fun getRowCount(): Int
}