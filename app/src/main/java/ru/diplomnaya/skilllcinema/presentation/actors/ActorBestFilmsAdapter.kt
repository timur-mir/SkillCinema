package ru.diplomnaya.skilllcinema.presentation.actors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.BestFilmsActorBinding
import ru.diplomnaya.skilllcinema.databinding.MoveListBinding
import ru.diplomnaya.skilllcinema.model.entities.Actor
import ru.diplomnaya.skilllcinema.model.entities.ShortFilmInfo
import ru.diplomnaya.skilllcinema.view.MovieHolder

class ActorBestFilmsAdapter(private val onClick: (ShortFilmInfo) -> Unit):
   ListAdapter<ShortFilmInfo,ActorBestFilmsAdapter.ActorBestFilmsHolder>(DiffUtilCallbackActorBestFilmsList()){
    class ActorBestFilmsHolder(val binding: BestFilmsActorBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorBestFilmsHolder {
        return ActorBestFilmsHolder(
           BestFilmsActorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }



    override fun onBindViewHolder(holder: ActorBestFilmsHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            titleBestFilm.text =item.nameFilm
            genresBestFilm.text = item.genre.toString()
            ratingLabelBestFilm.text= item.rating

             ratingLabelBestFilm.bringToFront()

            item?.let {
                Picasso
                    .with(posterPicBestFilm.context)
                    .load(item.urlFilmPic)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicBestFilm)

            }
            holder.binding.root.setOnClickListener {
                 onClick(item)
            }
    }
    }
    class DiffUtilCallbackActorBestFilmsList: DiffUtil.ItemCallback<ShortFilmInfo>(){
        override fun areItemsTheSame(oldItem: ShortFilmInfo, newItem: ShortFilmInfo): Boolean {
           return oldItem.idFilm== newItem.idFilm
        }

        override fun areContentsTheSame(oldItem: ShortFilmInfo, newItem: ShortFilmInfo): Boolean {
        return oldItem==newItem
        }

    }
}
