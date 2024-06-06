package ru.diplomnaya.skilllcinema.presentation.main

import android.util.Log

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.AlreadyViewedRepository
import ru.diplomnaya.skilllcinema.model.allRepository.PremieresRepository


class PremieresListViewModel private constructor(
    private val repository: PremieresRepository
) : ViewModel() {
    constructor() : this(PremieresRepository())
    private var downloader: Job?=null
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    val filterEnabled = MutableStateFlow(true)
    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies=_movies.asStateFlow()


//    val movies: StateFlow<List<Movie>> = combine(_movies, filterEnabled) { movies, filterEnabled ->
//        if (filterEnabled)
//            movies.filter { movie ->
//                movie.countries?.any { it.country == "Россия" } ?: false
//
//            }
//        else movies
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.Eagerly,
//        initialValue = _movies.value
//    )





    init {
        loadPremieres()
    }


    private fun loadPremieres() {
     downloader?:  viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                _isLoading.value = true
                repository.getPremieres(2024, "may")
            }.fold(
                onSuccess = { it ->
                  ///   _movies.value = it.filter { movie -> movie.countries?.any { it.country == "Россия" }?:false}
                    _movies.value = it
                },
                onFailure = { Log.d("PremieresListViewModel", it.message ?: "") }
            )
            _isLoading.value = false
         downloader?.cancel()
         downloader=null
        }
    }


    override fun onCleared() {
        super.onCleared()
        downloader=null
    }


    fun refresh() {
        loadPremieres()
    }

}