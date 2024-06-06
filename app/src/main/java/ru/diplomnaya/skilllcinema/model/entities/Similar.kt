package ru.diplomnaya.skilllcinema.model.entities

data class Similar(
    var total: Int? = null,
    var items: ArrayList<ItemsSimilar> = arrayListOf()
)

data class ItemsSimilar(
    var filmId: Int? = null,
    var nameRu: String? = null,
    var nameEn: String? = null,
    var nameOriginal: String? = null,
    var posterUrl: String? = null,
    var posterUrlPreview: String? = null,
    var relationType: String? = null
)
