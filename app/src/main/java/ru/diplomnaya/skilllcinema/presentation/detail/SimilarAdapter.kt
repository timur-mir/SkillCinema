package ru.diplomnaya.skilllcinema.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.SimilarListBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsSimilar

class SimilarAdapter(private val onClick: (ItemsSimilar) -> Unit) :
    ListAdapter<ItemsSimilar, SimilarAdapter.ItemsSimilarHolder>(DiffUtilCallbackSimilar()) {
    class DiffUtilCallbackSimilar : DiffUtil.ItemCallback<ItemsSimilar>() {
        override fun areItemsTheSame(oldItem: ItemsSimilar, newItem: ItemsSimilar): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(oldItem: ItemsSimilar, newItem: ItemsSimilar): Boolean {
            return oldItem == newItem
        }
    }

    class ItemsSimilarHolder(val binding: SimilarListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsSimilarHolder {
        return ItemsSimilarHolder(
            SimilarListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsSimilarHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            titleSimilar.text = item?.nameRu ?: item?.nameEn ?: item.nameOriginal
            item.let {
                Picasso
                    .with(posterPicSimilar.context)
                    .load(it?.posterUrl ?: it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicSimilar)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let { it1 -> onClick(it1) }
        }
    }
}