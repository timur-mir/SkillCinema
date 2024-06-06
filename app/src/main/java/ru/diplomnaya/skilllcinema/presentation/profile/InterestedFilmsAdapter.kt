package ru.diplomnaya.skilllcinema.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R

import ru.diplomnaya.skilllcinema.databinding.ButtonInterestedHistoryBinding
import ru.diplomnaya.skilllcinema.databinding.InterestedFilmsListBinding

import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity


class InterestedFilmsAdapter(
    private val interestedFilmsList: List<InterestedFilmsEntity>,
    private val onClick: (InterestedFilmsEntity) -> Unit
) :
    RecyclerView.Adapter<InterestedFilmsAdapter.InterestedFilmHolder>() {

    inner class InterestedFilmHolder(val binding: InterestedFilmsListBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):InterestedFilmHolder {
       return InterestedFilmHolder(
            InterestedFilmsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InterestedFilmHolder, position: Int) {
        val item = interestedFilmsList[position]
        with(holder.binding) {
            ratingLabelInterestedFilm.text = item.rating
            titleInterestedFilm.text = item.nameRu
            genresInterestedFilm.text = item.genres
            item.let {
                Picasso
                    .with(posterPicInterestedFilm.context)
                    .load(it.posterUrl)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicInterestedFilm)

            }
            holder.binding.root.setOnClickListener {
                onClick(item)
            }

        }
    }


    override fun getItemCount(): Int {
        return interestedFilmsList.size
    }


}