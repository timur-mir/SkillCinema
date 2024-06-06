package ru.diplomnaya.skilllcinema.presentation.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ImageAvatarBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages

class ImageAdapter(private val onClick: (ItemsImages) -> Unit):
    ListAdapter<ItemsImages ,ImageAdapter.ItemsImagesHolder>(DiffUtilCallbackImages())
{


    class DiffUtilCallbackImages: DiffUtil.ItemCallback<ItemsImages>() {
        override fun areItemsTheSame(oldItem: ItemsImages, newItem: ItemsImages): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: ItemsImages, newItem: ItemsImages): Boolean {
            return oldItem == newItem
        }
    }
        class ItemsImagesHolder(val binding: ImageAvatarBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsImagesHolder{
        return ItemsImagesHolder(
            ImageAvatarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemsImagesHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            item.let {
                Picasso
                    .with(filmImage.context)
                    .load(it?.previewUrl ?:it.imageUrl)
                    .placeholder(R.drawable.gradient2)
                    .into(filmImage)
            }
        }
        holder.binding.root.setOnClickListener {
            item?.let { it1 -> onClick(it1) }
        }
    }
}