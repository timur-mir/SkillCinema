package ru.diplomnaya.skilllcinema.presentation.search

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.FragmentSearchBinding
import ru.diplomnaya.skilllcinema.databinding.SettingsButtonBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.intro.IntroActivity.State.searchState
import ru.diplomnaya.skilllcinema.presentation.search.SearchFragment.randFilmForSearch.countryId
import ru.diplomnaya.skilllcinema.presentation.search.SearchFragment.randFilmForSearch.genreId
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.PremieresFullListAdapter
import kotlin.properties.Delegates
import kotlin.random.Random


class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    val binding get() = _binding!!

    private val searchViewModel by viewModels<SearchFilmsViewModel>()
    var searchAdapter = PremieresFullListAdapter { filmInfo -> findFilmDetailInfo(filmInfo) }
    var nameCountry: String = "Россия"
    var genreTitle: String = "Мелодрама"
    var filmRatingFrom = 8
    var filmRatingTo = 10
    var sortBy = "RATING"
    var filmType = "FILM"
    var beginYearForSearch = 1998
    var lastYearForSearch = 2023
    var hideViewedFilm = true
    var showFilm = "Скрывать"
    var hideViewedFilmPref by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.coroutineScope.launch {
            applicationDatastore.edit { preferences ->
                if (preferences[PreferenceKeys.HIDE_VIEWED_FILM] == null) {
                    preferences[PreferenceKeys.HIDE_VIEWED_FILM] = hideViewedFilm
                }

                if (preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE] == null) {
                    preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE] = showFilm
                }
            }
        }

        addCustomView()
        binding.searchResultRecycler.adapter = searchAdapter
        binding.searchResultRecycler.setHasFixedSize(true)
        binding.searchResultRecycler.addItemDecoration(ItemOffsetDecoration(requireContext()))
        val rootView = binding.searchFilm.rootView
        val settingsSearchButton = rootView.findViewById<ImageButton>(R.id.custom_button)

        settingsSearchButton.setOnClickListener {
            val action =
                SearchFragmentDirections.actionSearchFragment2ToSettingsSearchMainFragment()

            findNavController().navigate(action)
        }

        (binding.searchFilm as SearchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(searchString: String?): Boolean {
                if (searchString!!.isNotEmpty() && searchString.isNotBlank()) {
                    viewLifecycleOwner.lifecycleScope
                        .launch {
                            searchViewModel.getCountriesGenres()
                            delay(300)
                            searchState = true
                            if (searchState) {
                                val pref =
                                    applicationDatastore.data.first()
                                if (applicationDatastore.data.first()
                                        .contains(PreferenceKeys.COUNTRY_NAME)
                                ) {
                                    nameCountry = pref[PreferenceKeys.COUNTRY_NAME].toString()
                                }
                                if (applicationDatastore.data.first()
                                        .contains(PreferenceKeys.GENRE_TITLE)
                                ) {
                                    genreTitle = pref[PreferenceKeys.GENRE_TITLE].toString()
                                }

                                filmRatingFrom =
                                    pref[PreferenceKeys.RATING_FROM]?.toInt() ?: filmRatingFrom
                                filmRatingTo =
                                    pref[PreferenceKeys.RATING_TO]?.toInt() ?: filmRatingTo
                                sortBy = pref[PreferenceKeys.SORT_BY]?.toString() ?: sortBy
                                filmType = pref[PreferenceKeys.FILM_TYPE]?.toString() ?: filmType
                                beginYearForSearch = pref[PreferenceKeys.YEAR_FROM]!!
                                lastYearForSearch = pref[PreferenceKeys.YEAR_TO]!!
                                hideViewedFilmPref = pref[PreferenceKeys.HIDE_VIEWED_FILM]!!


                            }

                            searchViewModel.responseAllIdInfo.observe(viewLifecycleOwner) { countriesGenresId ->
                                countriesGenresId.countries.forEachIndexed { index, countries ->
                                    if (countries.country == nameCountry) {
                                        countryId = countries.id!!
                                    }
                                    countriesGenresId.genres.forEachIndexed { index, genres ->
                                        if (genres.genre == genreTitle) {
                                            genreId = genres.id!!
                                        }
                                    }
                                }
                            }


                            delay(1000)
                            if (searchString.isNotEmpty() && hideViewedFilmPref) {
                                searchViewModel.searchFilms(
                                    filmType,
                                    countryId,
                                    genreId,
                                    1,
                                    sortBy,
                                    filmRatingFrom,
                                    filmRatingTo,
                                    beginYearForSearch,
                                    lastYearForSearch,
                                    searchString
                                )
                                searchViewModel.responsList.observe(viewLifecycleOwner) {
                                    searchAdapter.submitList(
                                        it.filter { it.viewed != true }
                                    )
                                }
                            } else {
                                if (searchString.isNotEmpty() && !hideViewedFilmPref) {
                                    searchViewModel.searchFilms(
                                        filmType,
                                        countryId,
                                        genreId,
                                        1,
                                        sortBy,
                                        filmRatingFrom,
                                        filmRatingTo,
                                        beginYearForSearch,
                                        lastYearForSearch,
                                        searchString
                                    )
                                    searchViewModel.responsList.observe(viewLifecycleOwner) {
                                        searchAdapter.submitList(
                                            it
                                        )
                                    }
                                }

                            }
                        }

                    return true
                }
                return true
            }

        })


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun addCustomView() {
        val linearLayoutOfSearchView: LinearLayout =
            (binding.searchFilm.getChildAt(0) as LinearLayout)!!
        linearLayoutOfSearchView.addView(CustomView(activity?.applicationContext!!, null))
    }

    fun findFilmDetailInfo(film: Movie) {
        val action =
            SearchFragmentDirections.actionSearchFragment2ToMovieDetailFragment(film)

        findNavController().navigate(action)

    }

    inner class CustomView @kotlin.jvm.JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
    ) : FrameLayout(context, attrs, defStyleAttr) {

        private lateinit var binding: SettingsButtonBinding

        init {
            binding = SettingsButtonBinding.inflate(LayoutInflater.from(context))
            addView(binding.root)

        }


    }

    object randFilmForSearch {

        val countryS = Random.nextInt(1, 240)
        var countryId = 34
        var filmType = ""
        var genreId = 1
        val pageRandom = Random.nextInt(1, 10)
    }

}