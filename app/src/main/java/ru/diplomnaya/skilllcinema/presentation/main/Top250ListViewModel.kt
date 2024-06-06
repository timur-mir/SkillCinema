package ru.diplomnaya.skilllcinema.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.pagingSource.Top250PagingSource

class Top250ListViewModel : ViewModel() {
    val page250Movies: Flow<PagingData<Movie>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = {
            Top250PagingSource()
        }
    ).flow.cachedIn(viewModelScope)
    override fun onCleared() {
        super.onCleared()
        }
}