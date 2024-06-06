package ru.diplomnaya.skilllcinema.presentation.detail

import android.Manifest
import android.R.attr
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ImageFilmAnyTypeBinding
import ru.diplomnaya.skilllcinema.presentation.detail.MovieDetailFragment.FlagAndObject.forAddProfile
import java.io.File
import java.util.Date
import java.util.Objects


class ImageFragment : Fragment() {
    private var _binding: ImageFilmAnyTypeBinding? = null
    val binding get() = _binding!!
    var date: Date = Date()
    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                Toast.makeText(
                    requireContext(),
                    "Теперь разрешения на запись есть",
                    Toast.LENGTH_LONG
                ).show()
                val bitmapDrawable = binding.filmImageAnyType.drawable
                saveImage(bitmapDrawable.toBitmap())
            } else {
                Toast.makeText(requireContext(), "Нет разрешения на запись", Toast.LENGTH_LONG)
                    .show()
            }

        }
    private val changeImage =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
                val imgUri = data?.data
               binding.filmImageAnyType.setImageURI(imgUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = ImageFilmAnyTypeBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arg: ImageFragmentArgs by navArgs()

        Picasso
            .with(requireContext())
            .load(arg.urlPic)
            .fit()
            .centerInside()
            .placeholder(R.drawable.gradient2)
            .into(binding.filmImageAnyType)
        binding.saveActorsFoto.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                checkPermission()
            }
        }
        binding.openGallery.setOnClickListener{
            val pickImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            changeImage.launch(pickImg)
        }

    }

    fun saveImage(bitmap: Bitmap) {
        val resolver: ContentResolver? = context?.contentResolver
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

                val contentValues = ContentValues()
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "${forAddProfile.nameRu}_$date.jpg")
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                contentValues.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "TestFolder"
                )
                val imageUri =
                    resolver?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                val fos = Objects.requireNonNull(imageUri)?.let { resolver?.openOutputStream(it) }

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                Objects.requireNonNull(fos)
                Toast.makeText(requireContext(), "Изображение сохранено", Toast.LENGTH_SHORT).show()
            } else {

                val savedImageURL: String = MediaStore.Images.Media.insertImage(
                    resolver,
                    bitmap,
                    "${forAddProfile.nameRu}_$date.jpg",
                    "Фото из фильма,со съёмок фильма,..."
                )
                val savedImageURI = Uri.parse(savedImageURL)
                Toast.makeText(requireContext(), " Изображение сохранено -${savedImageURI} ", Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {
            Toast.makeText(
                requireContext(),
                " Не удалось сохранить изображение по причине:\n$e",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun checkPermission() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            Toast.makeText(requireContext(), "Есть разрешения на запись", Toast.LENGTH_LONG).show()
            val bitmapDrawable = binding.filmImageAnyType.drawable
            saveImage(bitmapDrawable.toBitmap())
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }


    }

    companion object {

        private val REQUEST_PERMISSIONS: Array<String> = buildList {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                add(Manifest.permission.ACCESS_MEDIA_LOCATION)
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                add(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.READ_MEDIA_IMAGES)
            }

        }.toTypedArray()


    }


}
