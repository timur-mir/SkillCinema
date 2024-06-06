package ru.diplomnaya.skilllcinema.presentation.search


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.SearchFilmsRepository
import ru.diplomnaya.skilllcinema.model.entities.AllCountriesGenresID

class SearchFilmsViewModel(
    private val repository: SearchFilmsRepository
) : ViewModel() {

    constructor() : this(SearchFilmsRepository())

    private val responseMutableLiveData = MutableLiveData<List<Movie>>()
    val responsList: LiveData<List<Movie>>
        get() = responseMutableLiveData
    private val responseCountriesGenresIdMutableLiveData = MutableLiveData<AllCountriesGenresID>()
    val responseAllIdInfo: LiveData<AllCountriesGenresID>
        get() = responseCountriesGenresIdMutableLiveData
//    suspend fun searchFilms(
//        type: String, countries: Int, genre: Int, page: Int,sort_by:String, ratingFrom: Int, ratingTo: Int,
//        yearFrom: Int, yearTo: Int, keyword: String
//    ) {
//        responseMutableLiveData.postValue(
//            repository.searchFilms(
//                type, countries, genre, page,sort_by, ratingFrom, ratingTo,
//                yearFrom, yearTo, keyword
//            )
//        )
//    }

    suspend fun searchFilms(
        type: String, countries: Int, genre: Int, page: Int,sort_by:String, ratingFrom: Int, ratingTo: Int,
        yearFrom: Int, yearTo: Int, keyword: String
    ) {

        viewModelScope.launch {
            kotlin.runCatching {
                repository.searchFilms(
                    type, countries, genre, page,sort_by, ratingFrom, ratingTo,
                    yearFrom, yearTo, keyword
                )
            }.fold(
                onSuccess = { responseMutableLiveData.value = it },
                onFailure = { Log.d("SearchFilmslViewModel", it.message ?: "Ошибка подключения или проблемы на сервере") }
            )

        }
    }

    suspend fun getCountriesGenres(){
        responseCountriesGenresIdMutableLiveData.postValue(repository.getAllIdCountriesGenres())
    }


}
