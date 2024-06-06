package ru.diplomnaya.skilllcinema.presentation.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.diplomnaya.skilllcinema.R
import ru.diplomnaya.skilllcinema.databinding.InterestedFilmsListBinding
import ru.diplomnaya.skilllcinema.databinding.InterestedStaffListBinding
import ru.diplomnaya.skilllcinema.model.database.InterestedFilmsEntity
import ru.diplomnaya.skilllcinema.model.database.InterestedStaffEntity

class InterestedStaffsAdapter(private val interestedStaffList: List<InterestedStaffEntity>,
                              private val onClick: (InterestedStaffEntity) -> Unit) :
RecyclerView.Adapter<InterestedStaffsAdapter.InterestedStaffHolder>() {

        inner class InterestedStaffHolder(val binding: InterestedStaffListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterestedStaffHolder {
      return  InterestedStaffHolder(
        InterestedStaffListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        )
    }


    override fun getItemCount(): Int {
       return interestedStaffList.size
    }

    override fun onBindViewHolder(holder: InterestedStaffHolder, position: Int) {
        val item = interestedStaffList[position]
        with(holder.binding){
            interestedStaffName.text=item.nameRu
            interestedStaffNameInFilm.text=item.professionKey
            item?.let {
                Picasso
                    .with(interestedStaffPic.context)
                    .load(it.posterUrl)
                    .placeholder(R.drawable.gradient2)
                    .into(interestedStaffPic)

            }
            holder.binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }


}