package com.example.reusemarket.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R
import com.example.reusemarket.databinding.AllItemBinding
import com.example.reusemarket.model.AllItem

class AllItemAdapter(private var allItem: List<AllItem>) :
    RecyclerView.Adapter<AllItemAdapter.AllItemViewHolder>() {


    inner class AllItemViewHolder(val binding: AllItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllItemViewHolder {
        val itemView =
            AllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AllItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return allItem.size
    }

    override fun onBindViewHolder(holder: AllItemViewHolder, position: Int) {
        val currentItem: AllItem = allItem[position]

        val tvNumber: TextView = holder.itemView.findViewById(R.id.itemName)
        tvNumber.text = currentItem.text1

        val location: TextView = holder.itemView.findViewById(R.id.txtLocation)
        location.text = currentItem.text2

    }


}