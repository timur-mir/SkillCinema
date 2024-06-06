package ru.diplomnaya.skilllcinema.utilits

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.diplomnaya.skilllcinema.model.database.Country


class CountryConverters {

private val gson: Gson by lazy { Gson() }
    @TypeConverter
    fun toCountryList(jsonCountry: String?): ArrayList<Country>? {
        if(jsonCountry==null)
            return null
        val notesType = object : TypeToken<ArrayList<Country>>() {}.type
        return gson.fromJson(jsonCountry, notesType) as ArrayList<Country>
    }

    @TypeConverter
    fun fromCountryGson(countries: ArrayList<Country>?): String? {
        if (countries == null)
            return null
        val notesType = object : TypeToken<ArrayList<Country>>() {}.type
        return Gson().toJson(countries, notesType)

    }

}
