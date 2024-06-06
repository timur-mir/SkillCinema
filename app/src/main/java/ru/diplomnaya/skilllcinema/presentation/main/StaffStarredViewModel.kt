package ru.diplomnaya.skilllcinema.presentation.main


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.model.pagingSource.StaffStarredPagingSource

class StaffStarredViewModel : ViewModel() {
    var staffStarred: Flow<PagingData<StaffStarred>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { StaffStarredPagingSource() }
    ).flow.cachedIn(viewModelScope)
    override fun onCleared() {
        super.onCleared()
    }
    fun refreshFlow(){
       staffStarred = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { StaffStarredPagingSource() }
        ).flow.cachedIn(viewModelScope)
    }
}
