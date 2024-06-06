package ru.diplomnaya.skilllcinema.presentation.intro

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionFragment
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionScreen

class CompositionAdapter(private val screens: List<CompositionScreen>, activity: FragmentActivity) :
    FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        val screen: CompositionScreen = screens[position]
        return CompositionFragment.newInstance(
            textRes = screen.textRes,
            bgColorRes = screen.bgColorRes,
            drawableRes = screen.drawableRes
        )
    }
}