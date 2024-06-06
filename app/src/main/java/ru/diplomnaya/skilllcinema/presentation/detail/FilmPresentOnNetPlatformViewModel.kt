package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.FilmPresentOnNetPlatformRepository
import ru.diplomnaya.skilllcinema.model.allRepository.SimilarRepository
import ru.diplomnaya.skilllcinema.model.entities.FilmPresentOnNetPlatform
import ru.diplomnaya.skilllcinema.model.entities.Similar

class FilmPresentOnNetPlatformViewModel private constructor(
    private val repository: FilmPresentOnNetPlatformRepository
) : ViewModel() {
    constructor() : this(FilmPresentOnNetPlatformRepository())
    val initialValue: FilmPresentOnNetPlatform? = null
    private val _filmUrlNetPlatform = MutableStateFlow(initialValue)
    val filmUrlNetPlatform = _filmUrlNetPlatform.asStateFlow()

    fun getMoreUrlFilmOnNet(idFilm:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getMoreUrlFilmOnNet(idFilm)
            }.fold(
                onSuccess = { _filmUrlNetPlatform .value = it
                 //   Log.d("FilmUrlNetPlatform2","${it.items[0].site.toString()} ${it.items[0].url.toString()} ${it.items[0].name.toString()}"
                //    )
                            },
                onFailure = { Log.d("FilmUrlNetPlatform", it.message ?: "") }
            )

        }
    }

}