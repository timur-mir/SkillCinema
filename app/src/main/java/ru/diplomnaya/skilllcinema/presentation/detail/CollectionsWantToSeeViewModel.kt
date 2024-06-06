package ru.diplomnaya.skilllcinema.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.WantToSeeRepository
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity

class CollectionsWantToSeeViewModel private constructor(
    private val repository: WantToSeeRepository
) : ViewModel() {

    constructor() : this(WantToSeeRepository())
    private val exist = MutableLiveData<Boolean>()

    val existLive: LiveData<Boolean>
        get() = exist
    override fun onCleared() {
        super.onCleared()
    }
    fun addWantToSee(film: WantToSeeEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveWantToSee(film)
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

    fun getAllWantToSee(): Flow<List<WantToSeeEntity>> = repository.getWantToSeeFilms()
    fun getWantToSeeById(id: Int): Flow<WantToSeeEntity> =
        repository.getWantToSeeFilmsById(id)
    suspend fun getCollectionsSize():Int =
        repository.getCollectionsSize()
}
