package com.example.reusemarket.views.viewItem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.reusemarket.databinding.FragmentViewItemBinding
import com.example.reusemarket.model.AllItem

class ViewItemFragment : Fragment() {

    private var item: AllItem? = null
    private lateinit var fragmentViewItemBinding: FragmentViewItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        fragmentViewItemBinding = FragmentViewItemBinding.inflate(inflater, container, false)
        return fragmentViewItemBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val itemTemp = it.getParcelable<AllItem>("item")
            if (itemTemp != null) {
                item = itemTemp
                fillData()
            }
        }

    }

    private fun fillData() {
        item?.let { item ->
            fragmentViewItemBinding.txtItemName.text = item.name
            Glide.with(requireContext())
                .load(item.image_url)
                .into(fragmentViewItemBinding.itemImageView)
        }
    }


}