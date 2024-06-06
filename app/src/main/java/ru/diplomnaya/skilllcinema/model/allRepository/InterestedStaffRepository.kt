package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity

class InterestedStaffRepository {
    private val interestedStaffDao = DataBase.INSTANCE!!.interestedStaffDao()

    suspend fun saveInterestedStaff(staff: InterestedStaffEntity)= interestedStaffDao.insertInterestedStaff(staff)

    fun getInterestedStaffs()=interestedStaffDao.getInterestedStaffsBase()

    suspend fun checkInterestedStaff(staff:Int):Boolean{
        return interestedStaffDao.existsInterestedStaff(staff)
    }

    suspend fun removeAllInterestedStaffs(){
        interestedStaffDao.removeAllInterestedStaffs()
    }
}