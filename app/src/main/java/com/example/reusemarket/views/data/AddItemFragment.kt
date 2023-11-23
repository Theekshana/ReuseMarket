package com.example.reusemarket.views.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.R
import com.example.reusemarket.cameraX.MyCameraActivity
import com.example.reusemarket.constants.NetworkUtils
import com.example.reusemarket.constants.NoInternetErrorDialogFragment
import com.example.reusemarket.constants.gone
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.FragmentAddItemBinding
import com.example.reusemarket.databinding.ImageCaptureBottomSheetBinding
import com.example.reusemarket.model.AllItem
import com.google.android.material.bottomsheet.BottomSheetDialog

/**
 * A fragment for adding a new item or editing an existing one.
 */
class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var viewModel: AddItemViewModel
    private var imageUri: Uri? = null

    private var selectedCategory: String = ""

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

        (requireActivity() as HomeActivity).findViewById<View>(R.id.bottomNavigationView)?.visibility =
            View.GONE


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

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCategory = arrayAdapter.getItem(position).toString()
            Log.d("AddItemFragment", "Selected Category: $selectedCategory")
        }

        binding.imageButton.setOnClickListener {

            showSelectImageModal()

        }

        binding.btnAddItem.setOnClickListener {

            // Close the keyboard
            val imm =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)

            if (validateFields()) {
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

                if (NetworkUtils.hasInternetConnection(requireContext())) {
                    if (item.itemId.isNullOrEmpty()) {
                        viewModel.addItemToFirestore(itemMarketItem)


                    } else {
                        viewModel.updateItemData(itemMarketItem)
                    }
                    viewModel.toastMessage.observe(viewLifecycleOwner) {
                        it?.let {
                            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                            viewModel.clearToastMessage()
                            navigateToUserProfile()
                        }
                    }

                } else {
                    val dialogFragment = NoInternetErrorDialogFragment()
                    dialogFragment.show(childFragmentManager, "noInternetErrorDialog")
                }
            }

            //goToNextInput
            binding.etItemName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    binding.etCategory.requestFocus() // Move focus to the next EditText
                    true
                } else {
                    false
                }
            }
            binding.autoCompleteTextView.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    // Move focus to the next EditText or view
                    binding.etLocation.requestFocus()

                    true
                } else {
                    false
                }
            }
            binding.etLocation.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    binding.etPhoneNumber.requestFocus() // Move focus to the next EditText
                    true
                } else {
                    false
                }
            }
            binding.etPhoneNumber.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    binding.etDescription.requestFocus() // Move focus to the last EditText

                    true
                } else {
                    false
                }
            }
            binding.etDescription.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Move the focus to the button
                    binding.btnAddItem.requestFocus()

                    true
                } else {
                    false
                }
            }


        }

    }

    private fun navigateToUserProfile() {
        findNavController().navigate(R.id.userFragment)
    }

    private fun validateFields(): Boolean {
        if (imageUri?.toString().isNullOrEmpty() && item.image_url.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Please select an image first", Toast.LENGTH_LONG)
                .show()
            return false
        }
        if (binding.etItemName.text.isNullOrEmpty()) {
            binding.txtItemName.error = "Name can not be empty"
            return false
        }
        binding.txtItemName.error = null

        if (selectedCategory.isBlank()) {
            binding.etCategory.error = "Select a category"
            return false
        }
        binding.etCategory.error = null

        if (binding.etLocation.text.isNullOrEmpty()) {
            binding.tiLocation.error = "Location can not be empty"
            return false
        }
        binding.tiLocation.error = null

        if (binding.etPhoneNumber.text.isNullOrEmpty()) {
            binding.tiPhoneNumber.error = "Phone number can not be empty"
            return false
        }
        binding.tiPhoneNumber.error = null

        return true
    }

    //fill data object
    private fun fillData() {
        binding.etItemName.setText(item.name)
        binding.autoCompleteTextView.setText(item.category)
        binding.etLocation.setText(item.location)
        binding.etPhoneNumber.setText(item.phoneNumber)
        binding.etDescription.setText(item.description)
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
