package ru.diplomnaya.skilllcinema.presentation.main

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.coroutineScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ActivityMainBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment
import ru.diplomnaya.skilllcinema.presentation.search.SettingsYearsFragment


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
                 defaultSettingsForFilmsSearch()
        val bottomNavigationView = binding.panelNavigationMain
        val navController = findNavController(R.id.fragmentContainer)


        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId) {
                R.id.movieListFragment2-> {
                    navController.navigate(R.id.movieListFragment2)

                }
                R.id.searchFragment2-> {
                    navController.navigate(R.id.searchFragment2)
                }
                R.id.profileFragment2-> {
                    navController.navigate(R.id.profileFragment2)
                }

            }
            true
        }


    }
    override fun onDestroy() {
        super.onDestroy()
      //  _binding=null
    }

    private fun defaultSettingsForFilmsSearch() {

            lifecycle.coroutineScope.launch {
                applicationDatastore.edit { preferences ->
                    preferences[PreferenceKeys.COUNTRY_NAME] ="Россия"
                    preferences[PreferenceKeys.GENRE_TITLE] ="Комедия"
                    preferences[PreferenceKeys.RATING_FROM] =8
                    preferences[PreferenceKeys.RATING_TO] =10
                    preferences[PreferenceKeys.FILM_TYPE] = "FILM"
                    preferences[PreferenceKeys.SORT_BY] = "YEAR"
                    preferences[PreferenceKeys.YEAR_TO] =2020
                    preferences[PreferenceKeys.YEAR_FROM] =1998

                }
            }
        }
}
