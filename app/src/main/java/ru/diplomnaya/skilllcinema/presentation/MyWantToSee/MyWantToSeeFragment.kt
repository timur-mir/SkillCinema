package ru.diplomnaya.skilllcinema.presentation.MyWantToSee

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
import androidx.recyclerview.widget.GridLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyWantToSeeFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.WantToSee
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsWantToSeeViewModel
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.utilits.getScreenHeight
import ru.diplomnaya.skilllcinema.utilits.getScreenWidth
import ru.diplomnaya.skilllcinema.utilits.toWantToSee

class MyWantToSeeFragment: Fragment() {
    private var _binding: MyWantToSeeFragmentBinding? = null
    private val binding get() = _binding!!
    var myWantToSeeFilmAdapter = MyWantToSeeAdapter { collectionWantToSee -> collectionWantToSee.nameRu?.let {
        onItemClickOnListSimpleCollection(
            it,collectionWantToSee.posterUrlPreview
        )
    } }
    private val collectionsWantToSeeViewModel by viewModels<CollectionsWantToSeeViewModel>()
    var listWantToSeeForAdapter = mutableListOf<WantToSee>()
    var listCollectionForRemove = mutableListOf<WantToSee>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MyWantToSeeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mArgs = arguments

        if (mArgs != null) {
            binding.titleCollectionWantToSee.text = mArgs.getString("name")
        }

        viewLifecycleOwner.lifecycleScope
            .launch {
                collectionsWantToSeeViewModel.getAllWantToSee()
            }
        val layoutManger2 = GridLayoutManager(requireContext(), 2).apply {
            GridLayoutManager.VERTICAL
        }
        binding.myCollectionWantToSee.setHasFixedSize(true)
        binding.myCollectionWantToSee.layoutManager = layoutManger2
        binding.myCollectionWantToSee.addItemDecoration(ItemOffsetDecoration(requireContext()))
        collectionsWantToSeeViewModel.wantToSeeFilmsLiveData .observe(viewLifecycleOwner) { list ->
            listWantToSeeForAdapter= list.map { it.toWantToSee() } as MutableList<WantToSee>
            binding.myCollectionWantToSee.adapter  =  myWantToSeeFilmAdapter
            myWantToSeeFilmAdapter.submitList(listWantToSeeForAdapter)
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