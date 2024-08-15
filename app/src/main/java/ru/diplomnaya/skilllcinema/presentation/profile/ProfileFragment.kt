package ru.diplomnaya.skilllcinema.presentation.profile

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.FragmentProfileBinding
import ru.diplomnaya.skilllcinema.model.Movie
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.Collections
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.CustomViewCollection
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsAlreadyViewedViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsFavouritesFilmsViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.CollectionsWantToSeeViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.intro.AuthenticationActivity
import ru.diplomnaya.skilllcinema.presentation.main.MainActivity
import ru.diplomnaya.skilllcinema.presentation.main.PremieresListViewModel
import ru.diplomnaya.skilllcinema.presentation.myCollection.CollectionsViewModel
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.NOTIFICATION_CHANNEL_ID
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.NOTIFICATION_ID
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.addNewCollectionFlag
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.collectionSize
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.interestedFilmsList
import ru.diplomnaya.skilllcinema.presentation.profile.ProfileFragment.Profile.interestedStaffList
import ru.diplomnaya.skilllcinema.utilits.ItemOffsetDecoration
import ru.diplomnaya.skilllcinema.utilits.getScreenHeight
import ru.diplomnaya.skilllcinema.utilits.getScreenWidth
import ru.diplomnaya.skilllcinema.view.FullUsesAdapters.AlreadyViewedAdapterForConcatAdapter
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
if(addNewCollectionFlag){
    findNavController().popBackStack()
    findNavController().navigate(R.id.profileFragment2)
}
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
        binding.favouriteTile.setImageCollection(ru.diplomnaya.skilllcinema.R.drawable.heart)

        binding.favouriteTile.setDeleteCollectionButtonVisibility()
        binding.wantToSee.setTextForNameCollection("Хочу посмотреть")
        binding.wantToSee.setImageCollection(ru.diplomnaya.skilllcinema.R.drawable.bookmark)

        binding.wantToSee.setDeleteCollectionButtonVisibility()


        binding.wantToSee.setOnClickListener {
            val action =
                ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(binding.wantToSee.getNameCollection())
            findNavController().navigate(action)
        }



        binding.favouriteTile.setOnClickListener {
            val args = Bundle()
            args.putString("name", "Любимые")
            findNavController().navigate(ru.diplomnaya.skilllcinema.R.id.favouriteFragment, args)

        }

        binding.createNewCollectionSecondUse.setOnClickListener {
            val liSecondUse = LayoutInflater.from(requireActivity())
            val viewDialogSecondUse =
                liSecondUse.inflate(ru.diplomnaya.skilllcinema.R.layout.add_collection_dialog, null)
            val textFieldCollectionSecondUse =
                viewDialogSecondUse.findViewById<View>(ru.diplomnaya.skilllcinema.R.id.name_collection) as EditText
            val closeButtonSecondUse =
                viewDialogSecondUse.findViewById<View>(ru.diplomnaya.skilllcinema.R.id.cancelbtn) as ImageButton
            val saveCollection =
                viewDialogSecondUse.findViewById<View>(ru.diplomnaya.skilllcinema.R.id.save_collection) as Button
            val dialog = AlertDialog.Builder(requireActivity())
                .setView(viewDialogSecondUse)
                .show()
            saveCollection.setOnClickListener {
                addNewCollectionFlag=true
                collectionsViewModel.addNewCollection(
                    Collections(
                        0, textFieldCollectionSecondUse.text.toString()
                    )
                )
                dialog.dismiss()              
                        createNotification(textFieldCollectionSecondUse.text.toString())
                        newMakeCollectionToast(textFieldCollectionSecondUse.text.toString(),1)

                     //   loadCollection(textFieldCollectionSecondUse.text.toString())

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
            
                

            } else {
               
                if (list.size != 0 && !Profile.itemAddFlag_Profile) {
                    collectionSize=list.size
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

    override fun onResume() {
        super.onResume()

    }

    private fun newMakeCollectionToast(item: String) {

    }

    private fun newMakeCollectionToast(item: String,  size: Int) {
        val inflater = layoutInflater;
        val layout = inflater.inflate(
            R.layout.toast_layout,
            requireActivity().findViewById(R.id.toast_layout_root)
        );
        val image = layout.findViewById<ImageView>(R.id.image_toast);
        image.setImageResource(R.drawable.make_many_jobs)
        val text = layout.findViewById<TextView>(R.id.toast_text);
        if (size>0){
        text.text = "Создаётся коллекция : $item"
        val toast = Toast(requireContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, getScreenWidth() / 2, getScreenHeight() / 2);
        toast.duration = Toast.LENGTH_LONG;
        toast.setView(layout);
        toast.show()
          loadCollectionFragment(item)
        }
        else {
            text.text = "Создайте особенную коллекцию!"
            val toast = Toast(requireContext());
         //   toast.setGravity(Gravity.CENTER_VERTICAL, getScreenWidth() / 2, getScreenHeight() / 2);
            toast.duration = Toast.LENGTH_LONG;
            toast.setView(layout);
            toast.show()

        }

    }
    private fun deleteOldCollectionToast(item: String) {
        val inflater = layoutInflater;
        val layout = inflater.inflate(
            R.layout.toast_layout,
            requireActivity().findViewById(R.id.toast_layout_root)
        );
        val image = layout.findViewById<ImageView>(R.id.image_toast);
        image.setImageResource(R.drawable.police)
        val text = layout.findViewById<TextView>(R.id.toast_text);

            text.text = "Удаляется коллекция : $item"
            val toast = Toast(requireContext());
            toast.setGravity(Gravity.CENTER_VERTICAL, getScreenWidth() / 2, getScreenHeight() / 2);
            toast.duration = Toast.LENGTH_LONG;
            toast.setView(layout);
            toast.show()
        findNavController().popBackStack()
        findNavController().navigate(R.id.profileFragment2)

    }
    fun loadCollectionFragment(name: String) {
        val action =
            ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                name
            )
        findNavController().navigate(action)
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotification(notificationMessage: String) {
        //val intent = requireActivity().packageManager.getLaunchIntentForPackage("ru.diplomnaya.skilllcinema")
        val intent = Intent(requireContext(), AuthenticationActivity::class.java)
        intent!!.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent =
            PendingIntent.getActivity(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(ru.diplomnaya.skilllcinema.R.drawable.add_to_collection)
            .setContentTitle("Создание коллекции")
            .setContentText("Создание коллекции-$notificationMessage выполнено!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(500))
            .setLargeIcon(
                BitmapFactory.decodeResource(getResources(),
                R.drawable.fotosimple))
            .addAction(ru.diplomnaya.skilllcinema.R.drawable.main, "В коллекцию", pendingIntent)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setDefaults(Notification.DEFAULT_LIGHTS)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .build()
        val ringURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        notification.sound = ringURI
        notification.ledARGB = Color.RED
        notification.ledOffMS = 0
        notification.ledOnMS = 1
        notification.flags = notification.flags or Notification.FLAG_SHOW_LIGHTS

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val notifyManager = activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        notifyManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val sound =
            Uri.parse("android.resource://" + "ru.diplomnaya.skilllcinema" + "/" + ru.diplomnaya.skilllcinema.R.raw.close)
        val name = "Новый канал"
        val descriptionText = "Это ...канал"
        val priority = NotificationManager.IMPORTANCE_HIGH
        val vibrationPattern = longArrayOf(500)
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(NOTIFICATION_CHANNEL_ID, name, priority).apply {
                description = descriptionText
            }
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        channel.description = "NOTIFICATION_DESCRIPTION"
        channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        channel.enableLights(true)
        channel.lightColor = Color.RED
        channel.enableVibration(true)
        channel.vibrationPattern = vibrationPattern

        val attributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
        channel.setSound(sound, attributes)
        channel.shouldShowLights()
        activity?.getNotificationManager()?.createNotificationChannel(channel)
//       val notifyManager= activity?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notifyManager.createNotificationChannel(channel)

    }

    private fun Context.getNotificationManager(): NotificationManager {
        return getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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
                val lookCollection =
                    rv.findViewById<AppCompatButton>(ru.diplomnaya.skilllcinema.R.id.button_view_collection)
                lookCollection.text = sizeCollection[colFirstInSlice]
                lookCollection.setOnClickListener {
                    val action =
                        ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                            nameCollection[(cardView1.id) - 1].toString()
                        )
                    findNavController().navigate(action)

                }
                val delButton =
                    rv.findViewById<ImageButton>(ru.diplomnaya.skilllcinema.R.id.delete_collection)
                delButton.setOnClickListener(View.OnClickListener { view ->
                    deleteOldCollectionToast(cardView1.getNameCollection())
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
                val lookCollection2 =
                    rv2.findViewById<AppCompatButton>(ru.diplomnaya.skilllcinema.R.id.button_view_collection)
                lookCollection2.text = sizeCollection[colSecondInSlice]
                lookCollection2.setOnClickListener {
                    val action =
                        ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                            nameCollection[(cardView2.id) - 1].toString()
                        )
                    findNavController().navigate(action)

                }
                val delButton2 =
                    rv2.findViewById<ImageButton>(ru.diplomnaya.skilllcinema.R.id.delete_collection)
                delButton2.setOnClickListener(View.OnClickListener { view ->
                    deleteOldCollectionToast(cardView2.getNameCollection())
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
                        rv3.findViewById<AppCompatButton>(ru.diplomnaya.skilllcinema.R.id.button_view_collection)
                    lookCollection3.text = sizeCollection[colFirstInSlice]
                    lookCollection3.setOnClickListener {
                        val action =
                            ProfileFragmentDirections.actionProfileFragment2ToMyCollectionFragment(
                                nameCollection[(cardView3.id) - 1].toString()
                            )
                        findNavController().navigate(action)

                    }
                    val delButton3 =
                        rv3.findViewById<ImageButton>(ru.diplomnaya.skilllcinema.R.id.delete_collection)
                    delButton3.setOnClickListener(View.OnClickListener { view ->
                        deleteOldCollectionToast( cardView3.getNameCollection())
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

    override fun onPause() {
        super.onPause()

    }





    object Profile {
        const val NOTIFICATION_CHANNEL_ID: String = "канал 1"
        const val NOTIFICATION_ID = 1000
        var addNewCollectionFlag=false
        lateinit var interestedFilmsList: List<InterestedFilmsEntity>
        lateinit var interestedStaffList: List<InterestedStaffEntity>
        var idFilm: Int = 1
        var collectionSize=0
        var amountCollection by Delegates.notNull<Int>()
        var itemAddFlag_Profile = false
        lateinit var collectionFilm_Profile: CollectionFilm

    }
}






