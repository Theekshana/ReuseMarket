package com.example.reusemarket.views.viewItem

import android.content.Intent
import android.net.Uri
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
            fragmentViewItemBinding.txtLocation.text = item.location
            fragmentViewItemBinding.txtCategory.text = item.category
            fragmentViewItemBinding.txtMultiLine.text = item.description
            Glide.with(requireContext())
                .load(item.image_url)
                .into(fragmentViewItemBinding.itemImageView)

            fragmentViewItemBinding.imgPhone.setOnClickListener {
                item.phoneNumber?.let { it1 -> callUser(it1) }

            }
            fragmentViewItemBinding.imgEmail.setOnClickListener {

                item.name?.let { it1 ->
                    sendEmail(
                        item.email.orEmpty(),
                        it1, "Can I buy this item"
                    )
                }

            }
        }
    }

    private fun sendEmail(email: String, subject: String, message: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)
        emailIntent.type = "message/rfc822"
        startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"))
    }

    private fun callUser(number: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$number")
        startActivity(dialIntent)
    }


}