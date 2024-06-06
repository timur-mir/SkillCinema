package ru.diplomnaya.skilllcinema.presentation.gallery

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.MainGalleryFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages
import ru.diplomnaya.skilllcinema.presentation.detail.ImagesViewModel
import ru.diplomnaya.skilllcinema.presentation.gallery.GalleryActivity.HelpGallery.idFilmOrSerial
import ru.diplomnaya.skilllcinema.presentation.gallery.MainGalleryFragment.HelpMainGalleryFragment.imagesListOfAnyType


class MainGalleryFragment : Fragment() {
    private var _binding: MainGalleryFragmentBinding? = null
    val binding get() = _binding!!
    private val imageViewModelForAllView by viewModels<ImagesViewModel>()
    val listImageFotoResourse = listOf<String>("Кадры из фильма", "Со съёмок", "Постеры")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainGalleryFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initImageBank()
        Handler().postDelayed(
            {
                binding.galleryViewPager.adapter =
                    GalleryStateAdapter(imagesListOfAnyType, this)
                binding.galleryViewPager.setCurrentItem(0, true)
                binding.galleryViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                val tablayoutForGallery = binding.tabLayoutGallery
                val galleryViewPager = binding.galleryViewPager
                TabLayoutMediator(tablayoutForGallery, galleryViewPager) { tab, position ->
                    tab.text = "${listImageFotoResourse.get(position)}"
                }.attach()
            }, 1600
        )

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initImageBank() {
        lifecycleScope.launch {
            delay(200)
            searchImageStill("STILL")
            delay(400)
            searchImageShooting("SHOOTING")
            delay(600)
            searchImageAnyType("POSTER")
        }
    }

    fun searchImageAnyType(typeImage: String) {
        lifecycleScope.launch {
            if (idFilmOrSerial != null) {
                imageViewModelForAllView.getImages(idFilmOrSerial!!, typeImage, 1)
            }
            imageViewModelForAllView.images
                .onEach { images ->

                    if (images != null) {
                        imagesListOfAnyType.add(images.items)
                    }
                }
                .launchIn(this)
        }
    }

    fun searchImageStill(typeImage: String) {
        lifecycleScope.launch {

            if (idFilmOrSerial != null) {
                imageViewModelForAllView.getImagesStill(idFilmOrSerial!!, typeImage, 1)
            }
            imageViewModelForAllView.imagesStill
                .onEach { images ->

                    if (images != null) {
                        imagesListOfAnyType.add(images.items)
                    }
                }
                .launchIn(this)
        }
    }

    fun searchImageShooting(typeImage: String) {
        lifecycleScope.launch {
            if (idFilmOrSerial != null) {
                imageViewModelForAllView.getImagesShooting(idFilmOrSerial!!, typeImage, 1)
            }
            imageViewModelForAllView.imagesShooting
                .onEach { images ->
                    if (images != null) {
                        imagesListOfAnyType.add(images.items)
                    }
                }
                .launchIn(this)
        }
    }

    object HelpMainGalleryFragment {
        var imagesListOfAnyType: MutableList<ArrayList<ItemsImages>> = mutableListOf()
    }

}