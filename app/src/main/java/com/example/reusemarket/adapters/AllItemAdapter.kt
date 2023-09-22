package com.example.reusemarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reusemarket.databinding.AllItemBinding
import com.example.reusemarket.model.AllItem

class AllItemAdapter(private val AllItemList: ArrayList<AllItem>) :
    RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {

    var onItemClickedListener: OnItemClickedListener? = null

    interface OnItemClickedListener {
        fun onItemClicked(item: AllItem)
    }

    inner class AllItemViewHolder(val binding: AllItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val itemView =
            AllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = AllItemList.size

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        //val currentItem: AllItem = allItem[position]
        val currentItem: AllItem = AllItemList[position]

        holder.binding.itemName.text = currentItem.name
        holder.binding.txtLocation.text = currentItem.category
        currentItem.image_url?.let { imageUrl ->
            val imageUriString = imageUrl.toString()
            Glide.with(holder.itemView)
                .load(imageUriString)
                .into(holder.binding.itemImage)

        }

        holder.itemView.setOnClickListener {
            onItemClickedListener?.onItemClicked(currentItem)
        }


    }
}


