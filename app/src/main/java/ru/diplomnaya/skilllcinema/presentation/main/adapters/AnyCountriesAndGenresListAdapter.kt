package ru.diplomnaya.skilllcinema.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.DifferentsFilmsBinding
import ru.diplomnaya.skilllcinema.model.Movie

class AnyCountriesAndGenresListAdapter(private val onClick: (Movie) -> Unit): PagingDataAdapter<Movie,AnyFilmsHolder>(DiffUtilCallbackAnyCountriesAndGenresList()) {

    override fun onBindViewHolder(holder: AnyFilmsHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewAnyCountriesGenres.visibility = View.INVISIBLE
            titleAnyFilms.text = item?.nameRu ?: ""
          anyGenres.text = item?.genres?.joinToString(",") { it.genre.toString() }
            FilmGenreAndCountry.countryName= if(item?.countries?.isEmpty() == true) "Страна" else {item?.countries?.get(0)?.country.toString()}
//            FilmGenreAndCountry.genreName= item?.genres?.joinToString(",") { it.genre.toString() }
            FilmGenreAndCountry.genreName= if(item?.genres?.isEmpty() == true) "Жанр" else {item?.genres?.get(0)?.genre.toString()}
            if (item != null) {
                if (item.viewed) {
                    gradientViewAnyCountriesGenres.visibility = View.VISIBLE
                    gradientViewAnyCountriesGenres.bringToFront()
                }
            }

            holder.binding.root.setOnClickListener{
                if (item != null) {
                    onClick(item)
                }
            }
            item?.let {
                Picasso
                    .with(posterPicAnyFilms.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicAnyFilms)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyFilmsHolder {
        return AnyFilmsHolder(
            DifferentsFilmsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}
class DiffUtilCallbackAnyCountriesAndGenresList : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}

class AnyFilmsHolder (val binding:DifferentsFilmsBinding) :
    RecyclerView.ViewHolder(binding.root)
object FilmGenreAndCountry {
    var countryName:String="Страна"
    var genreName:String="Жанр"


}
