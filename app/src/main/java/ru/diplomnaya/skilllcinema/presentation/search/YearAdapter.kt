package ru.diplomnaya.skilllcinema.presentation.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionScreen

class YearAdapter(private val screens_year: List<ArrayList<String>>, activity: FragmentActivity) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return screens_year.size
    }

    override fun createFragment(position: Int): Fragment {
        val yearsList = screens_year[position]
        return YearPickerFragment.newInstance(yearsList)
    }
}