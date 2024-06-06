package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.model.network.retrofit

class ActorRepository {


    suspend fun getInfoAboutStaff(personId: Int): Actor {
        return retrofit.getInfoAboutStaff(personId)
    }
}