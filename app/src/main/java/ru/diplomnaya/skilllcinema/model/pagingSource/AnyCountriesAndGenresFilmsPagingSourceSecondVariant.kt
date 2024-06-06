package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.AnyCountriesAndGenresFilmsSecondVariantRepository
import ru.diplomnaya.skilllcinema.view.MovieListFragment

class AnyCountriesAndGenresFilmsPagingSourceSecondVariant : PagingSource<Int, Movie>() {
    private val repository = AnyCountriesAndGenresFilmsSecondVariantRepository()

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = FIRST_PAGE

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {

        repository.getDifferentRandomFilmsSecondVariant     (
                MovieListFragment.randFilm.country_second_variant,
                MovieListFragment.randFilm.genre_second_variant, page)

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
        private val FIRST_PAGE = 1
    }
}