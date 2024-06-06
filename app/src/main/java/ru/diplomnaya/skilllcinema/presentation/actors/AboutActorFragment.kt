package ru.diplomnaya.skilllcinema.presentation.actors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.AboutActorFragmentBinding
import ru.diplomnaya.skilllcinema.model.Country
import ru.diplomnaya.skilllcinema.model.Genre
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.entities.ShortFilmInfo
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.actors.HelpCompanionObject.actorContainer
import ru.diplomnaya.skilllcinema.presentation.actors.HelpCompanionObject.recoveryObjFilmDetail
import ru.diplomnaya.skilllcinema.presentation.actors.HelpCompanionObject.recoveryObjMovie
import ru.diplomnaya.skilllcinema.presentation.actors.HelpCompanionObject.staffStaredObject
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.SerialViewModel
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyActivity
import ru.diplomnaya.skilllcinema.presentation.profile.InterestedFilmsViewModel
import ru.diplomnaya.skilllcinema.presentation.serial.SerialActivity
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.view.MovieListFragmentDirections
import java.nio.channels.Channel
import java.util.ArrayList


class AboutActorFragment : Fragment() {
    private var _binding: AboutActorFragmentBinding? = null
    val binding get() = _binding!!
    lateinit var actorBestFilmsAdapter: ActorBestFilmsAdapter
    private val actorViewModel by viewModels<ActorViewModel>()
    private val viewModelByFilmInfoDetail by viewModels<GetFilmDetailInfoViewModel>()
    var actorUrl: String = ""
    var mutableListAboutFilms: MutableList<ShortFilmInfo> = mutableListOf<ShortFilmInfo>()
    private val interestedFilmsViewModelActorsUse by viewModels<InterestedFilmsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AboutActorFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg: AboutActorFragmentArgs by navArgs()
        staffStaredObject = arg.actorsInfoForPage
        viewLifecycleOwner.lifecycleScope.launch {
            val chanel = kotlinx.coroutines.channels.Channel<Actor>()
            val chanel2 = kotlinx.coroutines.channels.Channel<Actor>()
            viewLifecycleOwner.lifecycleScope.launch {
                actorViewModel.getActorFullInfo(arg.actorsInfoForPage.staffId)
                actorViewModel.actorFullInfo
                    .onEach { info ->
                        if (info != null) {
                            actorContainer = info
                            chanel.send(info)
                            chanel2.send(info)
                        }
                        actorUrl = info?.posterUrl.toString()
                        binding.actorNamePage.text = info?.nameRu ?: info?.nameEn
                        binding.actorProfession.text = info?.profession ?: info?.profession
                        Picasso
                            .with(requireContext())
                            .load(info?.posterUrl)
                            .placeholder(R.drawable.gradient2)
                            .into(binding.actorPicPage)
                    }.launchIn(this)

            }
            viewLifecycleOwner.lifecycleScope.launch {
                val actorContainerIn = chanel.receive()
                actorContainerIn.films.forEach {
                    it.filmId?.let { filmID ->
                        delay(10)
                        viewModelByFilmInfoDetail.getFilmDetailInfoForActors(
                            filmID
                        )
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                val actorContainerInMap = chanel2.receive()
                var x = 0
                viewModelByFilmInfoDetail.filmInfoForActorsLiveData.observe(viewLifecycleOwner) { item ->
                    if (item.genres?.getOrNull(0) != null)
                        mutableListAboutFilms += ShortFilmInfo(
                            item?.genres[0].genre.toString(),
                            item.posterUrlPreview,
                            actorContainerInMap.films[x].filmId,
                            actorContainerInMap.films[x].rating,
                            actorContainerInMap.films[x].nameRu
                        )
                    else
                        mutableListAboutFilms += ShortFilmInfo(
                            "жанр неизвестен",
                            item.posterUrlPreview,
                            actorContainerInMap.films[x].filmId,
                            actorContainerInMap.films[x].rating,
                            actorContainerInMap.films[x].nameRu
                        )

                    x++
                }

            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            actorBestFilmsAdapter =
                ActorBestFilmsAdapter { bestMovie ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        onItemClickOnBestFilm(bestMovie)
                    }
                }
            binding.actorBestListRecycler.adapter = actorBestFilmsAdapter
            val layoutManger =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            binding.actorBestListRecycler.layoutManager = layoutManger
            actorBestFilmsAdapter.submitList(mutableListAboutFilms)
            binding.actorBestListRecycler.setHasFixedSize(true)
            binding.actorBestListRecycler.addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
            binding.actorPicPage.setOnClickListener {
                val action =
                    AboutActorFragmentDirections.actionAboutActorFragmentToActorFaceFragment(
                        actorUrl
                    )

                findNavController().navigate(action)
            }
            binding.openFilmsList.setOnClickListener {
                val intent = Intent(context, ActorFilmographyActivity::class.java).apply {
                    putExtra("person", arg.actorsInfoForPage)
                }
                requireContext().startActivity(intent)
            }
        }

        fun movieToInterestedFilmsActor(item: Movie): InterestedFilmsEntity {
            return InterestedFilmsEntity(
                item.kinopoiskId,
                item.filmId,
                item.nameRu ?: "фильм..",
                item.rating,
                item.posterUrl,
                item.genres?.get(0)?.genre.toString()

            )
        }

    suspend fun getDetailFilmInfoForTransformMovie(shortFilmInfo: ShortFilmInfo): Movie {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelByFilmInfoDetail.getFilmDetailInfoForActors(
                shortFilmInfo.idFilm!!
            )

            viewModelByFilmInfoDetail.filmInfoForActorsLiveData.observe(viewLifecycleOwner) { filmDetail ->
                recoveryObjFilmDetail = filmDetail
            }
            delay(300)
            recoveryObjMovie = Movie(
                recoveryObjFilmDetail.kinopoiskId,
                recoveryObjFilmDetail.nameRu,
                recoveryObjFilmDetail.posterUrlPreview,
                recoveryObjFilmDetail.posterUrlPreview,
                recoveryObjFilmDetail.genres  ,
                recoveryObjFilmDetail.nameOriginal,
                recoveryObjFilmDetail.countries ,
                recoveryObjFilmDetail.ratingImdb,
                false,
                recoveryObjFilmDetail.kinopoiskId

            )

        }
        delay(600)
        interestedFilmsViewModelActorsUse.saveInterestedFilm(
            movieToInterestedFilmsActor(
                recoveryObjMovie
            )
        )
        return recoveryObjMovie
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    suspend fun onItemClickOnBestFilm(shortFilmInfo: ShortFilmInfo) {
        var movieTransform = getDetailFilmInfoForTransformMovie(shortFilmInfo)
            delay(1000)
        val action =
            AboutActorFragmentDirections.actionAboutActorFragmentToMovieDetailFragment(
                movieTransform
            )
        findNavController().navigate(action)
    }
    }

    object HelpCompanionObject {
        lateinit var recoveryObjFilmDetail: FilmDetailInfo
        lateinit var recoveryObjMovie: Movie
        lateinit var actorContainer: Actor
        lateinit var staffStaredObject: StaffStarred
    }

