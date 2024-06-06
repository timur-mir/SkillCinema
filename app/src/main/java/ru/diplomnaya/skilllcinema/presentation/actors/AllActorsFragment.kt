package ru.diplomnaya.skilllcinema.presentation.actors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import ru.diplomnaya.skilllcinema.databinding.FullListActorsFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.actors.ActorsHelp.actorLifeInfo
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment

import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration



class AllActorsFragment:Fragment() {
    private var _binding: FullListActorsFragmentBinding? = null
    val binding get() = _binding!!
    private val actorsFullListAdapter =
        AllActorsAdapter { actorsInfo -> onItemClickOnActorsInfo(actorsInfo) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FullListActorsFragmentBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fullListActorsRecycler.adapter = actorsFullListAdapter
        val layoutManger =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.fullListActorsRecycler.layoutManager = layoutManger

        binding.fullListActorsRecycler.setHasFixedSize(true)
        binding.fullListActorsRecycler.addItemDecoration(ItemOffsetDecoration(requireContext()))

        val arg:AllActorsFragmentArgs by navArgs()
      actorsFullListAdapter.submitList(arg.actorsList.toList())
    }

    private fun onItemClickOnActorsInfo(staffStarred: StaffStarred) {
        actorLifeInfo=staffStarred
        val action =
            AllActorsFragmentDirections.actionAllActorsFragmentToAboutActorFragment(
               actorLifeInfo
            )
        findNavController().navigate(action)
    }
    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
    }

}
object ActorsHelp{
    lateinit var actorLifeInfo: StaffStarred
}
