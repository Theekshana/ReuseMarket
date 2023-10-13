package com.example.reusemarket.views.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reusemarket.databinding.SearchItemBinding
import com.example.reusemarket.model.AllItem

class SearchItemAdapter(private val list: List<AllItem>) :
    RecyclerView.Adapter<SearchItemAdapter.SearchItemViewHolder>() {

    var onItemClickedListener: OnItemClickedListener? = null

    interface OnItemClickedListener {
        fun onItemClicked(item: AllItem)
    }

    inner class SearchItemViewHolder(val binding: SearchItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchItemViewHolder {
        val itemView =
            SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchItemViewHolder(itemView)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SearchItemViewHolder, position: Int) {
        val currentItem: AllItem = list[position]
        holder.binding.itemName.text = currentItem.name
        holder.binding.txtLocation.text = currentItem.category
        currentItem.image_url?.let { imageUrl ->
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(holder.binding.itemImage)

        }
        holder.itemView.setOnClickListener {
            onItemClickedListener?.onItemClicked(currentItem)
        }
    }

}