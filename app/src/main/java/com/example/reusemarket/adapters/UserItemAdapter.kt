package com.example.reusemarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reusemarket.databinding.UserItemListBinding
import com.example.reusemarket.model.AllItem

class UserItemAdapter(
    private var userItems: List<AllItem>,
) : RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {




    inner class UserItemViewHolder(val binding: UserItemListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {

        val itemView =
            UserItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserItemViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return userItems.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {
        val currentItem: AllItem = userItems[position]
        holder.binding.itemName.text = currentItem.name

        currentItem.image_url?.let { imageUrl ->
            Glide.with(holder.itemView)
                .load(imageUrl)
                .into(holder.binding.itemImage)

        }


    }
}