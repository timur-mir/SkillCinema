package ru.diplomnaya.skilllcinema.view.FullUsesAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.DifferentFilmsSecondVariantBinding

import ru.diplomnaya.skilllcinema.model.Movie

class AnyCountriesAndGenresListAdapterSecondVariant(private val onClick: (Movie) -> Unit):
    PagingDataAdapter<Movie, AnyFilmsHolderSecondVariant>(DiffUtilCallbackAnyCountriesAndGenresListSecondVariant()) {
    override fun onBindViewHolder(holder: AnyFilmsHolderSecondVariant, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewAnyCountriesGenresSecond.visibility = View.INVISIBLE
            titleAnyFilmsSecondVariant.text = item?.nameRu ?: ""
            anyGenresSecondVariant.text = item?.genres?.joinToString(",") { it.genre.toString() }
            FilmGenreAndCountrySecondVariant.countryNameSecondVariant= if(item?.countries?.isEmpty() == true) "Страна:" else {item?.countries?.get(0)?.country.toString()}
            FilmGenreAndCountrySecondVariant.genreNameSecondVariant= if(item?.genres?.isEmpty() == true) "жанр:" else {item?.genres?.get(0)?.genre.toString()}
            if(item!=null) {
                if (item.viewed) {
                    gradientViewAnyCountriesGenresSecond.visibility = View.VISIBLE
                    gradientViewAnyCountriesGenresSecond.bringToFront()
                }
            }
                    holder.binding.root.setOnClickListener{
                if (item != null) {
                    onClick(item)
                }
            }
            item?.let {
                Picasso
                    .with(posterPicAnyFilmsSecondVariant.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicAnyFilmsSecondVariant)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnyFilmsHolderSecondVariant {
        return AnyFilmsHolderSecondVariant(
            DifferentFilmsSecondVariantBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}
class DiffUtilCallbackAnyCountriesAndGenresListSecondVariant : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}

class AnyFilmsHolderSecondVariant (val binding:DifferentFilmsSecondVariantBinding) :
    RecyclerView.ViewHolder(binding.root)
object FilmGenreAndCountrySecondVariant {
    var countryNameSecondVariant:String="Страна"
    var genreNameSecondVariant:String="Жанр"
}