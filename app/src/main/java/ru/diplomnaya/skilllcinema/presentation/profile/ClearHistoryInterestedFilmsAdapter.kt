package ru.diplomnaya.skilllcinema.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.ButtonInterestedHistoryBinding

class ClearHistoryInterestedFilmsAdapter(private val deleteHistoryInterestingObject: (delete: Boolean) -> Unit) :
    RecyclerView.Adapter<ClearHistoryInterestedFilmsAdapter.ButtonViewHolder>() {

    inner class ButtonViewHolder(val binding: ButtonInterestedHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        return ButtonViewHolder(
            ButtonInterestedHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun getItemCount(): Int {
     return 1
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        with(holder.binding) {
            infoStringInterestedObject.text =
                holder.itemView.resources.getString(R.string.clear_history_viewed_films)
            clearInterestedObject.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Удаление истории интересовавших Вас...",
                    Toast.LENGTH_LONG
                ).show()
                deleteHistoryInterestingObject(true)
        }
        }
    }
}