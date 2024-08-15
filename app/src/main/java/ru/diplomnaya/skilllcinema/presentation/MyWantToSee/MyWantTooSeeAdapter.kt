package ru.diplomnaya.skilllcinema.presentation.MyWantToSee

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.databinding.MyWantToSeeBinding
import ru.diplomnaya.skilllcinema.model.entities.WantToSee

class MyWantToSeeAdapter (private val onClick: (WantToSee) -> Unit):
    ListAdapter<WantToSee, MyWantToSeeHolder>(DiffUtilCallbackMyWantToSee()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWantToSeeHolder {
        return MyWantToSeeHolder(
            MyWantToSeeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyWantToSeeHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            titleWF.text = item?.nameRu ?: ""
            genresWF.text = item?.genres
            ratingLabelWF.text = ""
            item?.let {
                Picasso
                    .with(posterWF.context)
                    .load(it.posterUrlPreview)
                    .into(posterWF)

            }
            holder.binding.root.setOnClickListener{
                onClick(item)
            }
        }
    }


}
class DiffUtilCallbackMyWantToSee : DiffUtil.ItemCallback<WantToSee>() {


    override fun areItemsTheSame(oldItem: WantToSee, newItem: WantToSee): Boolean =
        oldItem.kinopoiskId == newItem.kinopoiskId


    override fun areContentsTheSame(oldItem: WantToSee, newItem: WantToSee): Boolean =
        oldItem == newItem


}
class MyWantToSeeHolder(val binding: MyWantToSeeBinding) : RecyclerView.ViewHolder(binding.root)
{

}
