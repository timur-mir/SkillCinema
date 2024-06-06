package ru.diplomnaya.skilllcinema.presentation.filmography

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.databinding.FilmographyListBinding
import ru.diplomnaya.skilllcinema.model.entities.ShortInfoActors
class ActorFilmographyAdapter (private val onClick: (ShortInfoActors) -> Unit) :
    ListAdapter<ShortInfoActors, ActorFilmographyAdapter.ActorFilmographyHolder>(DiffUtilCallbackActorFilmography()){

    class DiffUtilCallbackActorFilmography : DiffUtil.ItemCallback<ShortInfoActors>() {
        override fun areItemsTheSame(oldItem: ShortInfoActors, newItem: ShortInfoActors): Boolean {
            return oldItem.filmId == newItem.filmId
        }

        override fun areContentsTheSame(oldItem: ShortInfoActors, newItem: ShortInfoActors): Boolean {
            return oldItem == newItem
        }

    }
    class ActorFilmographyHolder(val binding:FilmographyListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorFilmographyHolder {
        return ActorFilmographyHolder(
            FilmographyListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorFilmographyHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            ratingLabelFilmography.text=item.rating
            ratingLabelFilmography.bringToFront()
            titleFilmography.text=item.nameRu?:item.nameEn
            genresFilmography.text=item.genre
            item?.let {
                Picasso
                    .with(posterPicFilmography.context)
                    .load(it.urlFilmPic)
                    .into(posterPicFilmography)

            }
            holder.binding.root.setOnClickListener {
               onClick(item)
            }
        }
    }
}