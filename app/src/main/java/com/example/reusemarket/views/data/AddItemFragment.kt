package com.example.reusemarket.views.data

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.reusemarket.R
import com.example.reusemarket.adapters.AllItemAdapter
import com.example.reusemarket.cameraX.MyCameraActivity
import com.example.reusemarket.constants.gone
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.FragmentAddItemBinding
import com.example.reusemarket.databinding.ImageCaptureBottomSheetBinding
import com.example.reusemarket.model.AllItem
import com.google.android.material.bottomsheet.BottomSheetDialog


class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var viewModel: AddItemViewModel
    private var imageUri: Uri? = null

    //private var selectedCategory: String = ""

    private var item = AllItem()

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { setImage(it) }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[AddItemViewModel::class.java]

        viewModel.toastMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                viewModel.clearToastMessage()
            }
        }


        return binding.root
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

        viewModel.loadingState.observeForever {

            if (it) {
                disableEnableControls(false, binding.addItemLayout)
                binding.btnAddItem.isEnabled = false
                binding.progressBar.show()
            } else {
                disableEnableControls(true, binding.addItemLayout)
                binding.btnAddItem.isEnabled = true
                binding.progressBar.gone()

            }

        }

        val categories = resources.getStringArray(R.array.categories)

        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)

        binding.autoCompleteTextView.setAdapter(arrayAdapter)

       /* binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = arrayAdapter.getItem(position).toString()
            Log.d("AddItemFragment", "Selected Category: $selectedCategory")
        }*/

        binding.imageButton.setOnClickListener {

            showSelectImageModal()

        }

        binding.btnAddItem.setOnClickListener {

            val name = binding.etItemName.text.toString()
            val location = binding.etLocation.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val description = binding.etDescription.text.toString()
            val selectedCategory = binding.autoCompleteTextView.text.toString()

            val itemMarketItem = item.copy(
                itemImage = imageUri,
                name = name,
                category = selectedCategory,
                location = location,
                phoneNumber = phoneNumber,
                description = description

            )

            if (item.itemId.isNullOrEmpty()) {
                viewModel.addItemToFirestore(itemMarketItem)
            } else {
                viewModel.updateItemData(itemMarketItem)
            }

            Log.e("TAG", "Data Added")

        }


    }

    private fun fillData() {
        binding.etItemName.setText(item.name)
        binding.autoCompleteTextView.setText(item.category)

        Glide.with(this)
            .load(item.image_url)
            .into(binding.itemImageView)

    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    private fun showSelectImageModal() {
        val dialog = BottomSheetDialog(requireContext())
        val view = ImageCaptureBottomSheetBinding.inflate(layoutInflater)
        view.btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

        view.btnGallery.setOnClickListener {
            resultContent.launch("image/*")
            dialog.dismiss()
        }
        view.btnCamera.setOnClickListener {
            captureImage()
            dialog.dismiss()
        }

        dialog.setCancelable(false)
        dialog.setContentView(view.root)
        dialog.show()
    }

    private fun captureImage() {
        val cameraIntent = Intent(requireContext(), MyCameraActivity::class.java)
        activityResultLauncher.launch(cameraIntent)
    }

    private val resultContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { imageUri ->
                // Display the selected image in an ImageView using Glide or any other library
                setImage(imageUri)


            }
        }

    private fun setImage(imageUri: Uri) {
        Glide.with(this)
            .load(imageUri)
            .into(binding.itemImageView)
        binding.imageButton.visibility = View.GONE

        // Save the selected image URI in a property for later use
        this.imageUri = imageUri
    }




}
