package ru.diplomnaya.skilllcinema.presentation.main

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.App
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.SplashActivityBinding
import kotlin.system.exitProcess

class SplashActivity : AppCompatActivity() {
    private var _binding: SplashActivityBinding? = null
    private val binding get() = _binding!!

    companion object {
        var audioFon = MediaPlayer.create(App.appContext, R.raw.fon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SplashActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.logoImageView.bringToFront()
        audioFon.start()

        binding.motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {

            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {

            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                binding.tableImage.alpha = 1F
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {

            }

        })


    }

    override fun onResume() {
        super.onResume()
        val myString = resources.getString(R.string.about_programm)
        val bytes = myString.toByteArray(charset("UTF-8"))
        val string2 = String(bytes, charset("UTF-8"))
        val chars = string2.length
        var oldChar=""

        lifecycleScope.launch (Dispatchers.Main)
        {
            for (ch in 0..chars-1) {
                binding.copyrightTextView.text= "$oldChar"+string2[ch].toString()
                        delay(30)
                oldChar=oldChar+string2[ch].toString()
            }
        }
       binding.logoImageView.postDelayed({binding.logoImageView.visibility= View.INVISIBLE},10000)
        Handler().postDelayed(Runnable {
            audioFon.apply {
                pause()
                reset()
                release()
            }.also {
                audioFon = null
                finish()
                exitProcess(1)
            }
        }, 32000)
    }
}