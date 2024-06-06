package ru.diplomnaya.skilllcinema.presentation.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.InterestedFilmsRepository
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity

class InterestedFilmsViewModel private constructor(
    private val repository: InterestedFilmsRepository
) : ViewModel() {
    constructor() : this(InterestedFilmsRepository())
    private val existInterestedFilm = MutableLiveData<Boolean>()
    val existInterestedFilmLiveData: LiveData<Boolean>
        get() = existInterestedFilm
    private val interestedFilms: MutableLiveData<List<InterestedFilmsEntity>> = MutableLiveData()
    val interestedFilmsLiveData: LiveData<List<InterestedFilmsEntity>>
        get() = interestedFilms
    fun saveInterestedFilm(film:InterestedFilmsEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.saveInterestedFilm(film)
        }
    }
    suspend fun checkInterestedFilm(id: Int){
        try {
            existInterestedFilm.postValue(repository.checkInterestedFim(id))
        } catch (e:Exception) {
            e.printStackTrace()
        }
    }
    fun getInterestedFilms() {
        viewModelScope.launch {
            repository.getInterestedFilms()
                .catch { e->
                    Log.d("error","${e.message}")
                }
                .collect { response ->
                    interestedFilms.value=response
                }
        }
    }
    suspend fun removeAllInterestedFilms(){
        viewModelScope.launch {
            try {
                repository.removeAllInterestedFilms()
            } catch (t: Throwable) {

            }

        }
    }

}