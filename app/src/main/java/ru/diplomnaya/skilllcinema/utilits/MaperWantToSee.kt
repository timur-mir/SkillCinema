package ru.diplomnaya.skilllcinema.utilits

import ru.diplomnaya.skilllcinema.model.database.FavouritesEntity
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity
import ru.diplomnaya.skilllcinema.model.entities.Favourite
import ru.diplomnaya.skilllcinema.model.entities.WantToSee

fun WantToSeeEntity.toWantToSee() =
    WantToSee(
        kinopoiskId,
        imdbId,
        nameRu,
        posterUrlPreview,
        genres,
        premiereRu,
        countries,
        ratingImdb,
        viewed
    )