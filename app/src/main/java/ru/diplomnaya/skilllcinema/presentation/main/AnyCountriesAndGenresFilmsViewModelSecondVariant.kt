package ru.diplomnaya.skilllcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.pagingSource.AnyCountriesAndGenresFilmsPagingSourceSecondVariant
import ru.diplomnaya.skilllcinema.model.Movie

class AnyCountriesAndGenresFilmsViewModelSecondVariant : ViewModel() {
    val randomFilmsWithRandomGenresSecondVariant: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { AnyCountriesAndGenresFilmsPagingSourceSecondVariant() }
    ).flow.cachedIn(viewModelScope)
}