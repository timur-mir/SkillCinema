package ru.diplomnaya.skilllcinema.presentation.search


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.CustomizationSearchMainBoardBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.country_check
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.flagLooking
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.genre_check

import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.year_left_edge_check
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.year_right_edge_check
class SettingsSearchMainFragment : Fragment() {
    private var _binding: CustomizationSearchMainBoardBinding? = null
    val binding get() = _binding!!
    var film_type: String = "ALL"
    var sort_by: String = "YEAR"
    var hideViewedFilm=true
    var hideViewFilmTitleState="Скрывать"
    var dontHideViewFilmTitleState="Не скрывать"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomizationSearchMainBoardBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycle.coroutineScope.launch {
            applicationDatastore.edit { preferences ->
                if(preferences[PreferenceKeys.HIDE_VIEWED_FILM]==hideViewedFilm){
                    binding.viewedStateImageButton.setImageResource(R.drawable.eye_closed)
                    binding.viewedStateTextView.text= preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE]
                }
                else
                {
                    binding.viewedStateImageButton.setImageResource(R.drawable.eye)
                    binding.viewedStateTextView.text= preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE]
                }
            }
        }
        lifecycle.coroutineScope.launch {
            bindingSettingsValue()
        }

        binding.viewedStateImageButton.setOnClickListener{
            if(hideViewedFilm) {
                hideViewedFilm = false
                flagLooking="Не скрывать"
                binding.viewedStateImageButton.setImageResource(R.drawable.eye)
                binding.viewedStateTextView.text= flagLooking

            }
            else
            {
                hideViewedFilm = true
                flagLooking="Скрывать"
                binding.viewedStateImageButton.setImageResource(R.drawable.eye_closed)
                binding.viewedStateTextView.text= flagLooking
            }
        }
        binding.country.setOnClickListener {
            val action =
                SettingsSearchMainFragmentDirections.actionSettingsSearchMainFragmentToSettingsCountryFragment()
            findNavController().navigate(action)
        }
        binding.genre.setOnClickListener {
            val action =
                SettingsSearchMainFragmentDirections.actionSettingsSearchMainFragmentToSettingsGenreFragment()
            findNavController().navigate(action)
        }
        binding.year.setOnClickListener {
            val action =
                SettingsSearchMainFragmentDirections.actionSettingsSearchMainFragmentToSettingsYearsFragment()
            findNavController().navigate(action)
        }
        binding.showAllFilmsSerials.setOnClickListener {
            film_type = "ALL"
        }
        binding.showFilms.setOnClickListener {
            film_type = "FILM"
        }
        binding.showSerials.setOnClickListener {
            film_type = "TV_SERIES"
        }
        binding.sortDate.setOnClickListener {
            sort_by = "YEAR"
        }
        binding.sortPopulation.setOnClickListener {
            sort_by = "NUM_VOTE"
        }
        binding.sortRating.setOnClickListener {
            sort_by = "RATING"
        }
        binding.applySettings.setOnClickListener {
            lifecycle.coroutineScope.launch {
                applicationDatastore.edit { preferences ->
                    if (country_check.isNotEmpty()) {
                        preferences[PreferenceKeys.COUNTRY_NAME] = country_check
                        binding.countryTitle.text = preferences[PreferenceKeys.COUNTRY_NAME]
                    }
                    if (genre_check.isNotEmpty()) {
                        preferences[PreferenceKeys.GENRE_TITLE] = genre_check
                        binding.genreType.text = preferences[PreferenceKeys.GENRE_TITLE]
                    }
                    preferences[PreferenceKeys.HIDE_VIEWED_FILM]=hideViewedFilm
                    preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE]= flagLooking
                    preferences[PreferenceKeys.RATING_FROM] = year_left_edge_check
                    preferences[PreferenceKeys.RATING_TO] = year_right_edge_check
                    preferences[PreferenceKeys.FILM_TYPE] = film_type
                    preferences[PreferenceKeys.SORT_BY] = sort_by
                    Handler().postDelayed(
                        {
                            val action=
                                SettingsSearchMainFragmentDirections.actionSettingsSearchMainFragmentToSearchFragment2()
                            findNavController().navigate(action)
                        },
                        1000
                    )


//                    if(preferences[PreferenceKeys.HIDE_VIEWED_FILM]==true){
//                        binding.viewedStateImageButton.setImageResource(R.drawable.eye_closed)
//                        binding.viewedStateTextView.text= preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE]
//                    }
//                    else
//                    {
//                        binding.viewedStateImageButton.setImageResource(R.drawable.eye)
//                        binding.viewedStateTextView.text= preferences[PreferenceKeys.HIDE_OR_SHOW_VIEWED_FILM_TITLE]
//                    }

                }
            }
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun bindingSettingsValue() {
        binding.sliderRating.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                binding.ratingAny.text = "${slider.values[0]}-${slider.values[1]}"
                year_left_edge_check = slider.values[0].toInt()
                year_right_edge_check = slider.values[1].toInt()


            }

        })


        lifecycle.coroutineScope.launch {
            delay(350)
            val pref = applicationDatastore.data.first()
            binding.countryTitle.text = pref[PreferenceKeys.COUNTRY_NAME]
            binding.genreType.text = pref[PreferenceKeys.GENRE_TITLE]
            binding.yearPeriod.text =
                "${pref[PreferenceKeys.YEAR_FROM]}-${pref[PreferenceKeys.YEAR_TO]}"
            binding.ratingAny.text =
                "${pref[PreferenceKeys.RATING_FROM]}-${pref[PreferenceKeys.RATING_TO]}"

        }


    }


    object SearchCompanionPlace {
        var flagLooking="Скрывать"
        lateinit var store_app: DataStore<Preferences>
        var country_check: String = ""
        var genre_check: String = ""
        var year_left_edge_check = 1
        var year_right_edge_check = 10
    }
}