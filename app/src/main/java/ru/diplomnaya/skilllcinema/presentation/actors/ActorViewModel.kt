package ru.diplomnaya.skilllcinema.presentation.actors

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.ActorRepository
import ru.diplomnaya.skilllcinema.model.entities.Actor


class ActorViewModel(
    private val repository: ActorRepository
) : ViewModel() {
    constructor() : this(ActorRepository())
    val initialValue: Actor? = null
    private val _actorFullInfo = MutableStateFlow(initialValue)
    val actorFullInfo= _actorFullInfo.asStateFlow()
    fun getActorFullInfo(personId:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getInfoAboutStaff(personId)
            }.fold(
                onSuccess = { _actorFullInfo.value = it },
                onFailure = { Log.d("ActorViewModel", it.message ?: "") }
            )

        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}