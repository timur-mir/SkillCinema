package ru.diplomnaya.skilllcinema.presentation.serial

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionFragment


class SerialFragmentStateAdapter(private val serialScreens: Serial, activity: FragmentActivity) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return serialScreens.total!!
    }

    override fun createFragment(position: Int): Fragment {
        return SeasonFragment.newInstance(serialScreens.items[position])
    }
}