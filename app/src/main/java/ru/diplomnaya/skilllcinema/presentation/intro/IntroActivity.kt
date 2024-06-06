package ru.diplomnaya.skilllcinema.presentation.intro

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ActivityIntroBinding
import ru.diplomnaya.skilllcinema.presentation.PreferenceKeys
import ru.diplomnaya.skilllcinema.presentation.intro.IntroActivity.State.netState


import ru.diplomnaya.skilllcinema.presentation.main.MainActivity

class IntroActivity : AppCompatActivity() {
    companion object {
        class WifiReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                    val connectivity =
                        intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
                    if (connectivity) {
                        netState = false

                    }
                }

            }
        }
    }

    private val screens: List<CompositionScreen> = listOf(
        CompositionScreen(
            textRes = R.string.find_out_about_the_premieres,
            bgColorRes = R.color.frame_color,
            drawableRes = R.drawable.intro1screen
        ),
        CompositionScreen(
            textRes = R.string.create_collections,
            bgColorRes = R.color.frame_color,
            drawableRes = R.drawable.intro2screen
        ),
        CompositionScreen(
            textRes = R.string.share_with_your_friends,
            bgColorRes = R.color.frame_color,
            drawableRes = R.drawable.intro3screen
        )
    )
    private var _binding: ActivityIntroBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter().apply { addAction(ConnectivityManager.CONNECTIVITY_ACTION) }
        val receiverWifi = WifiReceiver()
        registerReceiver(
            receiverWifi,
            filter
        )
        lifecycleScope
            .launch {
                delay(2000)
                val pref = App.DatastoreLauncher.applicationDatastore.data.first()
                if (pref[PreferenceKeys.NOT_LOADING_INTRO_ACTIVITY] == true && netState) {
                    jumpToNextActivity()
                } else {
                    Toast.makeText(
                        this@IntroActivity,
                        "Соединения с wifi или с сетью отсутствуют.Подключитесь к сети.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    Snackbar.make(binding.root, "Сеть недоступна", Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(Color.WHITE)
                        .setBackgroundTint((Color.BLUE))
                        .setAction("Дальше") {
                            jumpToNextActivity()
                        }
                        .show()

                    App.DatastoreLauncher.applicationDatastore.edit { preferences ->
                        preferences[PreferenceKeys.NOT_LOADING_INTRO_ACTIVITY] = true
                    }

                }

        }
        _binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = CompositionAdapter(screens, this)
        val vp = binding.viewpager
        binding.viewpager.adapter = adapter

        binding.viewpager.setCurrentItem(0, true)
        binding.viewpager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val tablayout = binding.tabDots

        TabLayoutMediator(tablayout, vp) { tab, position ->

        }.attach()
        vp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {


            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                val buttonNext = vp.findViewById<Button>(R.id.nextButton)
                buttonNext.setOnClickListener {
                    jumpToNextActivity()
                }
                if (position == vp.adapter!!.itemCount - 1) {
                    jumpToNextActivity()
                }
                super.onPageSelected(position)
            }
        })

    }



//        override fun onDestroy() {
//            super.onDestroy()
//            _binding=null
//
//        }


    fun jumpToNextActivity() {
        val intent = Intent(this@IntroActivity, LoadingActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

    private fun jumpToIntroActivity() {
        val intent = Intent(this@IntroActivity, IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        //  finish()
        startActivity(intent)
        //  overridePendingTransition(0, 0);
        Runtime.getRuntime().exit(0)
        overridePendingTransition(0, 0);
    }

    private fun jumpToMainActivity() {
        val intent = Intent(this@IntroActivity, MainActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }


    object State {
        var searchState = false
        var netState = true
    }
}


