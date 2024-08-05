package ru.diplomnaya.skilllcinema.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.activity.OnBackPressedCallback
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MovieDetailFragmentBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.FavouritesEntity
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity
import ru.diplomnaya.skilllcinema.model.database.WantToSeeEntity
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.entities.FilmNetSource
import ru.diplomnaya.skilllcinema.model.entities.Films
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.callTheSimilarFlag
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.filmForStaffStarredDownLoad
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.filmGlobalUrlInfo
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.filmPosterUrl
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.forAddProfile
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.similarFilmId
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.staffStarredList
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.workedOnFilmList
import ru.diplomnaya.skilllcinema.presentation.gallery.GalleryActivity
import ru.diplomnaya.skilllcinema.presentation.main.StaffStarredViewModel
import ru.diplomnaya.skilllcinema.presentation.main.adapters.StaffStarredAdapter
import ru.diplomnaya.skilllcinema.presentation.profile.InterestedStaffsViewModel
import ru.diplomnaya.skilllcinema.presentation.serial.SerialActivity
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import kotlin.properties.Delegates

class MovieDetailFragment() : Fragment() {

    var idKinopoisk by Delegates.notNull<Int>()
    var filmId by Delegates.notNull<Int>()


    private var _binding: MovieDetailFragmentBinding? = null
    val binding get() = _binding!!

    private val viewModelByFilmInfoDetail by viewModels<GetFilmDetailInfoViewModel>()
    private val favouritesFilmsViewModel by viewModels<CollectionsFavouritesFilmsViewModel>()
    private val collectionsWantTooSeeViewModel by viewModels<CollectionsWantToSeeViewModel>()
    private val collectionsAlreadyViewedViewModel by viewModels<CollectionsAlreadyViewedViewModel>()
    private val serialViewModel by viewModels<SerialViewModel>()
    private val staffStarredViewModel by viewModels<StaffStarredViewModel>()
    private val staffStarredAdapter =
        StaffStarredAdapter { staff ->
            onItemClickOnListStaffStarred(staff)
        }
    private val workedOnFilmAdapter =
        WorkedOnFilmAdapter { worker -> onItemClickOnListStaffStarred(worker) }
    private val imageViewModel by viewModels<ImagesViewModel>()
    private val imageAdapter = ImageAdapter { image ->
        lookImage(image)
    }
    private val similarViewModel by viewModels<SimilarViewModel>()
    private val similarAdapter = SimilarAdapter { similar ->
        lookSimilarFilm(similar.filmId)
    }
    private val interestedStaffsViewModel by viewModels<InterestedStaffsViewModel>()
    private val filmPresentOnNetPlatformViewModel by viewModels<FilmPresentOnNetPlatformViewModel>()


    private fun lookImage(image: ItemsImages) {
        val action =
            (image?.imageUrl ?: image.previewUrl)?.let {
                MovieDetailFragmentDirections.actionMovieDetailFragmentToImageFragment(
                    it
                )
            }
        if (action != null) {
            findNavController().navigate(action)
        }

    }

    private fun onItemClickOnListStaffStarred(staff: StaffStarred) {
        interestedStaffsViewModel.saveInterestedStaff(staffToInterestedStaffs(staff))

        val action =
            MovieDetailFragmentDirections.actionMovieDetailFragmentToAboutActorFragment(staff)
        findNavController().navigate(action)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MovieDetailFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_movieDetailFragment_to_movieListFragment2)
                binding.addToFavorite.setImageResource(R.drawable.heart)
                binding.addToIWantToSee.setImageResource(R.drawable.bookmark)
                binding.alreadyViewed.setImageResource(R.drawable.eye_closed)
                callTheSimilarFlag = false
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        //callTheSimilarFlag = false

        val arg: MovieDetailFragmentArgs by navArgs()

        idKinopoisk = arg.movieDetailInfo.kinopoiskId
        filmId = arg.movieDetailInfo.filmId

//Формирование списка актёров
        with(binding.starredInFilmListRecycler) {
            adapter = staffStarredAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 4, LinearLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))

        }
        with(binding.workedOnTheFilm) {
            adapter = workedOnFilmAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 2, LinearLayoutManager.HORIZONTAL, false)

            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))

        }
        with(binding.galleryList) {
            adapter = imageAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))

        }
        with(binding.relatedFilmList) {
            adapter = similarAdapter
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )

            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))

        }

        filmForStaffStarredDownLoad = arg.movieDetailInfo

        viewLifecycleOwner.lifecycleScope.launch {
          //  staffStarredViewModel.staffStarred.collect { it-> }
            staffStarredViewModel.staffStarred
                .onEach { staff ->
                    staffStarredAdapter.submitData(staff)
                }
                .launchIn(this)
        }
        viewLifecycleOwner.lifecycleScope.launch {

            staffStarredViewModel.staffStarred
                .onEach { staff ->
                    workedOnFilmAdapter.submitData(staff)
                }
                .launchIn(this)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            if (arg.movieDetailInfo.filmId != null && arg.movieDetailInfo.filmId != 0) {
                imageViewModel.getImages(arg.movieDetailInfo.filmId, "STILL", 1)
                imageViewModel.images
                    .onEach { images ->
                        imageAdapter.submitList(images?.items)
                        if (images != null) {
                            if (images.items.size > 10) {
                                binding.allGalleryList.visibility = View.VISIBLE
                                binding.allGalleryList.text = images.items.size.toString()
                            }
                        }
                    }
                    .launchIn(this)
            } else {
                imageViewModel.getImages(arg.movieDetailInfo.kinopoiskId, "STILL", 1)
                imageViewModel.images
                    .onEach { images ->
                        imageAdapter.submitList(images?.items)

                    }
                    .launchIn(this)
            }
        }

        binding.viewGlobal.setOnClickListener {

            viewLifecycleOwner.lifecycleScope.launch {
                if (arg.movieDetailInfo.kinopoiskId != 0) {
                    filmPresentOnNetPlatformViewModel.getMoreUrlFilmOnNet(arg.movieDetailInfo.kinopoiskId)
                } else {
                    filmPresentOnNetPlatformViewModel.getMoreUrlFilmOnNet(arg.movieDetailInfo.filmId)
                }

                filmPresentOnNetPlatformViewModel.filmUrlNetPlatform
                    .onEach { globalUrlItem ->
                        if (globalUrlItem?.items.isNullOrEmpty()) {
                            val str =
                                "https://www.imdb.com/title/" + "${FlagAndObject.requestFilm.imdbId}"
                            filmGlobalUrlInfo = FilmNetSource(
                                str,
                                "Неопределено",
                                "Неопределено"
                            )
                        } else {
                            if (globalUrlItem?.items?.get(0)?.site != "UNKNOWN" && globalUrlItem?.items?.get(
                                    0
                                )?.site != "KINOPOISK_WIDGET"
                            )
                                filmGlobalUrlInfo = globalUrlItem?.items?.get(0) ?: FilmNetSource(
                                    "https://www.youtube.com/watch?v=z9xhWJq8NMQ ",
                                    "О съёмках №2",
                                    "YOUTUBE"
                                )
                            else {
                                val str =
                                    "https://www.imdb.com/title/" + "${FlagAndObject.requestFilm.imdbId}"
                                filmGlobalUrlInfo = FilmNetSource(
                                    str,
                                    "Неопределено",
                                    "Неопределено"
                                )
                            }
                        }
                    }.launchIn(this)
            }
            Handler().postDelayed({
                val intent = Intent(context, GlobalActivity::class.java).apply {
                    putExtra("filmUrlOnGlobal", filmGlobalUrlInfo?.url.toString())
                }
                requireContext().startActivity(intent)
            }, 2000)
        }


        viewLifecycleOwner.lifecycleScope.launch {
            if (arg.movieDetailInfo.filmId != null && arg.movieDetailInfo.filmId != 0) {
                similarViewModel.getSimilar(arg.movieDetailInfo.filmId)
                similarViewModel.similar
                    .onEach { similar ->
                        similarAdapter.submitList(similar?.items)
                        binding.amountRelatedFilm.text = similar?.items?.size.toString()
                    }
                    .launchIn(this)
            } else {
                similarViewModel.getSimilar(arg.movieDetailInfo.kinopoiskId)
                similarViewModel.similar
                    .onEach { similar ->
                        similarAdapter.submitList(similar?.items)
                        binding.amountRelatedFilm.text = similar?.items?.size.toString()
                    }
                    .launchIn(this)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            if (forAddProfile.serial == true) {

                serialViewModel.getSerial(forAddProfile.kinopoiskId)
                serialViewModel.serial

                    .onEach { serial ->
                        var allEpizodeNumber = 0
                        serial?.items?.forEach {
                            allEpizodeNumber = it.episodes.size + allEpizodeNumber
                            binding.seasonAll.text = "${serial.total.toString()}сезон"

                        }
                        binding.seasonAndSeries.text =
                            "${serial?.total.toString()},${allEpizodeNumber}"
                    }
                    .launchIn(this)
            }

        }
        binding.allSeasonInfo.setOnClickListener {
            val intent = Intent(context, SerialActivity::class.java).apply {
                putExtra("serialId", arg.movieDetailInfo.kinopoiskId)
                putExtra("imageType", "STILL")
            }
            requireContext().startActivity(intent)
        }
        binding.allGalleryList.setOnClickListener {
            if (similarFilmId != null && similarFilmId != 0) {
                val intent = Intent(context, GalleryActivity::class.java).apply {
                    putExtra("filmOrSerialId", similarFilmId)
                }
                requireContext().startActivity(intent)
            } else {
                if (arg.movieDetailInfo.kinopoiskId != null && arg.movieDetailInfo.kinopoiskId != 0) {
                    val intent = Intent(context, GalleryActivity::class.java).apply {
                        putExtra("filmOrSerialId", arg.movieDetailInfo.kinopoiskId)
                    }
                    requireContext().startActivity(intent)
                } else {
                    if (arg.movieDetailInfo.filmId != null && arg.movieDetailInfo.filmId != 0) {
                        val intent = Intent(context, GalleryActivity::class.java).apply {
                            putExtra("filmOrSerialId", arg.movieDetailInfo.filmId)
                        }
                        requireContext().startActivity(intent)
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            staffStarredAdapter.loadStateFlow.collect {
                if (staffStarredAdapter.itemCount >= 20) {
                    staffStarredList = staffStarredAdapter.snapshot().items
                    binding.actionAmountInFilmButton.text =
                        staffStarredAdapter.itemCount.toString() + " >"
                }
            }
        }
        binding.actionAmountInFilmButton.setOnClickListener {
            val action =
                MovieDetailFragmentDirections.actionMovieDetailFragmentToAllActorsFragment(
                    staffStarredList.toTypedArray()
                )
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            workedOnFilmAdapter.loadStateFlow.collect {
                if (workedOnFilmAdapter.itemCount >= 20) {
                    workedOnFilmList = workedOnFilmAdapter.snapshot().items
                    binding.starredAmountUnderInFilmButton.text =
                        workedOnFilmAdapter.itemCount.toString() + " >"
                }
            }
        }
        binding.starredAmountUnderInFilmButton.setOnClickListener {
            val action =
                MovieDetailFragmentDirections.actionMovieDetailFragmentToAllActorsFragment(
                    workedOnFilmList.toTypedArray()
                )
            findNavController().navigate(action)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            if (arg.movieDetailInfo.kinopoiskId == 0) {
                delay(300)
                favouritesFilmsViewModel.checkFilm(FlagAndObject.requestFilm.kinopoiskId)
            } else {
                favouritesFilmsViewModel.checkFilm(arg.movieDetailInfo.kinopoiskId)
            }

            favouritesFilmsViewModel.existLive.observe(viewLifecycleOwner) { Yes ->
                if (Yes) {
                    binding.addToFavorite.setImageResource(R.drawable.heart_selected)
                    FlagAndObject.isButtonSelectedFavourite = true
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            if (arg.movieDetailInfo.kinopoiskId == 0) {
                delay(300)
                collectionsAlreadyViewedViewModel.checkFilm(FlagAndObject.requestFilm.kinopoiskId)
            } else {
                collectionsAlreadyViewedViewModel.checkFilm(arg.movieDetailInfo.kinopoiskId)
            }

            collectionsAlreadyViewedViewModel.existLive.observe(viewLifecycleOwner) { Yes ->
                if (Yes) {
                    binding.alreadyViewed.setImageResource(R.drawable.eye)
                    FlagAndObject.isButtonSelectedAlreadyViewed = true

                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            if (arg.movieDetailInfo.kinopoiskId == 0) {
                delay(300)
                collectionsWantTooSeeViewModel.checkFilm(FlagAndObject.requestFilm.kinopoiskId)
            } else {
                collectionsWantTooSeeViewModel.checkFilm(arg.movieDetailInfo.kinopoiskId)
            }

            collectionsWantTooSeeViewModel.existLive.observe(viewLifecycleOwner) { Yes ->
                if (Yes) {
                    binding.addToIWantToSee.setImageResource(R.drawable.bookmark_selected)
                    FlagAndObject.isButtonSelectedWantToSee = true

                }
            }
        }


        binding.addToFavorite.setOnClickListener {
            if (FlagAndObject.isButtonSelectedFavourite) {
                lifecycle.coroutineScope.launch {
                    if (arg.movieDetailInfo.kinopoiskId == 0) {
                        favouritesFilmsViewModel.removeFilmById(FlagAndObject.requestFilm.kinopoiskId)
                    } else {
                        favouritesFilmsViewModel.removeFilmById(arg.movieDetailInfo.kinopoiskId)
                    }

                    binding.addToFavorite.setImageResource(R.drawable.heart)
                    FlagAndObject.isButtonSelectedFavourite = false
                }
            } else {
                lifecycle.coroutineScope.launch {
                    favouritesFilmsViewModel.addFavourite(FlagAndObject.requestFilm)
                    binding.addToFavorite.setImageResource(R.drawable.heart_selected)
                    FlagAndObject.isButtonSelectedFavourite = true
                }
            }
        }
        binding.addToIWantToSee.setOnClickListener {
            if (FlagAndObject.isButtonSelectedWantToSee) {
                lifecycle.coroutineScope.launch {
                    collectionsWantTooSeeViewModel.removeFilmById(arg.movieDetailInfo.kinopoiskId)
                    binding.addToIWantToSee.setImageResource(R.drawable.bookmark)
                    FlagAndObject.isButtonSelectedWantToSee = false
                }
            } else {
                lifecycle.coroutineScope.launch {
                    collectionsWantTooSeeViewModel.addWantToSee(FlagAndObject.requestFilmWantToSee)
                    binding.addToIWantToSee.setImageResource(R.drawable.bookmark_selected)
                    FlagAndObject.isButtonSelectedWantToSee = true
                }
            }
        }
        binding.alreadyViewed.setOnClickListener {
            if (FlagAndObject.isButtonSelectedAlreadyViewed) {
                lifecycle.coroutineScope.launch {
                    collectionsAlreadyViewedViewModel.removeFilmById(arg.movieDetailInfo.kinopoiskId)
                    binding.alreadyViewed.setImageResource(R.drawable.eye_closed)
                    FlagAndObject.isButtonSelectedAlreadyViewed = false
                }
            } else {
                lifecycle.coroutineScope.launch {
                    var viewedFilm =
                        FlagAndObject.requestFilmAlreadyViewed.apply { this.viewed = true }
                    collectionsAlreadyViewedViewModel.addAlreadyViewed(viewedFilm)
                    binding.alreadyViewed.setImageResource(R.drawable.eye)
                    FlagAndObject.isButtonSelectedAlreadyViewed = true
                }
            }
        }
        binding.share.setOnClickListener {
            sendUrlFilm(FlagAndObject.requestFilm.imdbId.toString())
        }
        binding.zoom.setOnClickListener {
            binding.poster.updateLayoutParams { height = 520 }
            binding.zoom.visibility = View.GONE
        }
//        binding.poster.setOnClickListener {
//            // binding.headLevel.background= forAddProfile
//            Picasso.with(requireContext())
//                .load(forAddProfile.posterUrlPreview)
//                .into(binding.poster)
//
//        }
        viewLifecycleOwner.lifecycleScope
            .launch {
                viewModelByFilmInfoDetail.getFilmDetailInfo(
                    getFilmId(arg)
                )
                viewModelByFilmInfoDetail.movies
                    .onEach {
                        filmPosterUrl = it.posterUrlPreview.toString()
                        filmDetailInfo(it)
                        filmId = it.kinopoiskId
                        if(arg.movieDetailInfo.posterUrl.toString().isNotEmpty()) {
                            setPoster(arg.movieDetailInfo.posterUrl.toString())
                        }
                        else {
                            setPoster(arg.movieDetailInfo.posterUrl.toString())
                        }
                        forAddProfile = it
                        FlagAndObject.requestFilm = modificationsToRoom(it)
                        FlagAndObject.requestFilmAlreadyViewed =
                            modificationsToRoomAlreadyViewed(it)
                        FlagAndObject.requestFilmWantToSee = modificationsToRoomWantToSee(it)
                        idKinopoisk = modificationsToRoom(it).kinopoiskId
                    }
                    .launchIn(this)
            }
        binding.additionalMenu.setOnClickListener {
//            filmPosterUrl= arg.movieDetailInfo.posterUrl.toString()

            val settingsDialogFragment = SettingsDialogFragment()
            val args = Bundle()
            args.putInt("key", arg.movieDetailInfo.kinopoiskId)
            args.putInt("key2", arg.movieDetailInfo.filmId)
            args.putInt("key3", similarFilmId)
            args.putString("key4",arg.movieDetailInfo.premiereRu)
//            settingsDialogFragment.arguments=args
//            settingsDialogFragment.show(parentFragmentManager,SettingsDialogFragment.TAG);
            findNavController().navigate(R.id.settingsDialogFragment, args)
        }

    }

    fun staffToInterestedStaffs(item: StaffStarred): InterestedStaffEntity {
        return InterestedStaffEntity(
            item.staffId,
            item.nameRu,
            item.posterUrl,
            item.professionText,
            item.professionKey

        )
    }

    private fun lookSimilarFilm(similarId: Int?) {

        viewLifecycleOwner.lifecycleScope
            .launch {

                callTheSimilarFlag = true
                if (similarId != null) {
                    viewModelByFilmInfoDetail.getFilmDetailInfo(
                        similarId
                    )
                    similarFilmId = similarId

                    staffStarredViewModel.refreshFlow()

                    viewLifecycleOwner.lifecycleScope.launch {

                        staffStarredViewModel.staffStarred
                            .onEach { similar ->
                                staffStarredAdapter.submitData(similar)
                            }
                            .launchIn(this)
                    }
                    viewLifecycleOwner.lifecycleScope.launch {

                        staffStarredViewModel.staffStarred
                            .onEach { staff ->
                                workedOnFilmAdapter.submitData(staff)
                            }
                            .launchIn(this)
                    }

                }

                viewModelByFilmInfoDetail.movies
                    .onEach {
                        filmDetailInfo(it)
                        setPoster(it.posterUrlPreview.toString())
                        forAddProfile = it
                        FlagAndObject.requestFilm = modificationsToRoom(it)
                        FlagAndObject.requestFilmAlreadyViewed =
                            modificationsToRoomAlreadyViewed(it)
                        FlagAndObject.requestFilmWantToSee = modificationsToRoomWantToSee(it)
                        idKinopoisk = modificationsToRoom(it).kinopoiskId
                    }
                    .launchIn(this)
            }
        viewLifecycleOwner.lifecycleScope.launch {

            if (similarId != null) {
                imageViewModel.getImages(similarId, "STILL", 1)
            } else {
                imageViewModel.getImages(idKinopoisk, "STILL", 1)
            }
            imageViewModel.images
                .onEach { images ->
                    imageAdapter.submitList(images?.items)
                    if (images != null) {
                        if (images.items.size > 10) {
                            binding.allGalleryList.visibility = View.VISIBLE
                            binding.allGalleryList.text = images.items.size.toString()
                        }
                    }
                }
                .launchIn(this)

        }
        viewLifecycleOwner.lifecycleScope.launch {
            if (similarId != null) {
                similarViewModel.getSimilar(similarId)
            }
            similarViewModel.similar
                .onEach { similar ->
                    similarAdapter.submitList(similar?.items)
                    binding.amountRelatedFilm.text = similar?.items?.size.toString()
                }
                .launchIn(this)
        }

    }

    fun getFilmId(argFilm: MovieDetailFragmentArgs): Int {
        if (argFilm.movieDetailInfo.kinopoiskId == 0)
            return argFilm.movieDetailInfo.filmId
        else
            return argFilm.movieDetailInfo.kinopoiskId

    }

    fun sendUrlFilm(filmId: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(
                Intent.EXTRA_TEXT,
                "https://www.imdb.com/title/" + "${filmId}" + "/"
            )
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Ссылка на фильм"
            )
        }.also { intent ->
            val chooserIntent =
                Intent.createChooser(intent, "Отправка url фильма для просмотра...")
            startActivity(chooserIntent)
        }
    }

    fun modificationsToRoom(detInfo: FilmDetailInfo): FavouritesEntity {

        return FavouritesEntity(
            detInfo.kinopoiskId,
            detInfo.imdbId,
            detInfo.nameRu,
            detInfo.posterUrlPreview,
            detInfo.genres?.get(0)?.genre.toString(),
            detInfo.nameOriginal ?: "Film",
            detInfo.countries?.get(0)?.country.toString(),
            detInfo.ratingImdb,
            true
        )
    }

    fun modificationsToRoomWantToSee(detInfo: FilmDetailInfo): WantToSeeEntity {

        return WantToSeeEntity(
            detInfo.kinopoiskId,
            detInfo.imdbId,
            detInfo.nameRu,
            detInfo.posterUrlPreview,
            detInfo.genres?.get(0)?.genre.toString(),
            detInfo.nameOriginal ?: "Film",
            detInfo.countries?.get(0)?.country.toString(),
            detInfo.ratingImdb,
            true
        )
    }

    fun modificationsToRoomAlreadyViewed(detInfo: FilmDetailInfo): AlreadyViewedEntity {

        return AlreadyViewedEntity(
            detInfo.kinopoiskId,
            detInfo.imdbId,
            detInfo.nameRu,
            detInfo.posterUrlPreview,
            detInfo.genres?.joinToString(",") { it.genre.toString() } ?: "",
            detInfo.nameOriginal ?: "Film",
            detInfo.countries?.get(0)?.country.toString(),
            detInfo.ratingImdb,
            false
        )
    }


    fun setPoster(posterUrl: String) {
        Picasso.with(requireContext())
            .load(posterUrl)
            .into(binding.poster)
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        _binding = null
//        binding.starredInFilmListRecycler.adapter=null
//        binding.workedOnTheFilm.adapter=null
//        binding.galleryList.adapter=null
//        binding.relatedFilmList.adapter=null
    }

    private fun filmDetailInfo(message: FilmDetailInfo) {
        with(binding) {
            binding.serial.visibility = if (message.serial) View.VISIBLE else View.INVISIBLE
            namePremiereFilm.text = message.nameRu
            if (message.ratingImdb != null) {
                ratingPremiereFilm.text = message.ratingImdb + " "
            }
            originalNamePremiereFilm.text = message.nameOriginal?.toString() ?: ""
            yearPremiereFilm.text = message.year.toString() + ", "
            //    genreNamePremiereFilm.text = message.genres?.joinToString(",") { it.genre.toString() }
            genreNamePremiereFilm.text = message.genres?.get(0)?.genre.toString()

            countryPremiereFilm.text =
                message.countries?.get(0)?.country.toString() + ","
            if (message.ratingAgeLimits == null) {
                agePremiereFilm.text = "_"
            } else {
                if (message.ratingAgeLimits.length > 3) {
                    agePremiereFilm.text = message.ratingAgeLimits.substring(3) + "+"
                } else {
                    agePremiereFilm.text = message.ratingAgeLimits
                }
            }
            binding.seasonAll.visibility = if (message.serial) {
                View.VISIBLE.also {
                    binding.genreNamePremiereFilm.text = message.genres?.get(0)?.genre + ","
                }
            } else View.INVISIBLE
            var hours = message.filmLength / 60
            var minuties: Int = message.filmLength % 60
            if (hours < 1) {
                timePremiereFilm.text = "$minuties мин,"
            } else timePremiereFilm.text = "$hours ч $minuties мин,"
            expandTexView.setText(message.description)
        }
    }

    object FlagAndObject {
        var filmPosterUrl = ""
        var isButtonSelectedFavourite = false
        var isButtonSelectedWantToSee = false
        var isButtonSelectedAlreadyViewed = false
        var callTheSimilarFlag = false
        var similarFilmId = 0
        var filmGlobalUrlInfo: FilmNetSource? = null
        lateinit var forAddProfile: FilmDetailInfo
        lateinit var requestFilm: FavouritesEntity
        lateinit var requestFilmWantToSee: WantToSeeEntity
        lateinit var requestFilmAlreadyViewed: AlreadyViewedEntity
        lateinit var filmForStaffStarredDownLoad: Movie
        lateinit var staffStarredList: List<StaffStarred>
        lateinit var workedOnFilmList: List<StaffStarred>
    }
}



