package ru.diplomnaya.skilllcinema.presentation.filmography

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.diplomnaya.skilllcinema.model.entities.ShortInfoActors

class ActorFilmographyStateAdapter(private val actorsFilmsScreens:MutableList<ArrayList<ShortInfoActors>>, fm: Fragment):
    FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return actorsFilmsScreens.size
    }

    override fun createFragment(position: Int): Fragment {
        return ActorFilmographyFragment.newInstance(actorsFilmsScreens[position])
    }


}