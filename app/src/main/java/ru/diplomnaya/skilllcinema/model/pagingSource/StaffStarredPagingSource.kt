package ru.diplomnaya.skilllcinema.model.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import kotlinx.coroutines.delay
import ru.diplomnaya.skilllcinema.model.allRepository.StaffStarredRepository
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.callTheSimilarFlag
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.similarFilmId


class StaffStarredPagingSource : PagingSource<Int, StaffStarred>() {
    private val repository = StaffStarredRepository()

    override fun getRefreshKey(state: PagingState<Int, StaffStarred>): Int = FIRST_PAGE

    override suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, StaffStarred> {
        val page = params.key ?: FIRST_PAGE
        return kotlin.runCatching {
            if (callTheSimilarFlag == false) {
                val filmId: Int =
                    if (MovieDetailFragment.FlagAndObject.filmForStaffStarredDownLoad.kinopoiskId != 0) {
                        MovieDetailFragment.FlagAndObject.filmForStaffStarredDownLoad.kinopoiskId
                    } else {
                        MovieDetailFragment.FlagAndObject.filmForStaffStarredDownLoad.filmId
                    }

                repository.getStarredStaff(
                    filmId
                )
            } else {
                repository.getStarredStaff(
                    similarFilmId
                )
            }
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