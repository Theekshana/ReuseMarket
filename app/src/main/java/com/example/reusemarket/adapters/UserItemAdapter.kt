package com.example.reusemarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R

class UserItemAdapter(
    private var userItem: List<String>
) : RecyclerView.Adapter<UserItemAdapter.UserItemViewHolder>() {

    inner class UserItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.itemName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserItemViewHolder {

        val view =LayoutInflater.from(parent.context).inflate(R.layout.user_item_list, parent, false)
        return UserItemViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userItem.size
    }

    override fun onBindViewHolder(holder: UserItemViewHolder, position: Int) {

        holder.textView.text = userItem[position]






    }
}