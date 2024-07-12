package ru.diplomnaya.skilllcinema.presentation.intro

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.LoadingActivityBinding
import ru.diplomnaya.skilllcinema.presentation.main.MainActivity

class LoadingActivity : AppCompatActivity() {
    private var _binding: LoadingActivityBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoadingActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bar = supportActionBar
        val gradient = ResourcesCompat.getDrawable(resources, R.drawable.gradient7, null)
        bar!!.setBackgroundDrawable(gradient)

        Handler().postDelayed(
            {

                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            },

            500

        )
    }
    override fun onDestroy() {
        super.onDestroy()
        //_binding=null
    }
}