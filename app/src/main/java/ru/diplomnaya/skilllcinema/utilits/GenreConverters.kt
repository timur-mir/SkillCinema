package ru.diplomnaya.skilllcinema.utilits

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.diplomnaya.skilllcinema.model.database.Genre


class GenreConverters {
    private val gson: Gson by lazy { Gson() }
    @TypeConverter
    fun toGenreList(jsonGenre: String): List<Genre>? {
        if(jsonGenre==null)
            return null
        val notesType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(jsonGenre, notesType) as List<Genre>
    }
    @TypeConverter
    fun fromGenreGson(genres: List<Genre>?): String? {
        if (genres == null)
            return null
        val notesType = object : TypeToken<List<Genre>>() {}.type
        return gson.toJson(genres, notesType)


    }

}