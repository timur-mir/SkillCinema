package ru.diplomnaya.skilllcinema.view.FullUsesAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.TvSeriesListBinding
import ru.diplomnaya.skilllcinema.model.Movie


class AnyCountriesAndGenresTv_SeriesAdapter(private val onClick: (Movie) -> Unit) :
    PagingDataAdapter<Movie, AnyCountriesAndGenresTv_SeriesHolder>(
        DiffUtilCallbackTv_SeriesList()
    ) {
    override fun onBindViewHolder(holder: AnyCountriesAndGenresTv_SeriesHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            gradientViewTv.visibility = View.INVISIBLE
            titleTvSeries.text = item?.nameRu ?: ""
            genresTvSeries.text = item?.genres?.joinToString(",") { it.genre.toString() }
            ratingLabelTvSeries.text = item?.rating
            ratingLabelTvSeries.bringToFront()
            if(item!=null) {
                if (item.viewed) {
                   gradientViewTv.visibility = View.VISIBLE
                    gradientViewTv.bringToFront()
                }
            }
            holder.binding.root.setOnClickListener {
                if (item != null) {
                    onClick(item)
                }
            }
            item?.let {
                Picasso
                    .with(posterPicTvSeries.context)
                    .load(it.posterUrlPreview)
                    .placeholder(R.drawable.gradient2)
                    .into(posterPicTvSeries)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnyCountriesAndGenresTv_SeriesHolder {
        return AnyCountriesAndGenresTv_SeriesHolder(
            TvSeriesListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

}

class DiffUtilCallbackTv_SeriesList : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean =
        oldItem == newItem

}

class AnyCountriesAndGenresTv_SeriesHolder(val binding: TvSeriesListBinding) :
    RecyclerView.ViewHolder(binding.root)