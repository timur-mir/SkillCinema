package ru.diplomnaya.skilllcinema.utilits

import ru.diplomnaya.skilllcinema.model.database.FavouritesEntity
import ru.diplomnaya.skilllcinema.model.entities.Favourite

fun FavouritesEntity.toFavourite() =
    Favourite(
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