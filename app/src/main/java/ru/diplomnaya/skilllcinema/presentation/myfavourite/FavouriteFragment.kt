package ru.diplomnaya.skilllcinema.presentation.myfavourite

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyFavouriteFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.Favourite
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsFavouritesFilmsViewModel
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.utilits.getScreenHeight
import ru.diplomnaya.skilllcinema.utilits.getScreenWidth
import ru.diplomnaya.skilllcinema.utilits.toFavourite

class FavouriteFragment:Fragment() {
    private var _binding: MyFavouriteFragmentBinding? = null
    private val binding get() = _binding!!
    var myFavouriteFilmAdapter = MyFavouriteAdapter { collectionFavourite -> collectionFavourite.nameRu?.let {
        onItemClickOnListSimpleCollection(
            it,collectionFavourite.posterUrlPreview
        )
    } }
    private val collectionsFavouriteViewModel by viewModels<CollectionsFavouritesFilmsViewModel>()
    var listFavouriteForAdapter = mutableListOf<Favourite>()
    var listCollectionForRemove = mutableListOf<Favourite>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MyFavouriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mArgs = arguments

        if (mArgs != null) {
            binding.titleCollectionFavourite.text = mArgs.getString("name")
        }

        viewLifecycleOwner.lifecycleScope
            .launch {
                collectionsFavouriteViewModel.getAllFavouritesFilms()
            }
        val layoutManger2 = GridLayoutManager(requireContext(), 2).apply {
            GridLayoutManager.VERTICAL
        }
        binding.myCollectionFavourite.setHasFixedSize(true)
        binding.myCollectionFavourite.layoutManager = layoutManger2
        binding.myCollectionFavourite.addItemDecoration(ItemOffsetDecoration(requireContext()))
        collectionsFavouriteViewModel.favouritesFilmsLiveData.observe(viewLifecycleOwner) { list ->
            listFavouriteForAdapter= list.map { it.toFavourite() } as MutableList<Favourite>
                    binding.myCollectionFavourite.adapter  =  myFavouriteFilmAdapter
                    myFavouriteFilmAdapter.submitList(listFavouriteForAdapter)
        }
    }




    private fun onItemClickOnListSimpleCollection(item: String, posterUrl:String) {
        val inflater = layoutInflater;
        val layout = inflater.inflate(R.layout.toast_layout,requireActivity().findViewById(R.id.toast_layout_root) );
        val image = layout.findViewById<ImageView>(R.id.image_toast);
        val text = layout.findViewById<TextView>(R.id.toast_text);
        text.text = "$item"
        val toast = Toast(requireContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, getScreenWidth()/4, getScreenHeight()/2);
        toast.duration = Toast.LENGTH_LONG;
        Picasso.with(requireContext())
            .load(posterUrl)
            .into(image)
        toast.setView(layout);
        toast.show()

    }
}