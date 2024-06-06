package ru.diplomnaya.skilllcinema.presentation.filmography

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.ActorFilmographyMainFragmentBinding
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.ShortInfoActors
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.actors.HelpCompanionObject.actorContainer
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyActivity.HelpFilmographyActor.staredPersonObject
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyMainFragment.HelpActorFilmographyMainFragment.mutableListAboutFilmographyActor


class ActorFilmographyMainFragment : Fragment() {
    private var _binding: ActorFilmographyMainFragmentBinding? = null
    val binding get() = _binding!!
    private val viewModelByFilmInfoDetail by viewModels<GetFilmDetailInfoViewModel>()
    var shortInfoActorsList: MutableList<ArrayList<ShortInfoActors>> = mutableListOf()
    var actorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var herselfProf: ArrayList<ShortInfoActors> = arrayListOf()
    var producerProf: ArrayList<ShortInfoActors> = arrayListOf()
    var directorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var composerProf: ArrayList<ShortInfoActors> = arrayListOf()
    var writerProf: ArrayList<ShortInfoActors> = arrayListOf()
    var operatorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var designProf: ArrayList<ShortInfoActors> = arrayListOf()
    var editorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var voiceDirectorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var translatorProf: ArrayList<ShortInfoActors> = arrayListOf()
    var dubbingProf: ArrayList<ShortInfoActors> = arrayListOf()


    val actor = "ACTOR"
    private val herself = "HERSELF"
    private val dubbing = "DUBBING"
    private val producer = "PRODUCER"
    private val director = "DIRECTOR"
    private val composer = "COMPOSER"
    private val writer = "WRITER"
    private val operatorType = "OPERATOR"
    private val design = "DESIGN"
    private val editor = "EDITOR"
    private val voiceDirector = "VOICE_DIRECTOR"
    private val translator = "TRANSLATOR"

    private val actorR = "Актер"
    private val actressR = "Актриса"
    private val herselfR = "Актриса:играет саму себя"
    private val producerR = "Продюсер"
    private val directorR = "Режиссер"
    private val composerR = "Композитор"
    private val writerR = "Сценарист"
    private val operatorTypeR = "Оператор"
    private val designR = "Дизайнер"
    private val editorR = "Редактор"
    private val voiceDirectorR = "Озвучка"
    private val translatorR = "Переводчик"
    private val dubbingR = "Актриса дубляжа"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActorFilmographyMainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            delay(50)
            actorContainer.films.forEach {
                it.filmId?.let { filmID ->
                    delay(20)
                    viewModelByFilmInfoDetail.getFilmDetailInfoForActors(
                        filmID
                    )
                }
            }
        }

        lifecycleScope.launch {
            delay(200)
            var x = 0
            var genresInfo = "Не указано"

            viewModelByFilmInfoDetail.filmInfoForActorsLiveData.observe(viewLifecycleOwner) { item ->
                if (item.genres?.isNotEmpty() == true) {
                    genresInfo = item.genres[0].genre.toString()
                }
                mutableListAboutFilmographyActor += ShortInfoActors(
                    actorContainer.nameRu?.toString()
                        ?: actorContainer.nameEn?.toString() ?: "*",
                    genresInfo,
                    item.posterUrlPreview,
                    actorContainer.films[x].filmId,
                    actorContainer.films[x].nameRu.toString(),
                    actorContainer.films[x].nameEn.toString(),
                    actorContainer.films[x].rating.toString(),
                    actorContainer.sex.toString(),
                    staredPersonObject.professionText,
                    actorContainer.films[x].professionKey.toString()
                )
                x++
            }
        }


        Handler().postDelayed(
            {
                mutableListAboutFilmographyActor.forEach {
                    if (actor == it.professionKey) {
                        if (it.sex == "MALE")
                            actorProf.add(it.apply { it.professionKey = actorR })
                        else
                            actorProf.add(it.apply { it.professionKey = actressR })
                    }

                    if (herself == it.professionKey) {
                        herselfProf.add(it.apply { it.professionKey = herselfR })
                    }
                    if (dubbing == it.professionKey) {
                        dubbingProf.add(it.apply { it.professionKey = dubbingR })
                    }

                    if (producer == it.professionKey) {
                        producerProf.add(it.apply { it.professionKey = producerR })
                    }
                    if (director == it.professionKey) {
                        directorProf.add(it.apply { it.professionKey = directorR })
                    }
                    if (composer == it.professionKey) {
                        composerProf.add(it.apply { it.professionKey = composerR })
                    }
                    if (writer == it.professionKey) {
                        writerProf.add(it.apply { it.professionKey = writerR })
                    }
                    if (operatorType == it.professionKey) {
                        operatorProf.add(it.apply { it.professionKey = operatorTypeR })
                    }
                    if (design == it.professionKey) {
                        designProf.add(it.apply { it.professionKey = designR })
                    }
                    if (editor == it.professionKey) {
                        editorProf.add(it.apply { it.professionKey = editorR })
                    }
                    if (voiceDirector == it.professionKey) {
                        voiceDirectorProf.add(it.apply { it.professionKey = voiceDirectorR })
                    }
                    if (translator == it.professionKey) {
                        translatorProf.add(it.apply { it.professionKey = translatorR })
                    }
                    if (mutableListAboutFilmographyActor.last() == it) {
                        if (actorProf.isNotEmpty()) {
                            shortInfoActorsList.add(actorProf)
                        }
                        if (herselfProf.isNotEmpty()) {
                            shortInfoActorsList.add(herselfProf)
                        }
                        if (dubbingProf.isNotEmpty()) {
                            shortInfoActorsList.add(dubbingProf)
                        }

                        if (producerProf.isNotEmpty()) {
                            shortInfoActorsList.add(producerProf)
                        }
                        if (directorProf.isNotEmpty()) {
                            shortInfoActorsList.add(directorProf)
                        }
                        if (composerProf.isNotEmpty()) {
                            shortInfoActorsList.add(composerProf)
                        }
                        if (operatorProf.isNotEmpty()) {
                            shortInfoActorsList.add(operatorProf)
                        }
                        if (writerProf.isNotEmpty()) {
                            shortInfoActorsList.add(writerProf)
                        }
                        if (designProf.isNotEmpty()) {
                            shortInfoActorsList.add(designProf)
                        }
                        if (editorProf.isNotEmpty()) {
                            shortInfoActorsList.add(editorProf)
                        }
                        if (voiceDirectorProf.isNotEmpty()) {
                            shortInfoActorsList.add(voiceDirectorProf)
                        }
                        if (translatorProf.isNotEmpty()) {
                            shortInfoActorsList.add(translatorProf)
                        }
                    }
                }

                binding.filmographyViewPager.adapter = ActorFilmographyStateAdapter(
                    shortInfoActorsList,
                    this@ActorFilmographyMainFragment
                )
                Log.d(
                    "TAG134",
                    "Загрузка данных: ID  персоны:(${shortInfoActorsList[0][0].filmId})"
                )

                binding.filmographyViewPager.setCurrentItem(0, true)
                binding.filmographyViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                binding.actorNameTextView.text = mutableListAboutFilmographyActor[0].actorName
                val tablayoutForFilmography = binding.tabLayoutFilmography
                val filmographyViewPager = binding.filmographyViewPager
                TabLayoutMediator(tablayoutForFilmography, filmographyViewPager) { tab, position ->
                    tab.text =
                        "${shortInfoActorsList[position][0].professionKey} ${shortInfoActorsList[position].size}"
                }.attach()
            }, 1800
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    object HelpActorFilmographyMainFragment {
        var mutableListAboutFilmographyActor: ArrayList<ShortInfoActors> =
            arrayListOf<ShortInfoActors>()

    }

}