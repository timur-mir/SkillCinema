package ru.diplomnaya.skilllcinema.model.allRepository

import ru.diplomnaya.skilllcinema.model.entities.Images
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.model.network.retrofit

class ImagesRepository {
    suspend fun getImages(idFilm:Int,type:String,page:Int): Images {
        return retrofit.getImageAboutFilm(idFilm,type,page)
    }
}