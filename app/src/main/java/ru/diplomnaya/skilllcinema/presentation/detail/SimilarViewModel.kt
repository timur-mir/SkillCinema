package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.SimilarRepository
import ru.diplomnaya.skilllcinema.model.entities.Similar

class SimilarViewModel  private constructor(
    private val repository: SimilarRepository
) : ViewModel() {
    constructor() : this(SimilarRepository())
    val initialValue: Similar? = null
    private val _similar = MutableStateFlow(initialValue)
    val similar = _similar.asStateFlow()

    fun getSimilar(idFilm:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getSimilar(idFilm)
            }.fold(
                onSuccess = { _similar.value = it },
                onFailure = { Log.d("SimilarViewModel", it.message ?: "") }
            )

        }
    }

}