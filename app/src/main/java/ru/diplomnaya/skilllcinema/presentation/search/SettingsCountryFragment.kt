package ru.diplomnaya.skilllcinema.presentation.search

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.CustomizationCountrySearchFragmentBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsSearchMainFragment.SearchCompanionPlace.country_check


class SettingsCountryFragment : Fragment() {
    private var _binding: CustomizationCountrySearchFragmentBinding? = null
    val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomizationCountrySearchFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val countrySearchList = arrayListOf<String>()
        countrySearchList.add("Россия")
        countrySearchList.add("Великобритания")
        countrySearchList.add("Германия")
        countrySearchList.add("США")
        countrySearchList.add("Франция")
        fun setData()= ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, countrySearchList)
        var searchAdapter: ArrayAdapter<String> =setData()
        binding.countrySearchList.adapter = searchAdapter
        binding.searchCountry.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (countrySearchList.contains(query)) {
                    searchAdapter.getFilter().filter(query)
                    saveData(query.toString())
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "По Вашему запросу ничего не найдено",
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
            binding.searchCountry.findViewById<androidx.appcompat.widget.AppCompatImageView>(
                androidx.appcompat.R.id.search_close_btn
            )
        clearButton.setOnClickListener {
            if (binding.searchCountry.query.isEmpty()) {
                binding.searchCountry.setIconified(true);
            } else {
                binding.searchCountry.setQuery("", false)

            }
        }
        binding.countrySearchList.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as String

                binding.searchCountry.setQuery(selectedItem.toString(), true)
                if (countrySearchList.contains(selectedItem)) {
                    searchAdapter.getFilter().filter(selectedItem)
                    val countryForSearch: String = selectedItem
                    saveData(countryForSearch.toString())
                }
            }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun saveData(country_name: String) {
        if (country_name.isNotEmpty()) {
            lifecycle.coroutineScope.launch {
                country_check=country_name
//                SettingsSearchMainFragment.SearchCompanionPlace.store_app.edit { preferences ->
//                    preferences[PreferenceKeys.COUNTRY_NAME] =
//                        country_name
//                    Toast.makeText(
//                        requireActivity(),
//                        "Записано",
//                        Toast.LENGTH_LONG
//                    ).show()
            //    }
            }
        }

    }

}