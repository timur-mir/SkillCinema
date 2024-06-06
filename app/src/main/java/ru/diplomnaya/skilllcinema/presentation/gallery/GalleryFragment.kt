package ru.diplomnaya.skilllcinema.presentation.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.GalleryFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages
import ru.diplomnaya.skilllcinema.putArguments
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration

class GalleryFragment : Fragment() {
    private var _binding: GalleryFragmentBinding? = null
    val binding get() = _binding!!
    private val galleryAdapter = GalleryAdapter { imageUrl -> lookImage(imageUrl) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GalleryFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val listItemsImages = requireArguments().getParcelableArrayList<ItemsImages>(imageListKey)
        with(binding.imageListOfType) {
            adapter = galleryAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
            galleryAdapter.submitList(listItemsImages)
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun lookImage(imageUrl: String) {
        MainGalleryFragment.HelpMainGalleryFragment.imagesListOfAnyType.clear()
        val args = Bundle()
        args.putString("urlPic", imageUrl)
        findNavController().navigate(R.id.imageFragment2, args)

    }

    companion object {
        val imageListKey = "bundleImagesList"
        fun newInstance(
            list: ArrayList<ItemsImages>
        ): GalleryFragment {
            return GalleryFragment().putArguments {
                putParcelableArrayList(imageListKey, list)
            }
        }
    }

}