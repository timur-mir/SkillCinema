package ru.diplomnaya.skilllcinema.presentation.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.CustomizationSearchGenreFragmentBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.genre_check

class SettingsGenreFragment:Fragment() {
    private var _binding: CustomizationSearchGenreFragmentBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomizationSearchGenreFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val genreSearchList = arrayListOf<String>()
        genreSearchList.add("Комедия")
        genreSearchList.add("Мелодрама")
        genreSearchList.add("Вестерн")
        genreSearchList.add("Боевик")
        genreSearchList.add("Драма")
        fun setData()= ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, genreSearchList)
        var searchAdapter: ArrayAdapter<String> =setData()
        binding.genreSearchList.adapter = searchAdapter
        binding.searchGenre.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (genreSearchList.contains(query)) {
                    searchAdapter.getFilter().filter(query)
                    val genreForSearch: String? = query
                    saveData(query.toString())
                } else {
                    Toast.makeText(
                        requireActivity(), "По Вашему запросу ничего не найдено",
                        Toast.LENGTH_LONG
                    ).show();
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
        val clearButton =
            binding.searchGenre.findViewById<androidx.appcompat.widget.AppCompatImageView>(androidx.appcompat.R.id.search_close_btn)
        clearButton.setOnClickListener {
            if (binding.searchGenre.query.isEmpty()) {
                binding.searchGenre.setIconified(true);
            } else {
                binding.searchGenre.setQuery("", false)

            }
        }
        binding.genreSearchList.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as String

                binding.searchGenre.setQuery(selectedItem.toString(), true)
                if (genreSearchList.contains(selectedItem)) {
                    searchAdapter.getFilter().filter(selectedItem)
                    val genreForSearch: String = selectedItem
                    saveData(genreForSearch.toString())
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun saveData(genre_title: String) {
        if (genre_title.isNotEmpty()) {
            genre_check=genre_title
//            lifecycle.coroutineScope.launch {
//                SettingsSearchMainFragment.SearchCompanionPlace.store_app.edit { preferences ->
//                    preferences[PreferenceKeys.GENRE_TITLE] =
//                        genre_title
//                    Toast.makeText(
//                        requireActivity(),
//                        "Записано",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
           // }
        }

    }
}