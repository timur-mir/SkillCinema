package ru.diplomnaya.skilllcinema.presentation.gallery

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages

class GalleryStateAdapter(private val galleryScreens: List<ArrayList<ItemsImages>>, fm: Fragment) :
    FragmentStateAdapter(fm)  {
    override fun getItemCount(): Int {
        return galleryScreens.size
    }

    override fun createFragment(position: Int): Fragment {
        return GalleryFragment.newInstance(galleryScreens[position])
    }
}