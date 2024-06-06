package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.AlreadyViewedRepository
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity

class CollectionsAlreadyViewedViewModel private constructor(
    private val repository: AlreadyViewedRepository
) : ViewModel() {

    constructor() : this(AlreadyViewedRepository())
    private val exist = MutableLiveData<Boolean>()
    val existLive: LiveData<Boolean>
        get() = exist
    private val viewedFilms: MutableLiveData<List<AlreadyViewedEntity>> = MutableLiveData()
    val viewedFilmsLiveData:LiveData<List<AlreadyViewedEntity>>
        get() = viewedFilms
    override fun onCleared() {
        super.onCleared()
       }

    fun addAlreadyViewed(film: AlreadyViewedEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveAlreadyViewed(film)
        }
    }

    suspend fun checkFilm(id: Int){
        try {
            exist.postValue(repository.checkFilm(id))
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }


    suspend fun removeFilmById(id:Int) {
        viewModelScope.launch {
            try {
                repository.removeFilmById(id)
            } catch (t: Throwable) {

            }

        }
    }
    suspend fun removeAllViewedFilms(){
        viewModelScope.launch {
            try {
                repository.removeAllViewedFilms()
            } catch (t: Throwable) {

            }

        }
    }

    fun getAllAlreadyViewedFilms() {
        viewModelScope.launch {
            repository.getAllAlreadyViewedFilms()
                .catch { e->
                    Log.d("error","${e.message}")
                }
                .collect { response ->
                    viewedFilms.value=response
                }
        }
    }
    fun getAlreadyViewedById(id: Int): Flow<AlreadyViewedEntity> =
        repository.getAlreadyViewedFilmsById(id)
}

