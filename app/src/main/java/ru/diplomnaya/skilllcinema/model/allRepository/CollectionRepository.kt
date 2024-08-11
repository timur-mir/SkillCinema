package ru.diplomnaya.skilllcinema.model.allRepository

import kotlinx.coroutines.flow.Flow
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.model.database.Collections
import ru.diplomnaya.skilllcinema.model.database.CollectionsWithItemCollection
import ru.diplomnaya.skilllcinema.model.database.DataBase
import ru.diplomnaya.skilllcinema.model.database.ItemCollection


class CollectionRepository {
    private val collectionDao = DataBase.INSTANCE!!.collectionDao()

    suspend fun addCollection(collections: Collections) =
        collectionDao.addNewCollectionFilm(collections)

    suspend fun addCollectionItem(collectionsItem: ItemCollection) =
        collectionDao.addNewItems(collectionsItem)

    suspend fun getCollectionsById(id: Int): Flow<CollectionsWithItemCollection> =
        collectionDao.getCollectionsById(id)

    suspend fun getCollections(): Flow<List<CollectionsWithItemCollection>> =
        collectionDao.getCollections()

    //    suspend fun getCollectionSize():Flow<Int> =
//       collectionDao.getRowCount()
//    suspend fun checkFilm(film: CollectionFilm,id:Int):Boolean{
//        return collectionDao.exists(film,id)
//    }
    suspend fun getAllOffItemCollections(): Flow<List<ItemCollection>> =
        collectionDao.getAllItemsOffCollections()
    suspend fun removeCollectionById(id: Int) {
        collectionDao.removeCollectionById(id)
    }

    suspend fun removeItemsOfCollectionFilmByParentId(id: Int) {
        collectionDao.removeItemsOfCollectionFilmByParentId(id)
    }

    suspend fun removeItemsOfCollectionFilmById(id: Int,film:CollectionFilm) {
        collectionDao.removeItemsOfCollectionFilmById(id,film)
    }
    suspend fun removeFilm(film:CollectionFilm) {
        collectionDao.removeFilm(film)
    }

    suspend fun getCollectionsSize(): Int =
        collectionDao.getRowCount()

}