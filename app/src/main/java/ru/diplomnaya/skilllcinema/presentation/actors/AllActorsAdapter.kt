package ru.diplomnaya.skilllcinema.presentation.actors

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.AllActorsFragmentListBinding
import ru.diplomnaya.skilllcinema.model.entities.StaffStarred

class AllActorsAdapter(private val onClick: (StaffStarred) -> Unit) :
    ListAdapter<StaffStarred, AllActorsAdapter.ActorsFullListHolder>(
        DiffUtilCallbackActorsFullList()
    ) {


    class DiffUtilCallbackActorsFullList : DiffUtil.ItemCallback<StaffStarred>() {
        override fun areItemsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean =
            oldItem.staffId == newItem.staffId

        override fun areContentsTheSame(oldItem: StaffStarred, newItem: StaffStarred): Boolean =
            oldItem == newItem

    }

    class ActorsFullListHolder(val binding: AllActorsFragmentListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorsFullListHolder {
        return ActorsFullListHolder(
            AllActorsFragmentListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ActorsFullListHolder, position: Int) {
        val item = getItem(position)

        with(holder.binding) {
            actorNameVariant.text = item?.nameRu ?: item?.nameEn

            nameInFilmVariant.text = item?.description ?: item?.professionKey

            item.let {
                Picasso
                    .with(actorPicVariant.context)
                    .load(it?.posterUrl)
                    .placeholder(R.drawable.gradient)
                    .into(actorPicVariant)
            }
            holder.binding.root.setOnClickListener {
                item?.let { it1 -> onClick(it1) }
            }
        }
    }
}