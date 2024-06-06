package ru.diplomnaya.skilllcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.pagingSource.TopAwaitPagingSource

class TopAwaitViewModel : ViewModel() {
    val pageMovies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { TopAwaitPagingSource() }
    ).flow.cachedIn(viewModelScope)
    override fun onCleared() {
        super.onCleared()
    }
}