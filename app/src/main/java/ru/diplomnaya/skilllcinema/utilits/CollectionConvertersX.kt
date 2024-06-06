package ru.diplomnaya.skilllcinema.utilits

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
class CollectionConvertersX {
    @TypeConverter
    fun fromValuesToList(value: CollectionFilm): String? {
        if (value == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<CollectionFilm>() {}.type
        return gson.toJson(value, type);
    }

    @TypeConverter
    fun toOptionValuesObject(value: String): CollectionFilm? {
        if (value == null) {
            return (null);
        }
        val gson = Gson()
        val type = object : TypeToken<CollectionFilm>() {}.type
        return gson.fromJson(value, type);
    }
}