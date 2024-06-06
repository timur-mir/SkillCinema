package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.AnyCountriesAndGenresFilmsRepository
import ru.diplomnaya.skilllcinema.view.MovieListFragment.randFilm.country
import ru.diplomnaya.skilllcinema.view.MovieListFragment.randFilm.genre
class AnyCountriesAndGenresFilmsPagingSource: PagingSource<Int, Movie>() {
    private val repository = AnyCountriesAndGenresFilmsRepository()

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = FIRST_PAGE

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {

            repository.getDifferentRandomFilms(country,genre, page)

        }.fold(
            onSuccess = {
                PagingSource.LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page + 1
                )
            },
            onFailure = { PagingSource.LoadResult.Error(it) })

    }


    private companion object {
        private const val FIRST_PAGE = 1
    }
}