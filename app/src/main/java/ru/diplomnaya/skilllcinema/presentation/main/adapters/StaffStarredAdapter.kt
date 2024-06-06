package ru.diplomnaya.skilllcinema.presentation.main.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.StaffStarredListBinding
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred


class StaffStarredAdapter(private val onClick: (StaffStarred) -> Unit) :
    PagingDataAdapter<StaffStarred, StaffStarredAdapter.StaffStarredHolder>(
        DiffUtilCallbackStarredStaff()
    ) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffStarredHolder {

        return StaffStarredHolder(
            StaffStarredListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: StaffStarredHolder, position: Int) {

        val item = getItem(position)

        with(holder.binding) {
            actorName.text = item?.nameRu ?: item?.nameEn

            nameInFilm.text = item?.description

            item.let {
                Picasso
                    .with(actorPic.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.gradient2)
                    .into(actorPic)
            }

            holder.binding.root.setOnClickListener {
                item?.let { it1 -> onClick(it1) }
            }

        }
    }

    class DiffUtilCallbackStarredStaff : DiffUtil.ItemCallback<StaffStarred>() {
        override fun areItemsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean {
            return oldItem.staffId == newItem.staffId
        }

        override fun areContentsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean {
            return oldItem == newItem
        }
    }

    class StaffStarredHolder(val binding: StaffStarredListBinding) :
        RecyclerView.ViewHolder(binding.root)


    object forLoop {
        var itemCountLoop = 0
    }
}

