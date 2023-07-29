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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reusemarket.R
import com.example.reusemarket.constants.hide
import com.example.reusemarket.databinding.FragmentAddItemBinding


class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var viewModel: AddItemViewModel
    private var imageUri: Uri? = null


    // Get your image

            // private var personalCollectionRef = Firebase.firestore.collection("items")

            /*  val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }*/

            /* private val resultContent =
         registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
             //binding.imageView.setImageURI(result)
             imageUri = result

     //Load image
             Glide.with(this)
                 .load(imageUri)
                 .into(binding.imageView)

         }*/

            override fun onCreateView(
                inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?,
            ): View? {
                // Inflate the layout for this fragment
                binding = FragmentAddItemBinding.inflate(inflater, container, false)

                viewModel = ViewModelProvider(requireActivity())[AddItemViewModel::class.java]

                val categories = resources.getStringArray(R.array.categories)
                val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, categories)
                binding.autoCompleteTextView.setAdapter(arrayAdapter)




                return binding.root
            }

            override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
                super.onViewCreated(view, savedInstanceState)
                /* binding.imageView.setOnClickListener{
             val imageDialog = AlertDialog.Builder(requireContext())
             imageDialog.setTitle("Select Action")
             val imageDialogItem = arrayOf("Select camera","Select gallery")
             imageDialog.setItems(imageDialogItem){dialog, which ->
                 when(which){
                     0 -> camera()
                     1 -> gallery()
                 }
             }

             imageDialog.show()
         }*/

                binding.imageButton.setOnClickListener {
                    resultContent.launch("image/*")
                    //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    //captureImage()

                }
                binding.btnAddItem.setOnClickListener {
                   // val itemImage = imageUri.toString()
                    //val name = binding.etName.text.toString()
                    //val type = binding.etItemType.text.toString()


                   // val itemData = Data(imageUri, name, type)
                    //savePerson(itemData)

                    //pickImageFromGallery()
                    //cameraCheckPermissions()
                    //gallery()

                   // viewModel.addItemToFirestore(itemData)

                    //Toast.makeText(requireContext(), "Saved data", Toast.LENGTH_LONG).show()
                    // gallery()

                    Log.e("TAG", "Data Added")


                }


            }

            private fun captureImage() {
                // Open camera


            }


            /**
             * old
             */

            /* private fun cameraCheckPermissions() {
         Dexter.withContext(requireContext()).withPermissions(
             android.Manifest.permission.READ_EXTERNAL_STORAGE,
             android.Manifest.permission.CAMERA
         ).withListener(

             object : MultiplePermissionsListener {
                 override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                     camera()
                 }

                 override fun onPermissionRationaleShouldBeShown(
                     p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                     p1: PermissionToken?,

                     ) {
                     showRotationalDialogForPermissions()
                 }

             }

         ).onSameThread().check()
     }*/


            // private fun camera() {
            //   val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            //   startActivityForResult(intent, CAMRE_REQUEST_CODE)
            // }
            private fun gallery() {
                //resultContent.launch("image/*")
                //binding.imageView.setImageURI(imageUri)


            }
    private val resultContent = registerForActivityResult(ActivityResultContracts.GetContent()) { result ->
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

            private fun pickImageFromGallery() {
                TODO("Not yet implemented")
            }

            /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMRE_REQUEST_CODE -> {

                    val bitmap = data?.extras?.get("data") as Bitmap


                    //coil image loader
                    binding.imageView.load(bitmap) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    binding.imageView.load(data?.data) {
                        crossfade(true)
                        crossfade(1000)
                        transformations(CircleCropTransformation())
                    }


                }

            }
        }
    }*/

        }
