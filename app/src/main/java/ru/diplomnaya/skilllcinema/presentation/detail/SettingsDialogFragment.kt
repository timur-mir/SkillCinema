package ru.diplomnaya.skilllcinema.presentation.detail

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.SettingsDialogBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.Collections
import ru.diplomnaya.skilllcinema.model.database.ItemCollection
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.bindingCopy
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.checkedFlag
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.collectionFilm
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.favouriteCollectionFilmSize
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.idFilm
import ru.diplomnaya.skilllcinema.presentation.detail.AnyData.itemAddFlag
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.callTheSimilarFlag
import ru.diplomnaya.skilllcinema.presentation.myCollection.CollectionsViewModel
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragmentDirections
import kotlin.properties.Delegates

class SettingsDialogFragment : BottomSheetDialogFragment() {

    private var _binding: SettingsDialogBinding? = null

    private val binding get() = _binding!!

    private val favouritesFilmsViewModel by viewModels<CollectionsFavouritesFilmsViewModel>()
    private val collectionsWantTooSeeViewModel by viewModels<CollectionsWantToSeeViewModel>()
    private val collectionsAlreadyViewedViewModel by viewModels<CollectionsAlreadyViewedViewModel>()
    private val viewModelByFilmInfoDetail by viewModels<GetFilmDetailInfoViewModel>()
    private val collectionsViewModel by viewModels<CollectionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        _binding = SettingsDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingCopy = binding
        val mArgs = arguments
        if (mArgs!!.getInt("key2") != 0 && callTheSimilarFlag == false) {
            idFilm = mArgs.getInt("key2")
        } else {
            if (mArgs!!.getInt("key") != 0 && callTheSimilarFlag == false) {
                idFilm = mArgs.getInt("key")
            }
            else if(callTheSimilarFlag){
                idFilm=mArgs.getInt("key3")
            }
        }


        //Создание графических элементов программно на основе содержимого таблицы с типом Collections
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.getAllCollection()
            collectionsWantTooSeeViewModel.getAllWantToSee()
        }
        viewLifecycleOwner.lifecycleScope.launch {

            binding.quantityFavourite.text =
                favouritesFilmsViewModel.getCollectionsSize().toString()
            binding.quantityWantToSee.text =
                collectionsWantTooSeeViewModel.getCollectionsSize().toString()

        }
        collectionsViewModel.collectionFilmsLiveData.observe(viewLifecycleOwner) { list ->

            var idNumber = 1
            if (list.size == 0) {
            } else {
                if (list.size != 0 && !itemAddFlag) {
                    for (innerList in list) {
                        createViewCollectionElement(
                            innerList.collections.collectionID,
                            innerList.collections.CollectionName,
                            innerList.itemCollectionsList.size.toString()
                        )
                        idNumber += 1
                    }
                } else {
                }
            }

        }





        viewLifecycleOwner.lifecycleScope.launch {
            if (idFilm == 0) {
                delay(200)
                favouritesFilmsViewModel.checkFilm(MovieDetailFragment.FlagAndObject.requestFilm.kinopoiskId)
                collectionsWantTooSeeViewModel.checkFilm(MovieDetailFragment.FlagAndObject.requestFilm.kinopoiskId)
            } else {
                favouritesFilmsViewModel.checkFilm(idFilm)
                collectionsWantTooSeeViewModel.checkFilm(idFilm)
            }

            favouritesFilmsViewModel.existLive.observe(viewLifecycleOwner) { Yes ->
                if (Yes) {
                    binding.favouriteFilm.isChecked = true
                }
            }
            collectionsWantTooSeeViewModel.existLive.observe(viewLifecycleOwner) { Yes ->
                if (Yes) {
                    binding.iWantToSee.isChecked = true

                }
            }
        }
binding.quantityFavourite.setOnClickListener {
//  findNavController().navigate(action)
}
        binding.iWantToSee.setOnClickListener(View.OnClickListener {
            Toast.makeText(requireContext(), "I want to see", Toast.LENGTH_LONG).show()
            if (binding.iWantToSee.isPressed) {
                if (!binding.iWantToSee.isChecked)
                    lifecycle.coroutineScope.launch {

                        if (idFilm == 0) {
                            collectionsWantTooSeeViewModel.removeFilmById(MovieDetailFragment.FlagAndObject.requestFilmWantToSee.kinopoiskId)

                        } else {
                            collectionsWantTooSeeViewModel.removeFilmById(idFilm)

                        }
                    }


            }
            if (binding.iWantToSee.isPressed) {
                if (binding.iWantToSee.isChecked)
                    lifecycle.coroutineScope.launch {
                        collectionsWantTooSeeViewModel.addWantToSee(MovieDetailFragment.FlagAndObject.requestFilmWantToSee)
                    }
            }

        })

        binding.favouriteFilm.setOnClickListener(View.OnClickListener {
            if (binding.favouriteFilm.isPressed) {
                if (!binding.favouriteFilm.isChecked)
                    binding.favouriteFilm.isChecked = false
                lifecycle.coroutineScope.launch {
                    if (idFilm == 0) {
                        favouritesFilmsViewModel.removeFilmById(MovieDetailFragment.FlagAndObject.requestFilm.kinopoiskId)
                    } else {
                        favouritesFilmsViewModel.removeFilmById(idFilm)
                    }

                }
            }
            if (binding.favouriteFilm.isPressed) {
                if (binding.favouriteFilm.isChecked) {
                    lifecycle.coroutineScope.launch {
                        favouritesFilmsViewModel.addFavourite(MovieDetailFragment.FlagAndObject.requestFilm)
                    }
                }
            }
        })

// Инициализация интерфейса фрагмента
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelByFilmInfoDetail.getFilmDetailInfo(idFilm)

            viewModelByFilmInfoDetail.movies.onEach {
                collectionFilm = modificationsToCollectionForRoom(it)
                binding.titleAddFilm.text = it.nameRu
                binding.ratingAddFilm.text=it.ratingImdb
                binding.genresAddFilm.text = it.genres?.get(0)?.genre.toString()
                if (it.posterUrlPreview==""){
                    Picasso.with(requireContext()).load(arguments?.getString("key4"))
                        .placeholder(R.drawable.fotosimple).into(binding.posterAddFilm)
                }
                else {
                    Picasso.with(binding.posterAddFilm.context).load(it.posterUrlPreview)
                        .placeholder(R.drawable.fotosimple).into(binding.posterAddFilm)
                }
            }.launchIn(this)
//
        }
        // Создание коллекции  в "диалоговом режиме"
        binding.createNewCollection.setOnClickListener {
            val li = LayoutInflater.from(requireActivity())
            val viewDialog = li.inflate(R.layout.add_collection_dialog, null)
            val textFieldCollection =
                viewDialog.findViewById<View>(R.id.name_collection) as EditText
            val closeButton = viewDialog.findViewById<View>(R.id.cancelbtn) as ImageButton
            val saveCollection =
                viewDialog.findViewById<View>(R.id.save_collection) as Button

            val dialogSettings = AlertDialog.Builder(requireActivity())
                .setView(viewDialog)
                .show()
            saveCollection.setOnClickListener {
                collectionsViewModel.addNewCollection(
                    Collections(
                        0, textFieldCollection.text.toString()
                    )
                )
                dialogSettings.dismiss()
                val action =SettingsDialogFragmentDirections.actionSettingsDialogFragmentToMyCollectionFragment( textFieldCollection.text.toString())
                findNavController().navigate(action)

        }

            closeButton.setOnClickListener {
                dialogSettings.dismiss()
            }
        }
        //Закрытие фрагмента профиля

        binding.cancelbtn.setOnClickListener {
            itemAddFlag = false
            dismiss()
        }

    }


    fun createViewCollectionElement(
        number: Int, nameCollection: String, sizeCollectionForBindTextView: String
    ) {

        val layoutCollection = binding.collectionLayout

        val last_item_price_collection =
            layoutCollection.getChildAt(layoutCollection.childCount - 1)

        val rowNewCollection = LinearLayout(requireContext())

        rowNewCollection.orientation = LinearLayout.HORIZONTAL

        val params = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = 120

        if (last_item_price_collection != null) {
            params.addRule(RelativeLayout.BELOW, last_item_price_collection.id)
        }
        rowNewCollection.layoutParams = params
        rowNewCollection.id = number

        val checkBox = AppCompatCheckBox(requireContext())
        checkBox.text = "$nameCollection"
        checkBox.setTextColor(Color.WHITE)
        checkBox.tag = number
        checkBox.width = 250
        viewLifecycleOwner.lifecycleScope.launch {
            collectionsViewModel.getOffAllItemCollections()
        }
        collectionsViewModel.itemCollectionsLiveData.observe(viewLifecycleOwner) { items ->
            items.forEach { item ->
                if (item.parentCollectionID == checkBox.tag && item.collectionFilmProfile == collectionFilm) {
                    checkBox.isChecked = true

                }
            }

        }

        checkBox.setOnClickListener(View.OnClickListener {
            if (checkBox.isPressed) {
                if (checkBox.isChecked) {

                    viewLifecycleOwner.lifecycleScope.launch {

                        collectionsViewModel.getAllCollection()

                        collectionsViewModel.addNewCollectionItem(
                            ItemCollection(
                                0, collectionFilm,

                                checkBox.tag as Int
                            )
                        )
                        itemAddFlag = true
                    }

                }
            }
            if (checkBox.isPressed) {
                if (!checkBox.isChecked) {
                    viewLifecycleOwner.lifecycleScope.launch {

                        collectionsViewModel.removeItemsOfCollectionFilmById(number, collectionFilm)
                        itemAddFlag = true
                    }
                }
            }
        })

        val paramsT = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )

        paramsT.leftMargin = 300

        val textView: TextView? = TextView(requireContext())
        textView?.layoutParams = paramsT
        textView?.layoutParams?.width ?: 10
        textView?.layoutParams?.height ?: 10

        if (textView != null) {
            textView.text = sizeCollectionForBindTextView
            textView.setTextColor(Color.WHITE)
            textView.setOnClickListener {

                itemAddFlag = true
                val action =
                    SettingsDialogFragmentDirections.actionSettingsDialogFragmentToMyCollectionFragment(
                        nameCollection.toString()
                    )
                findNavController().navigate(action)
                findNavController().popBackStack(
                    R.id.action_settingsDialogFragment_to_myCollectionFragment, true

                )
            }
        }
        rowNewCollection.addView(checkBox)
        rowNewCollection.addView(textView)
        layoutCollection.addView(rowNewCollection, params)
    }

    fun modificationsToCollectionForRoom(detInfo: FilmDetailInfo): CollectionFilm {

        return CollectionFilm(
            detInfo.kinopoiskId,
            detInfo.imdbId,
            detInfo.nameRu,
            detInfo.nameOriginal,
            detInfo.year,
            detInfo.posterUrlPreview,
            detInfo.genres?.get(0)?.genre.toString(),
            detInfo.ratingImdb,
            detInfo.filmLength,
            detInfo.ratingAgeLimits,
            detInfo.serial,
            detInfo.countries?.get(0)?.country.toString(),
            detInfo.description,
            detInfo.type
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        itemAddFlag = false
        callTheSimilarFlag = false
        idFilm = 1

    }

    companion object {
        const val TAG = "SettingsDialogFragment "
    }
}

object AnyData {
    lateinit var bindingCopy: ViewBinding
    var idFilm: Int = 1
    var favouriteCollectionFilmSize = 1
    var itemAddFlag = false
    var checkedFlag = false
    lateinit var collectionFilm: CollectionFilm


}
