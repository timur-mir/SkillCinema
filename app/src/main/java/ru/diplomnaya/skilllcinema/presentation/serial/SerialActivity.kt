package ru.diplomnaya.skilllcinema.presentation.serial

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.SerialActivityBinding
import ru.diplomnaya.skilllcinema.model.entities.FilmDetailInfo
import ru.diplomnaya.skilllcinema.model.entities.Serial
import ru.diplomnaya.skilllcinema.presentation.detail.GetFilmDetailInfoViewModel
import ru.diplomnaya.skilllcinema.presentation.detail.SerialViewModel
import ru.diplomnaya.skilllcinema.presentation.serial.SerialActivity.SerialHelp.forSerialTitle

class SerialActivity : AppCompatActivity() {
    private var _binding: SerialActivityBinding? = null
    private val binding get() = _binding!!
    private val serialViewModel by viewModels<SerialViewModel>()
    private val viewModelByFilmInfoDetail by viewModels<GetFilmDetailInfoViewModel>()
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SerialActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bar = supportActionBar
        val gradient = ResourcesCompat.getDrawable(resources, R.drawable.gradient7, null)
        bar!!.setBackgroundDrawable(gradient)
        var mydata = intent?.extras?.getInt("serialId")

        lifecycleScope
            .launch {
                viewModelByFilmInfoDetail.getFilmDetailInfo(
                    mydata!!
                )
                viewModelByFilmInfoDetail.movies
                    .onEach {

                        forSerialTitle = it

                    }
                    .launchIn(this)
            }
        lifecycleScope.launch {
            delay(350)

            if (mydata != null) {
                serialViewModel.getSerial(mydata)
            }
            serialViewModel.serial
                .onEach { serial ->
                    if (serial != null) {
                        SerialHelp.seasonEpizode = serial
                    }

                }
                .launchIn(this)

        }
        Handler().postDelayed(
            {
                binding.serialViewPager.adapter =
                    SerialFragmentStateAdapter(SerialHelp.seasonEpizode, this@SerialActivity)
                binding.serialViewPager.setCurrentItem(0, true)
                binding.serialViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                val tablayoutForSeason = binding.tabLayoutSeasons
                val serialViewPagerB = binding.serialViewPager

                if (SerialHelp.forSerialTitle.nameOriginal?.isNotEmpty() == true) {
                    binding.serialNameTextView.text = SerialHelp.forSerialTitle.nameOriginal
                } else {
                    if (SerialHelp.forSerialTitle.nameRu.isNotEmpty() == true) {
                        binding.serialNameTextView.text =
                            SerialHelp.forSerialTitle.nameRu
                    }
                }
                TabLayoutMediator(tablayoutForSeason, serialViewPagerB) { tab, position ->
                    tab.text = "${position + 1}"
                }.attach()
                for (i in 0..SerialHelp.seasonEpizode.items.size) {
                    val textView = LayoutInflater.from(this@SerialActivity)
                        .inflate(R.layout.tab_title, null) as TextView
                    textView.text = "    ${i + 1}    "
                    binding.tabLayoutSeasons.getTabAt(i)?.customView = textView
                }

            }, 1000
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    object SerialHelp {
        lateinit var seasonEpizode: Serial
        lateinit var forSerialTitle: FilmDetailInfo
    }
}