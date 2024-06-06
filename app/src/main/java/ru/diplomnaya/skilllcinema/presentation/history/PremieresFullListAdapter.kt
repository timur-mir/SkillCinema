package ru.diplomnaya.skilllcinema.view.FullUsesAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.MoveListBinding
import ru.diplomnaya.skilllcinema.databinding.PremieresListBinding
import ru.diplomnaya.skilllcinema.model.Movie


class PremieresFullListAdapter (private val onClick: (Movie) -> Unit):
    ListAdapter<Movie,PremieresFullListHolder>(DiffUtilCallbackPremieresFullList()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PremieresFullListHolder {
        return PremieresFullListHolder(
           PremieresListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PremieresFullListHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewViewedListFilms.visibility = View.INVISIBLE
            titlePremieres.text = item?.nameRu ?: ""
            genresPreemieres.text = item?.genres?.joinToString(",") { it.genre.toString() }
            ratingLabelPremieres.text = item?.rating
            if(item!=null) {
                if (item.viewed) {
               gradientViewViewedListFilms.visibility = View.VISIBLE
               gradientViewViewedListFilms.bringToFront()
                }
            }
            item?.let {
                Picasso
                    .with(posterPremieres.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPremieres)

            }
            holder.binding.root.setOnClickListener{
                onClick(item)
            }
        }


    }
}
class DiffUtilCallbackPremieresFullList : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}
class PremieresFullListHolder(val binding: PremieresListBinding) : RecyclerView.ViewHolder(binding.root)