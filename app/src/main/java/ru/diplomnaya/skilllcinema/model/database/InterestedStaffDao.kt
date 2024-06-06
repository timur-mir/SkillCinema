package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InterestedStaffDao {
    @Query("SELECT EXISTS (SELECT 1 FROM interestedStaffs WHERE staffId = :id)")
    suspend fun existsInterestedStaff(id: Int): Boolean

    @Insert(onConflict= OnConflictStrategy.REPLACE)
    suspend fun insertInterestedStaff(staff: InterestedStaffEntity)

    @Query("SELECT*FROM interestedStaffs ")
    fun getInterestedStaffsBase(): Flow<List<InterestedStaffEntity>>

    @Query("DELETE FROM interestedStaffs")
    suspend fun removeAllInterestedStaffs()
}