package ru.diplomnaya.skilllcinema.presentation.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.ImagesRepository
import ru.diplomnaya.skilllcinema.model.allRepository.SerialRepository
import ru.diplomnaya.skilllcinema.model.entities.Images
import ru.diplomnaya.skilllcinema.model.entities.Serial

class ImagesViewModel(
    private val repository: ImagesRepository
) : ViewModel() {
    constructor() : this(ImagesRepository())
    val initialValue: Images? = null
    private val _images = MutableStateFlow(initialValue)
    val images = _images.asStateFlow()
    private val _imagesStill = MutableStateFlow(initialValue)
    val imagesStill = _imagesStill.asStateFlow()
    private val _imagesShooting = MutableStateFlow(initialValue)
    val imagesShooting = _imagesShooting.asStateFlow()

    fun getImages(idFilm:Int,type:String,page:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getImages(idFilm,type,page)
            }.fold(
                onSuccess = { _images.value = it },
                onFailure = { Log.d("ImagesViewModel", it.message ?: "") }
            )

        }
    }
    fun getImagesStill(idFilm:Int,type:String,page:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getImages(idFilm,type,page)
            }.fold(
                onSuccess = { _imagesStill.value = it },
                onFailure = { Log.d("ImagesViewModel", it.message ?: "") }
            )

        }
    }
    fun getImagesShooting(idFilm:Int,type:String,page:Int) {
        viewModelScope.launch {
            kotlin.runCatching {
                repository.getImages(idFilm,type,page)
            }.fold(
                onSuccess = { _imagesShooting.value = it },
                onFailure = { Log.d("ImagesViewModel", it.message ?: "") }
            )

        }
    }

}
