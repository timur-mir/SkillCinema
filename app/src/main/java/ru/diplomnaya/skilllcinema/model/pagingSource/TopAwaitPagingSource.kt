package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.AlreadyViewedRepository
import ru.diplomnaya.skilllcinema.model.allRepository.MoviePagedTopAwaitListRepository
import ru.diplomnaya.skilllcinema.model.database.DataBase

class TopAwaitPagingSource : PagingSource<Int, Movie>() {
    private val repository = MoviePagedTopAwaitListRepository()

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getTopAwaitList(page)
        }.fold(
            onSuccess = { it ->

                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )

            },
            onFailure = { LoadResult.Error(it) })

    }


    private companion object {
        private val FIRST_PAGE = 1
    }
}