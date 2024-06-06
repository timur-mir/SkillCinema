package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.allRepository.FilmDetailInfoRepository
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.network.retrofit


class GetFilmDetailInfoViewModel private constructor(
  private val repository: FilmDetailInfoRepository
) : ViewModel() {
    constructor() : this(FilmDetailInfoRepository())
    private val filmInfoForActors: MutableLiveData<FilmDetailInfo> = MutableLiveData()
    val filmInfoForActorsLiveData: LiveData<FilmDetailInfo>
        get() = filmInfoForActors

    private val _movies = MutableStateFlow<FilmDetailInfo>(
        FilmDetailInfo(
            462654,
            "tt1226837",
            "Красивый мальчик",
            "Beautiful Boy",
            2018,
            "https://kinopoiskapiunofficial.tech/images/posters/kp/462654.jpg",
            arrayListOf(Genre("драма"), Genre("биография")),
            "7.3",
            120,
            "18+",
            false,
            arrayListOf(Country("Сша")),
            " Дэвид Шефф переживает трагедию: его милый и очаровательный сын Ник стал" +
                    "наркоманом. Откуда взялась пагубная привычка? Ник растёт в любящей семье, он" +
                    " отлично учится, ни в чём не нуждается. Развод родителей прошёл спокойно." +
                    "С матерью, живущей в Лос-Анджелесе Ник общается до сих пор." +
                    " Пытаясь найти ответы, Дэвид вспоминает, каким ребёнок был раньше — вдумчивым" +
                    " и красивым мальчиком."
            ,
            "FILM"
        )
    )
    val movies = _movies.asStateFlow()


    fun getFilmDetailInfo(filmId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
              kotlin.runCatching {
          retrofit.getFilmById(filmId)

            }.fold(
                onSuccess = { _movies.value = it },
                onFailure = { Log.d("GetFilmDetailInfoViewModel", it.message ?: "") }
            )

            }
        }
    fun getFilmDetailInfoForActors(filmId: Int){
        viewModelScope.launch {
            try {
                filmInfoForActors.postValue(retrofit.getFilmById(filmId))
            } catch (t: Throwable) {

            }

        }
        }

}