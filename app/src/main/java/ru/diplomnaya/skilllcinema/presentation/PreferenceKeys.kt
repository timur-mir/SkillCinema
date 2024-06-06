package ru.diplomnaya.skilllcinema.presentation

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val COUNTRY_NAME = stringPreferencesKey("country_name")
    val GENRE_TITLE = stringPreferencesKey("genre_title")
    val RATING_FROM = intPreferencesKey("rating_from")
    val RATING_TO = intPreferencesKey("rating_to")
    val FILM_TYPE = stringPreferencesKey("film_type")
    val SORT_BY = stringPreferencesKey("sort_by")
    val YEAR_FROM = intPreferencesKey("year_from")
    val YEAR_TO = intPreferencesKey("year_to")
    val HIDE_VIEWED_FILM= booleanPreferencesKey("hide_viewed_film")
    val HIDE_OR_SHOW_VIEWED_FILM_TITLE= stringPreferencesKey("hide_or_show_viewed_film_title")
    val NOT_LOADING_INTRO_ACTIVITY=booleanPreferencesKey("activity_once_loading")


}