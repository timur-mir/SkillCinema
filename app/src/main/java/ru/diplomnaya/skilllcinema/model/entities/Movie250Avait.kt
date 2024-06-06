package ru.diplomnaya.skilllcinema.model.entities

import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import java.util.ArrayList

data class Movie250Avait(
    val filmId: Int,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val genres: ArrayList<Genre>?,
    val countries: ArrayList<Country>?,
    val rating: String?,
    val viewed:Boolean)
