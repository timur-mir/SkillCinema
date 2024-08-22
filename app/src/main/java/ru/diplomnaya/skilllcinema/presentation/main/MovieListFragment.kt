package ru.diplomnaya.skilllcinema.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.FilmsListBoardBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsAlreadyViewedViewModel
import ru.diplomnaya.skilllcinema.presentation.main.AnyCountriesAndGenresFilmsViewModel
import ru.diplomnaya.skilllcinema.presentation.main.AnyCountriesAndGenresFilmsViewModelSecondVariant
import ru.diplomnaya.skilllcinema.presentation.main.MainActivity
import ru.diplomnaya.skilllcinema.presentation.main.PremieresListViewModel
import ru.diplomnaya.skilllcinema.presentation.main.SplashActivity
import ru.diplomnaya.skilllcinema.presentation.main.SplashActivity.Companion.audioFon
import ru.diplomnaya.skilllcinema.presentation.main.Top250ListViewModel
import ru.diplomnaya.skilllcinema.presentation.main.TopAwaitViewModel
import ru.diplomnaya.skilllcinema.presentation.main.Tv_SeriesViewModel
import ru.diplomnaya.skilllcinema.presentation.profile.InterestedFilmsViewModel
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.AnyCountriesAndGenresListAdapterSecondVariant
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.AnyCountriesAndGenresTv_SeriesAdapter
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.FilmGenreAndCountrySecondVariant
import ru.diplomnaya.skilllcinema.view.Movies.movieList
import ru.diplomnaya.skilllcinema.view.Movies.top250MovieList
import ru.diplomnaya.skilllcinema.view.Movies.topAny1MovieList
import ru.diplomnaya.skilllcinema.view.Movies.topAny2MovieList
import ru.diplomnaya.skilllcinema.view.Movies.topAwaitMovieList
import ru.diplomnaya.skilllcinema.view.Movies.tv_SeriesMovieList
import kotlin.random.Random


class MovieListFragment : Fragment() {
    private var _binding: FilmsListBoardBinding? = null
    val binding get() = _binding!!

    private val viewModel by viewModels<PremieresListViewModel>()
    private val movieAdapter = PremieresAdapter { movie -> onItemClickOnListPremieres(movie) }

    private val viewModelTopAwait by viewModels<TopAwaitViewModel>()
    private val movieAdapterTopAwait =
        TopAwaitListAdapter { movie -> onItemClickOnListPremieres(movie) }

    private val viewModelTop250 by viewModels<Top250ListViewModel>()
    private val movieAdapterTop250 =
        Top250ListAdapter { movie -> onItemClickOnListPremieres(movie) }

    private val viewModelAnyFilms by viewModels<AnyCountriesAndGenresFilmsViewModel>()
    private val anyCountriesAndGenresListAdapter =
        AnyCountriesAndGenresListAdapter { movie -> onItemClickOnListPremieres(movie) }

    private val viewModelAnyFilmsSecondVariant by viewModels<AnyCountriesAndGenresFilmsViewModelSecondVariant>()
    private val anyCountriesAndGenresListAdapterSecondVariant =
        AnyCountriesAndGenresListAdapterSecondVariant { movie -> onItemClickOnListPremieres(movie) }

    private val tv_SeriesViewModel by viewModels<Tv_SeriesViewModel>()
    private val tv_SeriesAdapter =
        AnyCountriesAndGenresTv_SeriesAdapter { movie -> onItemClickOnListPremieres(movie) }
    private val collectionAlreadyViewedViewModel by viewModels<CollectionsAlreadyViewedViewModel>()
    private val interestedFilmsViewModel by viewModels<InterestedFilmsViewModel>()


    var audioFon: MediaPlayer? = null

    lateinit var genres: String
    lateinit var countries: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FilmsListBoardBinding.inflate(inflater)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.exit_programm -> {
                audioFon?.apply {
                    pause()
                    reset()
                    release()
                }.also { audioFon = null }
                val closeWarnings = AlertDialog.Builder(requireContext())
                closeWarnings.setTitle("Закрытие приложения")
                closeWarnings.setMessage("Программа поиска фильмов завершает свою работу")
                val progressBar = ProgressBar(requireContext())
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                progressBar.layoutParams = lp
                closeWarnings.setView(progressBar)
                closeWarnings.show()
                Handler().postDelayed(Runnable {
                    activity?.finish()
                    System.exit(1)
                    // exitProcess(0)
                }, 1000)
                true
            }

            R.id.about_program -> {
                val intent = Intent(requireContext(), SplashActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                true
            }

            else -> return super.onOptionsItemSelected(item)

        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainHeaderFilmLists.setSingleLine()
        binding.mainHeaderFilmLists.ellipsize = TextUtils.TruncateAt.MARQUEE
        binding.mainHeaderFilmLists.marqueeRepeatLimit = -1
        binding.mainHeaderFilmLists.isSelected = true
        setHasOptionsMenu(true)


        with(binding.moviesListRecycler) {
            adapter = movieAdapter
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        with(binding.topAwaitListRecycler) {
            adapter = movieAdapterTopAwait
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        with(binding.top250ListRecycler) {
            adapter = movieAdapterTop250
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        with(binding.topAnyFilmsListRecycler) {
            adapter = anyCountriesAndGenresListAdapter
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        with(binding.topAnyFilmsListRecyclerSecondVariant) {
            adapter = anyCountriesAndGenresListAdapterSecondVariant
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        with(binding.anyCountriesAndGenresTVSERIESRecycler) {
            adapter = tv_SeriesAdapter
            setHasFixedSize(true)
            addItemDecoration(ItemOffsetDecoration(requireContext()))
        }
        var switchState = true
        binding.switchFonMus.setOnClickListener {
            if (switchState) {
                audioFon?.pause()
                binding.switchFonMus.setImageResource(R.drawable.volume_off)
                switchState = false
            } else {
                binding.switchFonMus.setImageResource(R.drawable.volume_down)
                audioFon?.start()
                switchState = true
            }
        }
        viewModel.movies.onEach { movies ->
            if (movies.size > 20) {
                binding.premieresAll.visibility = View.VISIBLE
                movieList = movies
            }
            movieAdapter.submitList(movies)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.premieresAll.setOnClickListener {
            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    movieList.toTypedArray()
                )
            findNavController().navigate(action)
        }

        viewModelTopAwait.pageMovies.onEach { pmovies ->

            movieAdapterTopAwait.submitData(pmovies)

        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapterTopAwait.loadStateFlow.collect {
                if (movieAdapterTopAwait.itemCount >= 20) {
                    topAwaitMovieList = movieAdapterTopAwait.snapshot().items
                    binding.topAwaitAll.visibility = View.VISIBLE
                }
            }
        }

        binding.topAwaitAll.setOnClickListener {

            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    topAwaitMovieList.toTypedArray()
                )
            findNavController().navigate(action)
        }


        viewModelTop250.page250Movies.onEach {
            movieAdapterTop250.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            movieAdapterTop250.loadStateFlow.collect {
                if (movieAdapterTop250.itemCount >= 20) {
                    top250MovieList = movieAdapterTop250.snapshot().items
                    binding.top250All.visibility = View.VISIBLE
                }
            }
        }
        binding.top250All.setOnClickListener {

            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    top250MovieList.toTypedArray()
                )
            findNavController().navigate(action)

        }

        viewModelAnyFilms.randomFilmsWithRandomGenres.onEach {
            anyCountriesAndGenresListAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            anyCountriesAndGenresListAdapter.loadStateFlow.collect {
                if (anyCountriesAndGenresListAdapter.itemCount >= 20) {
                    topAny1MovieList = anyCountriesAndGenresListAdapter.snapshot().items
                    binding.topAnyFilmsAll.visibility = View.VISIBLE
                }
            }
        }
        binding.TVSERIESAll.setOnClickListener {

            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    tv_SeriesMovieList.toTypedArray()
                )
            findNavController().navigate(action)

        }
        binding.refreshSeries.setOnClickListener {
            Snackbar.make(binding.root, "Обновление сериалов", Snackbar.LENGTH_INDEFINITE)
                .setActionTextColor(Color.WHITE)
                .setBackgroundTint((Color.BLUE))
                .setAction("Перезапустить") {
                    tv_SeriesViewModel.refresh()
                    tv_SeriesViewModel.tv_Series?.onEach {
                        tv_SeriesAdapter.submitData(it)
                    }?.launchIn(viewLifecycleOwner.lifecycleScope)

                    viewLifecycleOwner.lifecycleScope.launch {
                        delay(1000)
                        tv_SeriesAdapter.loadStateFlow.collect {
                            if (tv_SeriesAdapter.itemCount >= 20) {
                                tv_SeriesMovieList = tv_SeriesAdapter.snapshot().items
                                binding.TVSERIESAll.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                .show()


        }
        tv_SeriesViewModel.tv_Series?.onEach {
            tv_SeriesAdapter.submitData(it)
        }?.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            tv_SeriesAdapter.loadStateFlow.collect {
                if (tv_SeriesAdapter.itemCount >= 20) {
                    tv_SeriesMovieList = tv_SeriesAdapter.snapshot().items
                    binding.TVSERIESAll.visibility = View.VISIBLE
                }
            }
        }

        binding.topAnyFilmsAll.setOnClickListener {

            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    topAny1MovieList.toTypedArray()
                )
            findNavController().navigate(action)

        }


        viewModelAnyFilmsSecondVariant.randomFilmsWithRandomGenresSecondVariant.onEach {
            anyCountriesAndGenresListAdapterSecondVariant.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        viewLifecycleOwner.lifecycleScope.launch {
            anyCountriesAndGenresListAdapterSecondVariant.loadStateFlow.collect {
                if (anyCountriesAndGenresListAdapterSecondVariant.itemCount >= 20) {
                    topAny2MovieList =
                        anyCountriesAndGenresListAdapterSecondVariant.snapshot().items
                    binding.topAnyFilmsAllSecondVariant.visibility = View.VISIBLE
                }
            }
        }
        binding.topAnyFilmsAllSecondVariant.setOnClickListener {

            val action =
                MovieListFragmentDirections.actionMovieListFragment2ToPremieresFullListFragment(
                    topAny2MovieList.toTypedArray()
                )
            findNavController().navigate(action)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3200)
            updateCountryGenreName()
        }
        updateCountryGenreName()


        viewModelAnyFilmsSecondVariant.randomFilmsWithRandomGenresSecondVariant.onEach {
            anyCountriesAndGenresListAdapterSecondVariant.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        val callbackExit = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish();
                System.exit(0);

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callbackExit)

    }

    override fun onResume() {
        super.onResume()
        audioFon = MediaPlayer.create(App.appContext, R.raw.fon_elektr)
        audioFon?.start()
        audioFon?.isLooping = true
    }

    private fun updateCountryGenreName() {
        binding.randomGenreTitle.text =
            "${FilmGenreAndCountry.genreName.toString()}   ${FilmGenreAndCountry.countryName.toString()}"

        binding.randomGenreTitleSecondVariant.text =
            "${FilmGenreAndCountrySecondVariant.genreNameSecondVariant.toString()}   ${FilmGenreAndCountrySecondVariant.countryNameSecondVariant.toString()}"
    }


    object randFilm {
        val country = Random.nextInt(1, 240)
        val genre = Random.nextInt(1, 33)
        val country_second_variant = Random.nextInt(1, 240)
        val genre_second_variant = Random.nextInt(1, 33)
    }

    fun onItemClickOnListPremieres(item: Movie) {
        interestedFilmsViewModel.saveInterestedFilm(movieToInterestedFilms(item))
        val action =
            MovieListFragmentDirections.actionMovieListFragment2ToMovieDetailFragment(item)

        findNavController().navigate(action)

    }

    fun movieToInterestedFilms(item: Movie): InterestedFilmsEntity {
        return InterestedFilmsEntity(
            item.kinopoiskId,
            item.filmId,
            item.nameRu ?: "фильм..",
            item.rating,
            item.posterUrl,
            item.genres?.joinToString(",") { it.genre.toString() }
        )
    }

    override fun onStop() {
        super.onStop()
//        audioFon.pause()
//        audioFon.reset()
//        audioFon.release()
//        audioFon = null
    }

    override fun onPause() {
        super.onPause()
        audioFon?.pause()
        audioFon?.reset()
        audioFon?.release()
        audioFon = null
    }

    override fun onDestroy() {
        super.onDestroy()
        super.onPause()
        audioFon?.pause()
        audioFon?.reset()
        audioFon?.release()
        audioFon = null
////        _binding=null
//        binding.moviesListRecycler.adapter=null
//        binding.topAwaitListRecycler.adapter=null
//        binding.top250ListRecycler.adapter=null
//        binding.topAnyFilmsListRecycler.adapter=null
//        binding.anyCountriesAndGenresTVSERIESRecycler.adapter=null
//        binding.topAnyFilmsListRecyclerSecondVariant.adapter=null
    }

    override fun onDestroyView() {
        super.onDestroyView()
//        audioFon.pause()
//        audioFon.reset()
//        audioFon.release()
//        audioFon = null
    }

}

object Movies {
    lateinit var movieList: List<Movie>
    lateinit var topAwaitMovieList: List<Movie>
    lateinit var top250MovieList: List<Movie>
    lateinit var topAny1MovieList: List<Movie>
    lateinit var topAny2MovieList: List<Movie>
    lateinit var tv_SeriesMovieList: List<Movie>
}




