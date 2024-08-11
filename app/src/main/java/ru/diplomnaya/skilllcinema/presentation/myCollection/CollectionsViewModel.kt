package ru.diplomnaya.skilllcinema.presentation.myCollection

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.model.allRepository.CollectionRepository
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.Collections
import ru.diplomnaya.skilllcinema.model.database.CollectionsWithItemCollection
import ru.diplomnaya.skilllcinema.model.database.ItemCollection

private const val TAG = "CollectionViewModel"

class CollectionsViewModel private constructor(
    private val collectionRepository: CollectionRepository

) : ViewModel() {
    constructor() : this(CollectionRepository())

    private val collectionFilms: MutableLiveData<List<CollectionsWithItemCollection>> =
        MutableLiveData()
    private val itemCollections: MutableLiveData<List<ItemCollection>> = MutableLiveData()
    val collectionFilmsLiveData: LiveData<List<CollectionsWithItemCollection>>
        get() = collectionFilms
    val itemCollectionsLiveData: LiveData<List<ItemCollection>>
        get() = itemCollections

    var size: Int = 0
    fun addNewCollection(newCollection: Collections) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.addCollection(newCollection)
        }
    }

    fun addNewCollectionItem(newCollectionItem: ItemCollection) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionRepository.addCollectionItem(newCollectionItem)
        }
    }

    suspend fun getCollectionsSize(): Int =
        collectionRepository.getCollectionsSize()

    override fun onCleared() {
        super.onCleared()
    }

    fun getOffAllItemCollections() {
        viewModelScope.launch {
            collectionRepository.getAllOffItemCollections()
                .catch { e ->
                    Log.d("error", "${e.message}")
                }
                .collect { response ->
                    itemCollections.value = response
                }
        }
    }

    fun getAllCollection() {
        viewModelScope.launch {
            collectionRepository.getCollections()
                .catch { e ->
                    Log.d("error", "${e.message}")
                }
                .collect { response ->
                    collectionFilms.value = response
                }
        }
    }

    suspend fun removeFilm(film: CollectionFilm) {
        viewModelScope.launch {
            try {
                collectionRepository.removeFilm(film)
            } catch (t: Throwable) {

            }

        }
    }

    suspend fun removeCollectionById(id: Int) {
        viewModelScope.launch {
            try {
                collectionRepository.removeCollectionById(id)
            } catch (t: Throwable) {

            }

        }
    }

    suspend fun removeItemsOfCollectionFilmByParentId(id: Int) {
        viewModelScope.launch {
            try {
                collectionRepository.removeItemsOfCollectionFilmByParentId(id)
            } catch (t: Throwable) {

            }

        }
    }

    suspend fun removeItemsOfCollectionFilmById(id: Int, film: CollectionFilm) {
        viewModelScope.launch {
            try {
                collectionRepository.removeItemsOfCollectionFilmById(id, film)
            } catch (t: Throwable) {

            }

        }
    }

    suspend fun getCollectionFilmById(id: Int): Flow<CollectionsWithItemCollection> =
        collectionRepository.getCollectionsById(id)
}




