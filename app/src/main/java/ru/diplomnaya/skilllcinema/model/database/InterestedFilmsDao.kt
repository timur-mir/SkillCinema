package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface  InterestedFilmsDao {
    @Query("SELECT EXISTS (SELECT 1 FROM interestedFilms WHERE kinopoiskId = :id)")
    suspend fun existsInterestedFilm(id: Int): Boolean

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertInterestedFilm(film: InterestedFilmsEntity)

    @Query("SELECT*FROM interestedFilms ")
    fun getInterestedFilmsBase(): Flow<List<InterestedFilmsEntity>>

    @Query("DELETE FROM interestedFilms")
    suspend fun removeAllInterestedFilms()
}