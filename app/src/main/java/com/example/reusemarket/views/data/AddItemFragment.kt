package com.example.reusemarket.views.data

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.reusemarket.databinding.FragmentAddItemBinding
import com.example.reusemarket.model.Data
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AddItemFragment : Fragment() {

    private lateinit var binding: FragmentAddItemBinding
    private lateinit var viewModel: AddItemViewModel
   // private var personalCollectionRef = Firebase.firestore.collection("items")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddItemBinding.inflate(inflater, container,false)

        viewModel = ViewModelProvider(requireActivity())[AddItemViewModel::class.java]


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString()
            val type = binding.etItemType.text.toString()

            val itemData = Data(name,type)
            //savePerson(itemData)




            viewModel.addItemToFirestore(itemData)

            Toast.makeText(requireContext(), "Saved data", Toast.LENGTH_LONG).show()


            Log.e("TAG","Data Added" )



        }


    }

    /*private fun savePerson(data: Data) = CoroutineScope(Dispatchers.IO).launch {
        try {
            personalCollectionRef.add(data).await()
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), "Saved data", Toast.LENGTH_LONG).show()
            }
        }catch (e: Exception){
            withContext(Dispatchers.Main){
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
            }
        }
    }*/
}