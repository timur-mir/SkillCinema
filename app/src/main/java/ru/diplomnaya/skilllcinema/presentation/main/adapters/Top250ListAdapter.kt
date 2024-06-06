package ru.diplomnaya.skilllcinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.Top250ListBinding
import ru.diplomnaya.skilllcinema.model.Movie

class Top250ListAdapter(private val onClick: (Movie) -> Unit): PagingDataAdapter<Movie,Top250ListHolder>(DiffUtilCallbackTop250List()) {


    override fun onBindViewHolder(holder: Top250ListHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewTop250.visibility = View.INVISIBLE
            titleTop250.text = item?.nameRu ?: ""
            genresTop250.text = item?.genres?.joinToString(",") { it.genre.toString() }
            ratingLabelTop250.text=item?.rating
            ratingLabelTop250.bringToFront()
            if(item!=null) {
                if (item.viewed) {
                    gradientViewTop250.visibility = View.VISIBLE
                    gradientViewTop250.bringToFront()
                }
            }
            holder.binding.root.setOnClickListener{
                if (item != null) {
                    onClick(item)
                }
            }
            item?.let {
                Picasso
                    .with(posterPic250.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPic250)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Top250ListHolder {
        return Top250ListHolder(
            Top250ListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
class DiffUtilCallbackTop250List : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}
class Top250ListHolder(val binding:Top250ListBinding) :
    RecyclerView.ViewHolder(binding.root)