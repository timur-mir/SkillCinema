package ru.diplomnaya.skilllcinema.presentation.myCollection

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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyCollectionFragmentBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm


class MyCollectionFragment : Fragment() {

    private var _binding: MyCollectionFragmentBinding? = null
    private val binding get() = _binding!!

    var myCollectionFilmAdapter = MyCollectionAdapter { collection -> }

    private val collectionsViewModel by viewModels<CollectionsViewModel>()

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
        binding.myCollectionRecycler.layoutManager = layoutManger

        val listCollection = mutableListOf<CollectionFilm>()

        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {

                for (single in list) {

                    if (single.collections.CollectionName == arg.collectionName) {
                        single.itemCollectionsList.forEach {
                            if (it.parentCollectionID == single.collections.collectionID) {
                                listCollection.add(it.collectionFilmProfile)
                            }
                            myCollectionFilmAdapter.submitList(listCollection)
                            binding.myCollectionRecycler.adapter =
                                myCollectionFilmAdapter


                        }
                    }
                }

            }

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
//        binding.myCollectionRecycler.adapter = null
    }
}



