package com.example.reusemarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R
import com.example.reusemarket.databinding.UserItemListBinding
import com.example.reusemarket.model.UserItem
import com.example.reusemarket.views.data.AddItemFragment

class UserItemAdapter(
    private var userItem: List<UserItem>,
) : RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {

    inner class UserItemViewHolder(val binding: UserItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {

        val itemView =
            UserItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserItemViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return userItem.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {

        // holder.textView.text = userItem[position]
        val currentItem: UserItem = userItem[position]

        val tvNumber: TextView = holder.itemView.findViewById(R.id.itemName)

        tvNumber.text = currentItem.text1


    }
}