package ru.diplomnaya.skilllcinema.presentation.filmography

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.FilmographyActivityBinding
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred
import ru.diplomnaya.skilllcinema.presentation.filmography.ActorFilmographyActivity.HelpFilmographyActor.staredPersonObject


class ActorFilmographyActivity : AppCompatActivity() {
    private var _binding: FilmographyActivityBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FilmographyActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bar = supportActionBar
        val gradient = ResourcesCompat.getDrawable(resources, R.drawable.gradient7, null)
        bar!!.setBackgroundDrawable(gradient)
        staredPersonObject = intent?.extras?.getParcelable<StaffStarred>("person")!!
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    object HelpFilmographyActor{
        lateinit var staredPersonObject: StaffStarred
    }
}