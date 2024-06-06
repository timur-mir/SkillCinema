package ru.diplomnaya.skilllcinema.presentation.profile

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.FragmentProfileBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.Collections
import ru.diplomnaya.skilllcinema.model.database.Country
import ru.diplomnaya.skilllcinema.model.database.Genre
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.CustomViewCollection
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsAlreadyViewedViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsFavouritesFilmsViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsWantToSeeViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.main.PremieresListViewModel
import ru.diplomnaya.skilllcinema.presentation.myCollection.CollectionsViewModel
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.interestedFilmsList
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.interestedStaffList

import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.AlreadyViewedAdapterForConcatAdapter
import ru.diplomnaya.skilllcinema.view.MovieListFragmentDirections
import ru.diplomnaya.skilllcinema.view.Movies

import kotlin.properties.Delegates

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    val binding get() = _binding!!
    private val viewModel by viewModels<PremieresListViewModel>()
    lateinit var adapter: ConcatAdapter
    private val favouritesFilmsViewModel_Profile by viewModels<CollectionsFavouritesFilmsViewModel>()
    private val collectionsWantTooSeeViewModel_Profile by viewModels<CollectionsWantToSeeViewModel>()
    private val collectionsAlreadyViewedViewMode_Profile by viewModels<CollectionsAlreadyViewedViewModel>()
    private val viewModelByFilmInfoDetail_Profile by viewModels<GetFilmDetailInfoViewModel>()
    private val collectionsViewModel by viewModels<CollectionsViewModel>()
    private val interestedFilmsViewModelProfile by viewModels<InterestedFilmsViewModel>()
    private val interestedStaffsViewModelProfile by viewModels<InterestedStaffsViewModel>()


    var alreadyViewedFilmAdapter = AlreadyViewedAdapterForConcatAdapter({ viewedFilms ->
        onItemClickOnListPremieres(modificationsToLook(viewedFilms))
    },
        { deleteAllViewedFilms -> deleteViewedFilms(deleteAllViewedFilms) })
    lateinit var interestedFilmsAdapter: InterestedFilmsAdapter
    lateinit var interestedStaffAdapter: InterestedStaffsAdapter
    lateinit var clearHistoryInterestedFilmsAdapter: ClearHistoryInterestedFilmsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.coroutineScope.launch {
            collectionsAlreadyViewedViewMode_Profile.getAllAlreadyViewedFilms()
            favouritesFilmsViewModel_Profile.getAllFavouritesFilms()
            collectionsViewModel.getAllCollection()
            collectionsWantTooSeeViewModel_Profile.getAllWantToSee()
            interestedFilmsViewModelProfile.getInterestedFilms()
            interestedStaffsViewModelProfile.getInterestedStaffs()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            binding.favouriteTile.setAmountFilmInCollection(
                favouritesFilmsViewModel_Profile.getCollectionsSize().toString()
            )
            binding.wantToSee.setAmountFilmInCollection(
                collectionsWantTooSeeViewModel_Profile.getCollectionsSize().toString()
            )
        }

        binding.favouriteTile.setTextForNameCollection("Любимые")
        binding.favouriteTile.setImageCollection(R.drawable.heart)

        binding.favouriteTile.setDeleteCollectionButtonVisibility()
        binding.wantToSee.setTextForNameCollection("Хочу посмотреть")
        binding.wantToSee.setImageCollection(R.drawable.bookmark)

        binding.wantToSee.setDeleteCollectionButtonVisibility()


        binding.wantToSee.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(binding.wantToSee.getNameCollection())
            findNavController().navigate(action)
        }



        binding.favouriteTile.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                    "Любимые"
                )
            findNavController().navigate(action)

        }

        binding.createNewCollectionSecondUse.setOnClickListener {
            val liSecondUse = LayoutInflater.from(requireActivity())
            val viewDialogSecondUse = liSecondUse.inflate(R.layout.add_collection_dialog, null)
            val textFieldCollectionSecondUse =
                viewDialogSecondUse.findViewById<View>(R.id.name_collection) as EditText
            val closeButtonSecondUse =
                viewDialogSecondUse.findViewById<View>(R.id.cancelbtn) as ImageButton
            val saveCollection =
                viewDialogSecondUse.findViewById<View>(R.id.save_collection) as Button
            val dialog = AlertDialog.Builder(requireActivity())
                .setView(viewDialogSecondUse)
                .show()
            saveCollection.setOnClickListener {
                collectionsViewModel.addNewCollection(
                    Collections(
                        0, textFieldCollectionSecondUse.text.toString()
                    )
                )
                dialog.dismiss()
                val action =
                    ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                        textFieldCollectionSecondUse.text.toString()
                    )
                findNavController().navigate(action)

            }
            closeButtonSecondUse.setOnClickListener {
                dialog.dismiss()
            }

        }
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.getAllCollection()
        }

        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->

            var idNumber = 1
            if (list.size == 0) {
//                Toast.makeText(
//                    requireContext(), "У Вас нету пока своих коллекций", Toast.LENGTH_LONG
//                ).show()
            } else {
                if (list.size != 0 && !Profile.itemAddFlag_Profile) {
                    var nameElement = mutableListOf<String>()
                    var sizeFilmsInCollection = mutableListOf<String>()
                    for (innerList in list) {
                        nameElement.add(innerList.collections.CollectionName)
                        sizeFilmsInCollection.add(innerList.itemCollectionsList.size.toString())
                        if ((nameElement.size == list.size)) {

                            createViewCollectionElement(
                                nameElement, sizeFilmsInCollection
                            )
                        }

                    }
                } else {
                    // Toast.makeText(requireContext(), "Флаг", Toast.LENGTH_LONG).show()
                }
            }

        }


//Уровень "Просмотренно"

        val layoutManger =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.moviesViewedList.layoutManager = layoutManger
        collectionsAlreadyViewedViewMode_Profile.viewedFilmsLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {

                with(binding.moviesViewedList) {
                    alreadyViewedFilmAdapter.submitList(list)
                    val listOfAdapters = listOf(alreadyViewedFilmAdapter)
                    adapter = ConcatAdapter(listOfAdapters)
                    addItemDecoration(ItemOffsetDecoration(requireContext()))
                }
                binding.viewedAll.text = "${list.size.toString()} >"
                binding.viewedAll.setOnClickListener {
                    var historyList = ArrayList<Movie>()
                    list.forEach { historyList += arrayListOf<Movie>(modificationsToLook(it)) }
                    val action =
                        ProfileFragmentDirections.actionProfileFragment2ToPremieresFullListFragment(
                            historyList.toTypedArray()
                        )
                    findNavController().navigate(action)
                }
            }
        }

        //Уровень "Вам было интересно"
        val layoutMangerInterestedObject =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.interestingFilms.layoutManager = layoutMangerInterestedObject
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1000)
            interestedFilmsViewModelProfile.interestedFilmsLiveData.observe(viewLifecycleOwner) { list ->
                interestedFilmsList = list
            }
            interestedStaffsViewModelProfile.interestedStaffsLiveData.observe(viewLifecycleOwner) { list ->
                interestedStaffList = list
            }
            if (interestedFilmsList.isNotEmpty() && interestedStaffList.isNotEmpty()) {
                binding.viewedAllInteresting.text =
                    "${interestedFilmsList.size + interestedStaffList.size} >"

                interestedFilmsAdapter =

                    InterestedFilmsAdapter(
                        interestedFilmsList
                    ) { returnValue ->
                        onItemClickOnListPremieres(
                            modificationsToSmallMovieInfo(returnValue)
                        )
                    }
                interestedStaffAdapter =
                    InterestedStaffsAdapter(interestedStaffList) { returnValue ->
                        onItemClickOnListStaffs(modificationsToSmallStaffInfo(returnValue))
                    }
                clearHistoryInterestedFilmsAdapter =
                    ClearHistoryInterestedFilmsAdapter { clickEvent -> clearHistoryInterestingObject() }
                val config = ConcatAdapter.Config.Builder().setIsolateViewTypes(true).build()
                binding.interestingFilms.adapter = ConcatAdapter(
                    config,
                    interestedFilmsAdapter,
                    interestedStaffAdapter,
                    clearHistoryInterestedFilmsAdapter
                )
                binding.interestingFilms.addItemDecoration(ItemOffsetDecoration(requireContext()))
            }
        }

    }

    private fun onItemClickOnListStaffs(item: StaffStarred) {
        val action =
            ProfileFragmentDirections.actionProfileFragment2ToAboutActorFragment(item)

        findNavController().navigate(action)
    }


    private fun clearHistoryInterestingObject() {
        viewLifecycleOwner.lifecycleScope.launch {
//            Toast.makeText(
//                requireContext(),
//                "Удаление коллекции интересовавших Вас...",
//                Toast.LENGTH_SHORT
//            ).show()
            interestedFilmsViewModelProfile.removeAllInterestedFilms()
            interestedStaffsViewModelProfile.removeAllInterestedStaffs()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Profile.itemAddFlag_Profile = false
        _binding = null
    }

    fun deleteViewedFilms(start: Boolean) {
        lifecycle.coroutineScope.launch {
            if (start) {
                collectionsAlreadyViewedViewMode_Profile.removeAllViewedFilms()
            }
        }
    }


    fun onItemClickOnListPremieres(item: Movie) {
        val action =
            ProfileFragmentDirections.actionProfileFragment2ToMovieDetailFragment(item)

        findNavController().navigate(action)
    }

    fun modificationsToSmallMovieInfo(detInfo: InterestedFilmsEntity): Movie {
        return Movie(
            detInfo.kinopoiskId!!,
            detInfo.nameRu,
            detInfo.posterUrl,
            detInfo.posterUrl,
            java.util.ArrayList(),
            "",
            java.util.ArrayList(),
            detInfo.rating,
            false,
            detInfo.filmId!!

        )
    }

    fun modificationsToSmallStaffInfo(detInfo: InterestedStaffEntity): StaffStarred {
        return StaffStarred(
            detInfo.staffId!!,
            detInfo.nameRu ?: "Участник",
            "Staff",
            "Участвовавший в фильме",
            detInfo.posterUrl ?: "",
            detInfo.professionText ?: "Участник в сьёмках",
            detInfo.professionKey ?: "Участник в сьёмках"
        )
    }


    fun modificationsToLook(detInfo: AlreadyViewedEntity): Movie {
        val strSeparator = ","
        val objectArray =
            detInfo.genres.split(strSeparator.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val objects: ArrayList<ru.diplomnaya.skilllcinema.model.Genre> =
            ArrayList<ru.diplomnaya.skilllcinema.model.Genre>()
        for (string in objectArray) {
            if (objects != null) {
                objects.add(ru.diplomnaya.skilllcinema.model.Genre(string))
            }
        }
        val objectArray2 =
            detInfo.countries.split(strSeparator.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
        val objects2: ArrayList<ru.diplomnaya.skilllcinema.model.Country> =
            ArrayList<ru.diplomnaya.skilllcinema.model.Country>()
        for (string in objectArray2) {
            objects2.add(ru.diplomnaya.skilllcinema.model.Country(string))
        }
        return Movie(
            detInfo.kinopoiskId,
            detInfo.nameRu,
            detInfo.posterUrlPreview,
            detInfo.posterUrlPreview,
            objects as ArrayList<ru.diplomnaya.skilllcinema.model.Genre>,
            detInfo.nameRu,
            objects2 as ArrayList<ru.diplomnaya.skilllcinema.model.Country>,
            detInfo.ratingImdb,
            true,
            1
        )
    }

    fun createViewCollectionElement(
        nameCollection: MutableList<String>,
        sizeCollection: MutableList<String>
    ) {
        var flagOfRepeat = true
        var addLayoutsSize = 1
        var colFirstInSlice = 0
        var colSecondInSlice = 1
        val layoutCollection = binding.collectionLayout

        do {
            viewLifecycleOwner.lifecycleScope.launch {
                collectionsViewModel.getAllCollection()
            }

            val last_item_price_collection =
                layoutCollection.getChildAt(layoutCollection.childCount - 1)

            val rowNewCollection = LinearLayout(requireContext())

            rowNewCollection.orientation = LinearLayout.HORIZONTAL

            val params = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            params.leftMargin = 41
            params.topMargin = 22

            if (last_item_price_collection != null) {
                params.addRule(RelativeLayout.BELOW, last_item_price_collection.id)
            }
            rowNewCollection.layoutParams = params
            rowNewCollection.id = addLayoutsSize


            if (nameCollection.getOrNull(colFirstInSlice) != null && nameCollection.getOrNull(
                    colSecondInSlice
                ) != null
            ) {
                val paramsCard = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                paramsCard.leftMargin = 24

                val cardView1 = CustomViewCollection(requireContext(), null)
                cardView1.setTextForNameCollection(nameCollection.get(colFirstInSlice))
                cardView1.id = colFirstInSlice + 1
                cardView1.transitionName = nameCollection[colFirstInSlice]
                val rv = cardView1.rootView
                val lookCollection = rv.findViewById<AppCompatButton>(R.id.button_view_collection)
                lookCollection.text = sizeCollection[colFirstInSlice]
                lookCollection.setOnClickListener {
                    val action =
                        ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                            nameCollection[(cardView1.id) - 1].toString()
                        )
                    findNavController().navigate(action)

                }
                val delButton = rv.findViewById<ImageButton>(R.id.delete_collection)
                delButton.setOnClickListener(View.OnClickListener { view ->
//                    Toast.makeText(
//                        requireContext(),
//                        "Работает удаление коллекции ${cardView1.id}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    viewLifecycleOwner.lifecycleScope.launch {
                        deleteCollection(cardView1.getNameCollection())
                    }

                })


                val cardView2 = CustomViewCollection(requireContext(), null)
                cardView2.setTextForNameCollection(nameCollection[colSecondInSlice])
                cardView2.layoutParams = paramsCard
                cardView2.id = colSecondInSlice + 1
                cardView2.transitionName = nameCollection[colSecondInSlice]
                val rv2 = cardView2.rootView
                val lookCollection2 = rv2.findViewById<AppCompatButton>(R.id.button_view_collection)
                lookCollection2.text = sizeCollection[colSecondInSlice]
                lookCollection2.setOnClickListener {
                    val action =
                        ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                            nameCollection[(cardView2.id) - 1].toString()
                        )
                    findNavController().navigate(action)

                }
                val delButton2 = rv2.findViewById<ImageButton>(R.id.delete_collection)
                delButton2.setOnClickListener(View.OnClickListener { view ->
//                    Toast.makeText(
//                        requireContext(),
//                        "Работает удаление коллекции ${cardView2.id}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    viewLifecycleOwner.lifecycleScope.launch {
                        deleteCollection(cardView2.getNameCollection())
                    }
                })


                rowNewCollection.addView(cardView1); rowNewCollection.addView(cardView2)
                layoutCollection.addView(rowNewCollection, params)
                addLayoutsSize += 1
                colFirstInSlice += 2
                colSecondInSlice += 2
                Profile.itemAddFlag_Profile = true
                if (nameCollection.getOrNull(colFirstInSlice) == null) {
                    flagOfRepeat = false
                }

            } else {

                if (nameCollection.getOrNull(colFirstInSlice) != null && nameCollection.getOrNull(
                        colSecondInSlice
                    ) == null
                ) {
                    val cardView3 = CustomViewCollection(requireContext(), null)
                    cardView3.setTextForNameCollection(nameCollection.get(colFirstInSlice))
                    cardView3.id = colFirstInSlice + 1
                    cardView3.transitionName = nameCollection[colFirstInSlice]

                    val rv3 = cardView3.rootView
                    val lookCollection3 =
                        rv3.findViewById<AppCompatButton>(R.id.button_view_collection)
                    lookCollection3.text = sizeCollection[colFirstInSlice]
                    lookCollection3.setOnClickListener {
                        val action =
                            ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                                nameCollection[(cardView3.id) - 1].toString()
                            )
                        findNavController().navigate(action)

                    }
                    val delButton3 = rv3.findViewById<ImageButton>(R.id.delete_collection)
                    delButton3.setOnClickListener(View.OnClickListener { view ->
//                        Toast.makeText(
//                            requireContext(),
//                            "Работает удаление коллекции ${cardView3.id}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        viewLifecycleOwner.lifecycleScope.launch {
                            deleteCollection(cardView3.getNameCollection())
                        }
                    })

                    rowNewCollection.addView(cardView3)
                    layoutCollection.addView(rowNewCollection, params)
                    flagOfRepeat = false
                    Profile.itemAddFlag_Profile = true

                }
            }


        } while (flagOfRepeat)
    }

    private fun deleteCollection(name: String) {

        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.getAllCollection()

        }
        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {

                for (single in list) {

                    if (single.collections.CollectionName == name) {
                        if (single.itemCollectionsList.isEmpty()) {
                            deleteCollectionUnItemCollection(single.collections.collectionID)
                        }
                        single.itemCollectionsList.forEach {
                            if (it.parentCollectionID == single.collections.collectionID) {
                                viewLifecycleOwner.lifecycleScope.launch {
                                    collectionsViewModel.removeCollectionById(single.collections.collectionID)
                                    collectionsViewModel.removeItemsOfCollectionFilmByParentId(
                                        it.parentCollectionID
                                    )
                                }
                            }
                        }
                    }
                }

            }


        }
    }

    fun deleteCollectionUnItemCollection(idCollection: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.removeCollectionById(idCollection)

        }

    }


    object Profile {
        lateinit var interestedFilmsList: List<InterestedFilmsEntity>
        lateinit var interestedStaffList: List<InterestedStaffEntity>
        var idFilm: Int = 1
        var amountCollection by Delegates.notNull<Int>()
        var itemAddFlag_Profile = false
        lateinit var collectionFilm_Profile: CollectionFilm

    }
}






