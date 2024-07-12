package ru.diplomnaya.skilllcinema.presentation.myCollection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MyCollectionBinding
import ru.diplomnaya.skilllcinema.model.database.CollectionFilm


class MyCollectionAdapter (private val onClick: (CollectionFilm) -> Unit):
    ListAdapter<CollectionFilm, MyCollectionHolder>(DiffUtilCallbackMyCollection()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCollectionHolder {
        return MyCollectionHolder(
            MyCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyCollectionHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            titleCF.text = item?.nameRu ?: ""
//            genresCF.text = item?.genres?.joinToString(",") { it.genre.toString() }
            genresCF.text = item?.genres?.toString()
            ratingLabelCF.text = ""
            item?.let {
                Picasso
                    .with(posterCF.context)
                    .load(it.posterUrlPreview)
                    .into(posterCF)

            }
            holder.binding.root.setOnClickListener{
                onClick(item)
            }
        }
    }


}
class DiffUtilCallbackMyCollection : DiffUtil.ItemCallback<CollectionFilm>() {


    override fun areItemsTheSame(oldItem: CollectionFilm, newItem: CollectionFilm): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId


    override fun areContentsTheSame(oldItem: CollectionFilm, newItem: CollectionFilm): Boolean =
        oldItem == newItem


}
class MyCollectionHolder(val binding: MyCollectionBinding) : RecyclerView.ViewHolder(binding.root)
{

}

