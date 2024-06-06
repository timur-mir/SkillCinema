package ru.diplomnaya.skilllcinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MoveListBinding
import ru.diplomnaya.skilllcinema.model.Movie

class PremieresAdapter(private val onClick: (Movie) -> Unit) :
    ListAdapter<Movie, MovieHolder>(DiffUtilCallbackPremieres()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
        return MovieHolder(
            MoveListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewPremieres.visibility = View.INVISIBLE
            title.text = item?.nameRu ?: ""
            genres.text = item?.genres?.joinToString(",") { it.genre.toString() }
            ratingLabel.text = ""
            ratingLabel.bringToFront()
            if (item.viewed == true) {
                gradientViewPremieres.visibility = View.VISIBLE
                gradientViewPremieres.bringToFront()
            }

            item?.let {
                Picasso
                    .with(posterPic.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPic)

            }
            holder.binding.root.setOnClickListener {
                onClick(item)
            }


        }
    }

}

class DiffUtilCallbackPremieres : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem
}

class MovieHolder(val binding: MoveListBinding) : RecyclerView.ViewHolder(binding.root)