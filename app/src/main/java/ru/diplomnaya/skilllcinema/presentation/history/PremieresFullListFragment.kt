package ru.diplomnaya.skilllcinema.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ru.diplomnaya.skilllcinema.databinding.HistoryFragmentBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.PremieresFullListAdapter

class PremieresFullListFragment : Fragment() {
    private var _binding: HistoryFragmentBinding? = null
    val binding get() = _binding!!
    private val premieresFullListAdapter =
        PremieresFullListAdapter { movie -> onItemClickOnListPremieres(movie) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HistoryFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.premieresListRecycler.adapter = premieresFullListAdapter
        binding.premieresListRecycler.layoutManager = GridLayoutManager(requireContext(), 2).apply {
            GridLayoutManager.VERTICAL
        }
        binding.premieresListRecycler.setHasFixedSize(true)
        binding.premieresListRecycler.addItemDecoration(ItemOffsetDecoration(requireContext()))
        val arg: PremieresFullListFragmentArgs by navArgs()
        premieresFullListAdapter.submitList(arg.fullListPremieres.toList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
//        binding.premieresListRecycler.adapter=null
    }

    fun onItemClickOnListPremieres(item: Movie) {
        val action =
            PremieresFullListFragmentDirections.actionPremieresFullListFragmentToMovieDetailFragment(
                item
            )
        findNavController().navigate(action)
    }
}