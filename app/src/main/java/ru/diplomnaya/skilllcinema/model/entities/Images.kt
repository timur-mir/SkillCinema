package ru.diplomnaya.skilllcinema.model.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Images(
    var total: Int? = null,
    var totalPages: Int? = null,
    var items: ArrayList<ItemsImages> = arrayListOf()
): Parcelable
@Parcelize
data class ItemsImages(
    var imageUrl: String? = null,
    var previewUrl: String? = null
):Parcelable

