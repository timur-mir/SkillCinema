package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface PremieresDao  {
    @Query("SELECT EXISTS (SELECT 1 FROM premieres WHERE kinopoiskId = :id)")
    suspend fun exists(id: Int): Boolean

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertPremieres(films:List<PremieresEntity>)


    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertPremiere(film:PremieresEntity)

//    @Query("SELECT*FROM premieres ")
//    fun getPremieres(): Flow<List<PremieresEntity>>
    @Query("SELECT*FROM premieres ")
    fun getPremieres():List<PremieresEntity>
    @Query("SELECT*FROM premieres  WHERE kinopoiskId=:kinopoiskId")
    fun getPremiereById(kinopoiskId:Int): Flow<PremieresEntity>

    @Update
    suspend fun updatePremiere(film: PremieresEntity)

    @Delete
    suspend fun removePremiere(film: PremieresEntity)

    @Query("DELETE FROM premieres   WHERE kinopoiskId=:kinopoiskId")
    suspend fun removePremiereById(kinopoiskId: Int)

    @Query("DELETE FROM premieres")
    suspend fun removeAllPremieres()
}