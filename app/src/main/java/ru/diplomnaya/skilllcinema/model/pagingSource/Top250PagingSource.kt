package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.diplomnaya.skilllcinema.model.allRepository.MoviePagedTop250ListRepository
import ru.diplomnaya.skilllcinema.model.Movie

class Top250PagingSource: PagingSource<Int, Movie>() {
    private val repository = MoviePagedTop250ListRepository()
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            repository.getTop250List(page)
        }.fold(
            onSuccess = {
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