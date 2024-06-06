package ru.diplomnaya.skilllcinema.presentation.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.databinding.ImageListForGalleryBinding
import ru.diplomnaya.skilllcinema.model.entities.ItemsImages

class GalleryAdapter(private val onClick: (String) -> Unit) :
    ListAdapter<ItemsImages, GalleryAdapter.GalleryHolder>(DiffUtilCallbackImageForGallery()) {


    class DiffUtilCallbackImageForGallery : DiffUtil.ItemCallback<ItemsImages>() {
        override fun areItemsTheSame(oldItem: ItemsImages, newItem: ItemsImages): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(oldItem: ItemsImages, newItem: ItemsImages): Boolean {
            return oldItem == newItem
        }

    }

    class GalleryHolder(val binding: ImageListForGalleryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryHolder {
        return GalleryHolder(
            ImageListForGalleryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            item?.let {
                Picasso
                    .with(imageOfAnyType.context)
                    .load(it.previewUrl)
                    .into(imageOfAnyType)

            }
            holder.binding.root.setOnClickListener {
                item.imageUrl?.let { it1 -> onClick(it1) }?:item.previewUrl
            }
        }
    }
}
