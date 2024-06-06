package ru.diplomnaya.skilllcinema.presentation.serial

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter

import ru.diplomnaya.skilllcinema.databinding.SerialFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.Items
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionFragment
import ru.diplomnaya.skilllcinema.putArguments
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration

class SeasonFragment : Fragment() {
    private var _binding: SerialFragmentBinding? = null
    val binding get() = _binding!!
    val seasonAdapter = SerialAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SerialFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val list = requireArguments().getParcelable<Items>(T)
        binding.seasonTotalEpizode.text =
            "${list?.number} ${"сезон"} ${list?.episodes?.size} ${"серий"} "
        with(binding.serialSeasonEpizodeList) {
            seasonAdapter.submitList(list?.episodes)
            adapter = seasonAdapter
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.serialSeasonEpizodeList.adapter = null
        _binding = null
    }

    companion object {
        val T = "bundleSeason"
        fun newInstance(
            list: Items

        ): SeasonFragment {
            return SeasonFragment().putArguments {
                putParcelable(T, list)
            }

        }
    }
}

