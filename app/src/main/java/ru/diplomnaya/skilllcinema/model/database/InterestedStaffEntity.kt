package ru.diplomnaya.skilllcinema.model.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interestedStaffs")
data class InterestedStaffEntity(
    @PrimaryKey(autoGenerate = false)
    val staffId: Int?,
    val nameRu: String?,
    val posterUrl: String?,
    val professionText: String?,
    val professionKey: String?

)