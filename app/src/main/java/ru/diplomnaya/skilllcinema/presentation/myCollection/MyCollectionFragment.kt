package ru.diplomnaya.skilllcinema.presentation.myCollection

import android.content.res.Resources
import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyCollectionFragmentBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.addNewCollectionFlag
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.utilits.getScreenHeight
import ru.diplomnaya.skilllcinema.utilits.getScreenWidth


class MyCollectionFragment : Fragment() {

    private var _binding: MyCollectionFragmentBinding? = null
    private val binding get() = _binding!!

    var myCollectionFilmAdapter = MyCollectionAdapter { collection -> collection.nameRu?.let {
        onItemClickOnListSimpleCollection(
            it,collection.posterUrlPreview
        )
    } }

    private val collectionsViewModel by viewModels<CollectionsViewModel>()
    val listCollection = mutableListOf<CollectionFilm>()
    var listCollectionForRemove = mutableListOf<CollectionFilm>()

    companion object {
        var parentId = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MyCollectionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: MyCollectionFragmentArgs by navArgs()
        if(ProfileFragment.Profile.addNewCollectionFlag)
            addNewCollectionFlag=false

        binding.titleCollection.text = arg.collectionName

        viewLifecycleOwner.lifecycleScope
            .launch {
                collectionsViewModel.getAllCollection()
            }
        val layoutManger2 = GridLayoutManager(requireContext(), 2).apply {
            GridLayoutManager.VERTICAL
        }
        binding.myCollectionRecycler.setHasFixedSize(true)
        binding.myCollectionRecycler.layoutManager = layoutManger2
        binding.myCollectionRecycler.addItemDecoration(ItemOffsetDecoration(requireContext()))
        loadSelectLoadCollections(arg.collectionName)
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val deletedCollectionFilm: CollectionFilm =
                    listCollection[position]

                listCollectionForRemove.add(deletedCollectionFilm)
                listCollection.removeAt(position)
                myCollectionFilmAdapter.notifyItemRemoved(position)


                Snackbar.make(
                    binding.myCollectionRecycler,
                    "Удалёние фильма:  " + deletedCollectionFilm.nameRu,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint((Color.RED))
                    .setAction("Отменить", View.OnClickListener {
                        listCollection.add(position, deletedCollectionFilm)
                        myCollectionFilmAdapter.notifyItemInserted(position)
                        listCollectionForRemove.remove(deletedCollectionFilm)

                    }
                    )
                    .setDuration(6000)
                    .show()
            }

        }).attachToRecyclerView(binding.myCollectionRecycler)

    }

    override fun onPause() {
        super.onPause()
        viewLifecycleOwner.lifecycleScope.launch {
            listCollectionForRemove.forEach { movie ->
                collectionsViewModel.removeFilm(movie)
            }
            findNavController().popBackStack()

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

    private fun loadSelectLoadCollections(collectionName: String) {
        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                for (single in list) {
                    if (single.collections.CollectionName == collectionName) {
                        single.itemCollectionsList.forEach {
                            if (it.parentCollectionID == single.collections.collectionID) {
                                parentId = it.parentCollectionID
                                listCollection.add(it.collectionFilmProfile)
                            }
                        }
                    }
                }

            }
            binding.myCollectionRecycler.adapter =
                myCollectionFilmAdapter
            myCollectionFilmAdapter.submitList(listCollection)

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //   _binding = null
        binding.myCollectionRecycler.adapter = null
    }

}



