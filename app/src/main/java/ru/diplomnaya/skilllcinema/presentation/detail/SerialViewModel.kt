package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.SerialRepository
import ru.diplomnaya.skilllcinema.model.entities.Serial


class SerialViewModel private constructor(
    private val repository: SerialRepository
) : ViewModel() {
    constructor() : this(SerialRepository())
   val initialValue: Serial? = null
    private val _serial = MutableStateFlow(initialValue)
    val serial = _serial.asStateFlow()

    fun getSerial(idFilm:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getSerial(idFilm)
            }.fold(
                onSuccess = { _serial.value = it },
                onFailure = { Log.d("SerialViewModel", it.message ?: "") }
            )

            }
        }

    }
