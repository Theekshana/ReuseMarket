package com.example.reusemarket.views.data

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reusemarket.R
import com.example.reusemarket.constants.gone
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.FragmentAddItemBinding
import com.example.reusemarket.model.AllItem


class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var viewModel: AddItemViewModel
    private var imageUri: Uri? = null

    private var selectedCategory: String = ""

    private var item = AllItem()


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


        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = arrayAdapter.getItem(position).toString()
            Log.d("AddItemFragment", "Selected Category: $selectedCategory")
        }

        binding.imageButton.setOnClickListener {
            resultContent.launch("image/*")
            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            //captureImage()

        }
        binding.btnAddItem.setOnClickListener {

            val name = binding.etItemName.text.toString()

            val itemMarketItem = item.copy(
                itemImage = imageUri,
                name = name,
                category = selectedCategory

            )

            if (item.itemId.isNullOrEmpty()) {
                viewModel.addItemToFirestore(itemMarketItem)
            } else {
                viewModel.updateItemData(itemMarketItem)
            }

            Log.e("TAG", "Data Added")
            
        }


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

    private fun captureImage() {
        // Open camera


    }

    private val resultContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
            result?.let { imageUri ->
                // Display the selected image in an ImageView using Glide or any other library
                Glide.with(this)
                    .load(imageUri)
                    .into(binding.itemImageView)
                binding.imageButton.visibility = View.GONE

                // Save the selected image URI in a property for later use
                this.imageUri = imageUri
            }
        }


    private fun showRotationalDialogForPermissions() {
        AlertDialog.Builder(requireContext()).setMessage("you have turend off permissions")
            .setPositiveButton("Go to settings") { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", requireActivity().packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }

            }
            .setNegativeButton("CANCEL") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }


}
