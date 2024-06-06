package ru.diplomnaya.skilllcinema.presentation.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import ru.diplomnaya.skilllcinema.databinding.MyCollectionActivityBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm
import ru.diplomnaya.skilllcinema.presentation.myCollection.CollectionsViewModel
import ru.diplomnaya.skilllcinema.presentation.myCollection.MyCollectionAdapter

class MyCollectionActivity : AppCompatActivity() {
    private var _binding: MyCollectionActivityBinding? = null
    private val binding get() = _binding!!

    var myCollectionFilmAdapter = MyCollectionAdapter { collection -> }
    private val collectionsViewModel by viewModels<CollectionsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = MyCollectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val responseStringInIntent: Intent = intent
        val messageTextOut = responseStringInIntent.getStringExtra("message")


        binding.titleCollectionActivity.text = messageTextOut.toString()

        lifecycle.coroutineScope
            .launch {
                collectionsViewModel.getAllCollection()
            }
        val layoutManger = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL, false
        )

        binding.myCollectionRecyclerActivity.layoutManager = layoutManger

        val listCollection = mutableListOf<CollectionFilm>()

        collectionsViewModel.collectionFilmsLiveData.observe(this) { list ->
            if (list.isNotEmpty()) {
                val listSize = list.size
                var n = 0
                for (single in list) {
                    n = n + 1
                    if (single.collections.CollectionName == messageTextOut) {
                        single.itemCollectionsList.forEach {
                            if (it.parentCollectionID == single.collections.collectionID) {
                                listCollection.add(it.collectionFilmProfile)
                                if (listSize == n) {
                                    myCollectionFilmAdapter.submitList(listCollection)
                                    binding.myCollectionRecyclerActivity.adapter =
                                        myCollectionFilmAdapter

                                }

                            }
                        }
                    }

                }

            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null

    }

}
