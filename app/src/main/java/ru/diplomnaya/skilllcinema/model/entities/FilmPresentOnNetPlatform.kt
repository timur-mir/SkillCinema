package ru.diplomnaya.skilllcinema.model.entities

data class FilmPresentOnNetPlatform(
    var total: Int? = null,
    var items: ArrayList<FilmNetSource> = arrayListOf()
)

data class FilmNetSource(
    var url: String? = null,
    var name: String? = null,
    var site: String? = null
)
