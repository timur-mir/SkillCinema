package ru.diplomnaya.skilllcinema.presentation.gallery

import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.GalleryActivityBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages
import ru.diplomnaya.skilllcinema.presentation.detail.ImagesViewModel
import ru.diplomnaya.skilllcinema.presentation.gallery.GalleryActivity.HelpGallery.idFilmOrSerial


class GalleryActivity : AppCompatActivity() {
    private var _binding: GalleryActivityBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = GalleryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        idFilmOrSerial = intent?.extras?.getInt("filmOrSerialId")
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
    object HelpGallery{
        var idFilmOrSerial: Int? = null
    }


}
