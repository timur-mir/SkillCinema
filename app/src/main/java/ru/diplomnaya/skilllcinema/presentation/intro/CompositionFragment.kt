package ru.diplomnaya.skilllcinema.presentation.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import ru.diplomnaya.skilllcinema.R

import ru.diplomnaya.skilllcinema.databinding.FragmentCompositionBinding
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.putArguments
import ru.diplomnaya.skilllcinema.presentation.intro.CompositionFragment.stateButton.clickState


class CompositionFragment : Fragment() {
    private var _binding: FragmentCompositionBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompositionBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireView().setBackgroundResource(requireArguments().getInt(KEY_COLOR))
        binding.introTextView.setText(requireArguments().getInt(KEY_TEXT))
        binding.introImageView.setImageResource(requireArguments().getInt(KEY_IMAGE))
        binding.headIntroLevel.background=resources.getDrawable(R.drawable.fon_tekstura)
            binding.nextButton.setOnClickListener {
                clickState=true

            }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


    companion object {

        private const val KEY_TEXT = "text"
        private const val KEY_COLOR = "color"
        private const val KEY_IMAGE = "image"
        fun newInstance(
            @StringRes textRes: Int,
            @ColorRes bgColorRes: Int,
            @DrawableRes drawableRes: Int
        ): CompositionFragment {
            return CompositionFragment().putArguments {
                putInt(KEY_TEXT, textRes)
                putInt(KEY_COLOR, bgColorRes)
                putInt(KEY_IMAGE, drawableRes)

            }

        }

    }
    object stateButton{
       var clickState:Boolean = false
    }

}
