package ru.diplomnaya.skilllcinema.view.FullUsesAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ButtonHistoryBinding
import ru.diplomnaya.skilllcinema.databinding.ViewedMovieListBinding
import ru.diplomnaya.skilllcinema.model.database.AlreadyViewedEntity


class AlreadyViewedAdapterForConcatAdapter(private val onClick: (AlreadyViewedEntity,) -> Unit, private val deleteHistory: (delete: Boolean) -> Unit) :
    ListAdapter<AlreadyViewedEntity, RecyclerView.ViewHolder>(
        DiffUtilCallbackAlreadyViewedFilms()
    ) {


    inner class DataViewHolder(val binding: ViewedMovieListBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ButtonViewHolder(val binding: ButtonHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }

    override fun getItemViewType(position: Int) = when {
        position == currentList.size -> TYPE_FOOTER
        else -> TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        TYPE_ITEM -> DataViewHolder(
            ViewedMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        TYPE_FOOTER ->ButtonViewHolder(
            ButtonHistoryBinding.inflate(LayoutInflater.from(parent.context),
            parent,false)
        )

        else -> {
            throw RuntimeException("there is no type that matches the type $viewType," +
                    "make sure your using types correctly")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is DataViewHolder->{
                if(position == currentList.size){

                }
            else {
                val item = getItem(position)
                with(holder.binding) {
                    viewedMovieTitle.text = item?.nameRu ?: ""
                    viewedMovieGenres.text = item?.genres
                    viewedMovieRatingLabel.text = item.ratingImdb
                    viewedMovieRatingLabel.bringToFront()
                    if(item.viewed){
                        gradientView.bringToFront()
                        gradientView.visibility= View.VISIBLE
                    }

                    item?.let {
                        Picasso
                            .with(viewedMoviePosterPic.context)
                            .load(it.posterUrlPreview)
                            .placeholder(R.drawable.gradient2)
                            .into(viewedMoviePosterPic)

                    }
                    holder.binding.root.setOnClickListener {
                        onClick(item)
                    }
                }

                }
            }
            is ButtonViewHolder->{
                with(holder.binding) {
                 infoString.text = holder.itemView.resources.getString(R.string.clear_history_viewed_films)
                    clearButton.setOnClickListener {
                        Toast.makeText(
                           holder.itemView.context, "Удаление истории", Toast.LENGTH_LONG
                        ).show()
                        deleteHistory(true)

                    }
                }
            }
        }
    }


    companion object {
        private const val TYPE_ITEM = 1
        private const val TYPE_FOOTER = 2
    }
}

class  DiffUtilCallbackAlreadyViewedFilms(): DiffUtil.ItemCallback<AlreadyViewedEntity>() {

    override fun areItemsTheSame(
        oldItem: AlreadyViewedEntity,
        newItem: AlreadyViewedEntity
    ): Boolean = oldItem.kinopoiskId == newItem.kinopoiskId

    override fun areContentsTheSame(
        oldItem: AlreadyViewedEntity,
        newItem: AlreadyViewedEntity
    ): Boolean = oldItem == newItem


}


