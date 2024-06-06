package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.allRepository.AnyCountriesAndGenresTv_SeriesRepository
import ru.diplomnaya.skilllcinema.view.MovieListFragment
import kotlin.random.Random

class AnyCountriesAndGenresTv_SeriesPagingSource: PagingSource<Int, Movie>() {
    private val repository = AnyCountriesAndGenresTv_SeriesRepository()

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? = FIRST_PAGE

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {

            repository.getDifferentTv_Series(
                randFilmAlter.country,
                randFilmAlter.genre, randFilmAlter.pageRandom
            )

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

    object randFilmAlter {
        var country = Random.nextInt(1, 240)
        var genre = Random.nextInt(1, 33)
        var pageRandom = Random.nextInt(1, 10)
        fun refreshSeriesQuery() {
            country = Random.nextInt(1, 240)
            genre = Random.nextInt(1, 33)
            pageRandom = Random.nextInt(1, 10)
        }
    }
}
