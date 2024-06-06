package ru.diplomnaya.skilllcinema.model

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList


data class Movie(
    val kinopoiskId: Int,
    var nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val genres: ArrayList<Genre>?,
    val premiereRu: String?,
    val countries: ArrayList<Country>?,
    val rating: String?,
    var viewed: Boolean =false,
    val filmId:Int

):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(Genre),
        parcel.readString(),
        parcel.createTypedArrayList(Country),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
                parcel.readInt()

    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(kinopoiskId)
        parcel.writeString(nameRu)
        parcel.writeString(posterUrl)
        parcel.writeString(posterUrlPreview)
        parcel.writeTypedList(genres)
        parcel.writeString(premiereRu)
        parcel.writeTypedList(countries)
        parcel.writeString(rating)
        parcel.writeByte(if (viewed) 1 else 0)
        parcel.writeInt(filmId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}


data class Genre(
    val genre: String?
):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(genre)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Genre> {
        override fun createFromParcel(parcel: Parcel): Genre {
            return Genre(parcel)
        }

        override fun newArray(size: Int): Array<Genre?> {
            return arrayOfNulls(size)
        }
    }
}


data class Country(
    val country: String?
):Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Country> {
        override fun createFromParcel(parcel: Parcel): Country {
            return Country(parcel)
        }

        override fun newArray(size: Int): Array<Country?> {
            return arrayOfNulls(size)
        }
    }
}
