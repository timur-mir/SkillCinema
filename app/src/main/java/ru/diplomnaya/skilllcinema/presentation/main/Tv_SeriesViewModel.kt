package ru.diplomnaya.skilllcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.pagingSource.AnyCountriesAndGenresTv_SeriesPagingSource
import ru.diplomnaya.skilllcinema.model.Movie


class Tv_SeriesViewModel : ViewModel() {
    var tv_Series: Flow<PagingData<Movie>>? = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { AnyCountriesAndGenresTv_SeriesPagingSource() }
    ).flow.cachedIn(viewModelScope)
    override fun onCleared() {
        super.onCleared()
    }
    fun refresh(){
        AnyCountriesAndGenresTv_SeriesPagingSource.randFilmAlter.refreshSeriesQuery()
        set()
        val refResourse= AnyCountriesAndGenresTv_SeriesPagingSource()
        tv_Series = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {refResourse }
        ).flow.cachedIn(viewModelScope)
    }
    fun set(){
        tv_Series=null
    }
}