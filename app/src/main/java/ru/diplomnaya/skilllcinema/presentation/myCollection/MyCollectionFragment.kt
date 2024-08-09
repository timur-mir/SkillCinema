package ru.diplomnaya.skilllcinema.presentation.myCollection

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyCollectionFragmentBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.ItemCollection
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration


class MyCollectionFragment : Fragment() {

    private var _binding: MyCollectionFragmentBinding? = null
    private val binding get() = _binding!!

    var myCollectionFilmAdapter = MyCollectionAdapter { collection -> }

    private val collectionsViewModel by viewModels<CollectionsViewModel>()
    val listCollection = mutableListOf<CollectionFilm>()
    companion object {
        var idCollection = 0
        lateinit var temporaryCollectionFilm: CollectionFilm

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

        binding.titleCollection.text = arg.collectionName

        viewLifecycleOwner.lifecycleScope
            .launch {
                collectionsViewModel.getAllCollection()
            }
        val layoutManger = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
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
                val deletedCollectionFilm: CollectionFilm =
                    listCollection[viewHolder.bindingAdapterPosition]
                temporaryCollectionFilm = deletedCollectionFilm
                removeFilm(arg.collectionName)
                    Snackbar.make(
                    binding.myCollectionRecycler,
                    "Удалёние фильма:  " + deletedCollectionFilm.nameRu,
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setActionTextColor(Color.WHITE)
                    .setBackgroundTint((Color.BLUE))
                    .setAction("Отменить", View.OnClickListener {
                        collectionsViewModel.addNewCollectionItem(
                            ItemCollection(
                                0, temporaryCollectionFilm, idCollection
                            )
                        )
                    }
                    )
                    .setDuration(6000)
                    .show()
            }

        }).attachToRecyclerView(binding.myCollectionRecycler)

    }

    fun removeFilm(paramName:String) {
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.removeItemsOfCollectionFilmById(
                idCollection,
                temporaryCollectionFilm
            )
        }
        loadSelectLoadCollections(paramName)


    }
    private fun loadSelectLoadCollections(collectionName:String){
        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->
            listCollection.clear()
            if (list.isNotEmpty()) {

                for (single in list) {

                    if (single.collections.CollectionName ==collectionName) {
                        single.itemCollectionsList.forEach {
                            if (it.parentCollectionID == single.collections.collectionID) {
                                idCollection = it.parentCollectionID
                                listCollection.add(it.collectionFilmProfile)
                            }
                            binding.myCollectionRecycler.removeAllViews()

                            binding.myCollectionRecycler.adapter =
                                myCollectionFilmAdapter
                            myCollectionFilmAdapter.submitList(listCollection)


                        }
                    }
                }

            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
     //   _binding = null
        binding.myCollectionRecycler.adapter = null
    }
}



