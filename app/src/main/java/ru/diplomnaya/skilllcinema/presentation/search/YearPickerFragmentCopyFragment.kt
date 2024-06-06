package ru.diplomnaya.skilllcinema.presentation.search


import android.R
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.databinding.CustomDataPickerCopyBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsYearsFragment.TemporalValueOfYear.yearToTemporale
import ru.diplomnaya.skilllcinema.putArguments

class YearPickerFragmentCopyFragment() :Fragment() {
    private var _binding: CustomDataPickerCopyBinding? = null
    val binding get() = _binding!!
    val yearMap = mapOf("1998" to 1998,"1999" to 1999,"2000" to 2000,"2001" to 2001,"2002" to 2002,
        "2003" to 2003,"2004" to 2004,"2005" to 2005,"2006" to 2006,"2007" to 2007,"2008" to 2008,
        "2009" to 2009, "2010" to 2010,"2011" to 2011,"2012" to 2012,"2013" to 2013,"2014" to 2014,
        "2015" to 2015,"2016" to 2016,"2017" to 2017,"2018" to 2018,"2019" to 2019,"2020" to 2020,
        "2021" to 2021,"2022" to 2022,"2023" to 2023)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomDataPickerCopyBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list_years =
            requireArguments().getStringArrayList(YearPickerFragmentCopyFragment.KEY_YEAR_ARRAY_COPY)
        list_years?.forEach { year ->
            addChip(year)
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun addChip(yearValueString: String) {
        val color_stroke = resources.getColor(R.color.white)
        val simpleChip= Chip(requireContext())
        simpleChip.setChipStrokeColor(ColorStateList.valueOf(color_stroke))
        simpleChip.text=yearValueString
        simpleChip.setTextColor(resources.getColor(R.color.black))
        simpleChip.textSize= TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 8f,
            resources.displayMetrics
        )
        val paddingDp = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 2f,
            resources.displayMetrics
        ).toInt()
        simpleChip.setPadding(paddingDp, paddingDp, paddingDp, paddingDp)
        simpleChip.setOnClickListener {
            lifecycle.coroutineScope.launch {
                applicationDatastore.edit { preferences ->
                    if( yearMap.contains(yearValueString)){
                        val year=yearMap[yearValueString]
                        SettingsYearsFragment.TemporalValueOfYear.yearToTemporale=year!!

                    }
                }

            }
        }
        binding.chipGroupCopy.addView(simpleChip)

    }

    companion object {

        private const val KEY_YEAR_ARRAY_COPY = "year_array"

        fun newInstance(
            year_array_copy: ArrayList<String>,

            ): YearPickerFragmentCopyFragment {
            return YearPickerFragmentCopyFragment().putArguments {
                putStringArrayList(KEY_YEAR_ARRAY_COPY, year_array_copy)

            }

        }
    }
}