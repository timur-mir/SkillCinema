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
import ru.diplomnaya.skilllcinema.model.allRepository.FavouritesFilmsRepository
import ru.diplomnaya.skilllcinema.model.database.FavouritesEntity

class CollectionsFavouritesFilmsViewModel private constructor(
    private val repository: FavouritesFilmsRepository
) : ViewModel() {

    constructor() : this(FavouritesFilmsRepository())

    private val exist = MutableLiveData<Boolean>()
    val existLive: LiveData<Boolean>
        get() = exist
    private val favouritesFilms: MutableLiveData<List<FavouritesEntity>> = MutableLiveData()
    val favouritesFilmsLiveData:LiveData<List<FavouritesEntity>>
        get() = favouritesFilms
    override fun onCleared() {
        super.onCleared()
    }
    fun addFavourite(film: FavouritesEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveFavourite(film)
        }
    }

    suspend fun checkFilm(id: Int) {
        try {
            exist.postValue(repository.checkFilm(id))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    suspend fun removeFilmById(id: Int) {
        viewModelScope.launch {
            try {
                repository.removeFilmById(id)
            } catch (t: Throwable) {

            }

        }
    }

    fun getAllFavouritesFilms(){
        viewModelScope.launch {
            repository.getFavouritesFilms()
                .catch { e->
                    Log.d("error","${e.message}")
                }
                .collect { response ->
                    favouritesFilms.value=response
                }
        }
    }
    fun getFavouritesFilmsById(id: Int): Flow<FavouritesEntity> =
        repository.getFavouritesFilmsById(id)
    suspend fun getCollectionsSize():Int =
       repository.getCollectionsSize()

}
