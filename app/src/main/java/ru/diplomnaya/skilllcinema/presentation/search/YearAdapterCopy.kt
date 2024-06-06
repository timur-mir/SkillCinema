package ru.diplomnaya.skilllcinema.presentation.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class YearAdapterCopy(private val screens_year_copy: List<ArrayList<String>>, activity: FragmentActivity) :
    FragmentStateAdapter(activity)  {
    override fun getItemCount(): Int {
        return screens_year_copy.size
    }
    override fun createFragment(position: Int): Fragment {
        val yearsList=screens_year_copy[position]
        return YearPickerFragmentCopyFragment.newInstance(yearsList)
    }
}