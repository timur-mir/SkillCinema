package ru.diplomnaya.skilllcinema.presentation.filmography

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ActorFilmographyFragmentBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.entities.Films
import ru.diplomnaya.skilllcinema.model.entities.ShortFilmInfo
import ru.diplomnaya.skilllcinema.model.entities.ShortInfoActors
import ru.diplomnaya.skilllcinema.presentation.actors.ActorBestFilmsAdapter
import ru.diplomnaya.skilllcinema.presentation.actors.ActorViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyFragment.HelpActorFilmographyFragment.recoveryObjFilmDetailActorFilmography
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyFragment.HelpActorFilmographyFragment.recoveryObjMovieActorFilmography
import ru.diplomnaya.skilllcinema.presentation.profile.InterestedFilmsViewModel
import ru.diplomnaya.skilllcinema.putArguments
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration

class ActorFilmographyFragment : Fragment() {
    private var _binding: ActorFilmographyFragmentBinding? = null
    val binding get() = _binding!!
    lateinit var actorFilmographyAdapter: ActorFilmographyAdapter
    private val viewModelByFilmInfoDetailByActorFilmography by viewModels<GetFilmDetailInfoViewModel>()
    private val interestedFilmsViewModel by viewModels<InterestedFilmsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActorFilmographyFragmentBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val actorInfo =
            requireArguments().getParcelableArrayList<ShortInfoActors>("bundleActorFullInfo")
        if (actorInfo != null) {
            initAndPutDataAdapter(actorInfo)
        }
    }

    private fun initAndPutDataAdapter(listFilmography: ArrayList<ShortInfoActors>) {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(100)
            actorFilmographyAdapter = ActorFilmographyAdapter { it ->
                Log.d(
                    "TAG10",
                    "Загрузка данных персоны:${it}"
                )
                viewLifecycleOwner.lifecycleScope.launch {
                    lookFilmDetailInfo(it)
                }
            }
            binding.filmographyListOfType.adapter = actorFilmographyAdapter
            val layoutManger =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.filmographyListOfType.layoutManager = layoutManger
            actorFilmographyAdapter.submitList(listFilmography)
            binding.filmographyListOfType.setHasFixedSize(true)
            binding.filmographyListOfType.addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
    }

    private suspend fun lookFilmDetailInfo(shortInfoActor: ShortInfoActors) {
        getDetailFilmInfoForTransformMovieOnFilmographyGallery(shortInfoActor)
        delay(1200)
        val movieTransform = recoveryObjMovieActorFilmography
        Log.d(
            "TAG12",
            "Загрузка данных персоны:${recoveryObjMovieActorFilmography}"
        )

        ActorFilmographyMainFragment.HelpActorFilmographyMainFragment.mutableListAboutFilmographyActor.clear()
        delay(1300)
        val args = Bundle()
        args.putParcelable("movieDetailInfo", movieTransform)
        findNavController().navigate(R.id.movieDetailFragment2, args)


    }

    private suspend fun getDetailFilmInfoForTransformMovieOnFilmographyGallery(shortInfoActor: ShortInfoActors) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelByFilmInfoDetailByActorFilmography.getFilmDetailInfoForActors(shortInfoActor.filmId!!)
            delay(300)
            viewModelByFilmInfoDetailByActorFilmography.filmInfoForActorsLiveData
                .observe(viewLifecycleOwner) { filmDetail ->
                    recoveryObjFilmDetailActorFilmography = filmDetail
                }
            delay(400)
            recoveryObjMovieActorFilmography = Movie(
                recoveryObjFilmDetailActorFilmography.kinopoiskId,
                recoveryObjFilmDetailActorFilmography.nameRu,
                recoveryObjFilmDetailActorFilmography.posterUrlPreview,
                recoveryObjFilmDetailActorFilmography.posterUrlPreview,
                recoveryObjFilmDetailActorFilmography.genres,
                recoveryObjFilmDetailActorFilmography.nameOriginal,
                recoveryObjFilmDetailActorFilmography.countries,
                recoveryObjFilmDetailActorFilmography.ratingImdb,
                false,
                recoveryObjFilmDetailActorFilmography.kinopoiskId

            )
            delay(1000)
            interestedFilmsViewModel.saveInterestedFilm(
                movieToInterestedFilmsActorFilmography(
                    recoveryObjMovieActorFilmography
                )
            )


        }

    }

    fun movieToInterestedFilmsActorFilmography(item: Movie): InterestedFilmsEntity {
        return InterestedFilmsEntity(
            item.kinopoiskId,
            item.filmId,
            item.nameRu ?: "фильм..",
            item.rating,
            item.posterUrl,
            item.genres?.get(0)?.genre.toString()

        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val actorFullInfoKey = "bundleActorFullInfo"
        fun newInstance(
            filmographyActor: ArrayList<ShortInfoActors>

        ): ActorFilmographyFragment {
            return ActorFilmographyFragment().putArguments {
                putParcelableArrayList(actorFullInfoKey, filmographyActor)
            }

        }
    }

    object HelpActorFilmographyFragment {
        lateinit var recoveryObjFilmDetailActorFilmography: FilmDetailInfo
        lateinit var recoveryObjMovieActorFilmography: Movie
    }
}