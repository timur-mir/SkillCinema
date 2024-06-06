package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.model.network.retrofit

class StaffStarredRepository {
     suspend fun getStarredStaff(filmId: Int): List<StaffStarred> {
        return retrofit.getStaffStarred(filmId)
    }
}