package ru.diplomnaya.skilllcinema.presentation.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.WorkedOnFilmListBinding
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred


class WorkedOnFilmAdapter(private val onClick: (StaffStarred) -> Unit) :
    PagingDataAdapter<StaffStarred, WorkedOnFilmAdapter.WorkedHolder>(DiffUtilCallbackWorked()) {
    class DiffUtilCallbackWorked : DiffUtil.ItemCallback<StaffStarred>() {
        override fun areItemsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean {
            return oldItem.staffId == newItem.staffId
        }

        override fun areContentsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean {
            return oldItem == newItem
        }
    }

    class WorkedHolder(val binding: WorkedOnFilmListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: WorkedHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            staffNameInFilm.text = item?.nameRu ?: item?.nameEn
            professionKeyInFilm.text = item?.professionKey
            item.let {
                Picasso
                    .with(staffPicOutsideTheFilm.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.gradient2)
                    .into(staffPicOutsideTheFilm)
            }

            holder.binding.root.setOnClickListener {
                onClick(item!!)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkedHolder {
        return WorkedOnFilmAdapter.WorkedHolder(
            WorkedOnFilmListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}