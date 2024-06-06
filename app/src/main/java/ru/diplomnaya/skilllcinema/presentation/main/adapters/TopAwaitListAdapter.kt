package ru.diplomnaya.skilllcinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.TopAwaitFilmsListBinding
import ru.diplomnaya.skilllcinema.model.Movie

class TopAwaitListAdapter(private val onClick: (Movie) -> Unit) :
    PagingDataAdapter<Movie, TopAwaitListHolder>(DiffUtilCallbackTopAwaitList()) {

    override fun onBindViewHolder(holder: TopAwaitListHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewTopAwait.visibility = View.INVISIBLE
           titleAwait.text = item?.nameRu ?: ""
            genresAwait.text = item?.genres?.joinToString(",") { it.genre.toString() }
            ratingLabelAwaitFilms.text = item?.rating
            ratingLabelAwaitFilms.bringToFront()
            if(item!=null) {
                if (item.viewed) {
                    gradientViewTopAwait.visibility = View.VISIBLE
                   gradientViewTopAwait.bringToFront()
                }
            }
            holder.binding.root.setOnClickListener {
                if (item != null) {
                    onClick(item)
                }
            }
            item?.let {
                Picasso.with(posterPicAwait.context).load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2).
                into(posterPicAwait)

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopAwaitListHolder {
        return TopAwaitListHolder(
            TopAwaitFilmsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}

class DiffUtilCallbackTopAwaitList : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem

}

class TopAwaitListHolder(val binding: TopAwaitFilmsListBinding) :
    RecyclerView.ViewHolder(binding.root)