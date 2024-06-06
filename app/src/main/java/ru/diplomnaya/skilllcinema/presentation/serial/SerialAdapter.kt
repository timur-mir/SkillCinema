package ru.diplomnaya.skilllcinema.presentation.serial

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import ru.diplomnaya.skilllcinema.databinding.SerialListBinding

import ru.diplomnaya.skilllcinema.model.entities.Episodes



class SerialAdapter:
    ListAdapter<Episodes, SerialAdapter.SerialHolder>(DiffUtilCallbackSerial()) {


    class DiffUtilCallbackSerial : DiffUtil.ItemCallback<Episodes>() {
        override fun areItemsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
        return    oldItem.releaseDate==newItem.releaseDate
        }

        override fun areContentsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
          return  oldItem==newItem
        }

    }
    class SerialHolder(val binding:SerialListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerialHolder {
        return SerialHolder(
            SerialListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SerialHolder, position: Int) {
        with(holder.binding) {
            val item = getItem(position)
            if (item.nameEn?.isNotEmpty() == true) {
                epizodeNumberAndName.text =
                    "${item.episodeNumber.toString()} ${"серия."} ${item.nameEn}"
                epizodeRelease.text = item.releaseDate
            } else {
                if (item.nameRu?.isNotEmpty() == true) {
                    epizodeNumberAndName.text =
                        "${item.episodeNumber.toString()} ${"серия."} ${item.nameRu}"
                    epizodeRelease.text = item.releaseDate
                }
            }
        }
    }
    }