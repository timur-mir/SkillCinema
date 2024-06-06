package ru.diplomnaya.skilllcinema.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import androidx.viewpager2.widget.ViewPager2
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App.DatastoreLauncher.applicationDatastore
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.CustomYearPickerSearchFragmentBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.search.SettingsYearsFragment.TemporalValueOfYear.yearFromTemporale
import ru.diplomnaya.skilllcinema.presentation.search.SettingsYearsFragment.TemporalValueOfYear.yearToTemporale


class SettingsYearsFragment: Fragment() {
    private var _binding: CustomYearPickerSearchFragmentBinding? = null
    val binding get() = _binding!!
    private val years: List<ArrayList<String>> = listOf(arrayListOf("1998","1999","2000","2001","2002",
        "2003","2004","2005","2006","2007","2008","2009"), arrayListOf("2010","2011","2012","2013","2014",
        "2015","2016","2017","2018","2019","2020","2021"),
        arrayListOf("2022","2023")
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CustomYearPickerSearchFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var viewPagerDateFrom = binding.viewpagerDateFrom
        val viewPagerDateTo = binding.viewpagerDateTo
        val adapter =YearAdapter(years, requireActivity())
        val adapter_copy =YearAdapterCopy(years, requireActivity())

        viewPagerDateFrom.adapter = adapter

        binding.next.setOnClickListener {
            val currentItem=viewPagerDateFrom.currentItem
            viewPagerDateFrom.setCurrentItem(currentItem+1)
        }

        binding.prev.setOnClickListener {
            val currentItem=viewPagerDateFrom.currentItem
            viewPagerDateFrom.setCurrentItem(currentItem-1)
        }
        viewPagerDateTo.adapter=adapter_copy
        binding.nextTo.setOnClickListener {
            val currentItem=viewPagerDateTo.currentItem
            viewPagerDateTo.setCurrentItem(currentItem+1)
        }

        binding.prevTo.setOnClickListener {
            val currentItem=viewPagerDateTo.currentItem
            viewPagerDateTo.setCurrentItem(currentItem-1)
        }


        viewPagerDateFrom.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                  val listYear =  years[position]
                binding.dateInterval.text=
                    "${listYear.first()}-${listYear.last()}"

                if (position == viewPagerDateFrom.adapter!!.itemCount - 1) {

                }

            }
        })
        viewPagerDateTo.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val listYear = years[position]
                binding.dateIntervalTo.text =
                    "${listYear.first()}-${listYear.last()}"

            }
        })
        binding.applyChange.setOnClickListener {
            lifecycle.coroutineScope.launch {
                applicationDatastore.edit { preferences ->
                    preferences[PreferenceKeys.YEAR_TO] =yearToTemporale
                    preferences[PreferenceKeys.YEAR_FROM] =yearFromTemporale

                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    object TemporalValueOfYear{
        var yearFromTemporale = 1998
        var yearToTemporale = 1998
    }
}