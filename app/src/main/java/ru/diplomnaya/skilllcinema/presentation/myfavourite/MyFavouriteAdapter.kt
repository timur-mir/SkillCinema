package ru.diplomnaya.skilllcinema.presentation.myfavourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.databinding.MyFavouriteBinding
import ru.diplomnaya.skilllcinema.model.entities.Favourite

class MyFavouriteAdapter (private val onClick: (Favourite) -> Unit):
    ListAdapter<Favourite, MyFavouriteHolder>(DiffUtilCallbackMyFavourite()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyFavouriteHolder {
        return MyFavouriteHolder(
            MyFavouriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyFavouriteHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            titleFF.text = item?.nameRu ?: ""
            genresFF.text = item?.genres
            ratingLabelFF.text = ""
            item?.let {
                Picasso
                    .with(posterFF.context)
                    .load(it.posterUrlPreview)
                    .into(posterFF)

            }
            holder.binding.root.setOnClickListener{
                onClick(item)
            }
        }
    }


}
class DiffUtilCallbackMyFavourite : DiffUtil.ItemCallback<Favourite>() {


    override fun areItemsTheSame(oldItem: Favourite, newItem: Favourite): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId


    override fun areContentsTheSame(oldItem: Favourite, newItem: Favourite): Boolean =
        oldItem == newItem


}
class MyFavouriteHolder(val binding: MyFavouriteBinding) : RecyclerView.ViewHolder(binding.root)
{

}
