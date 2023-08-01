package com.example.reusemarket.views.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reusemarket.R
import com.example.reusemarket.adapters.UserItemAdapter
import com.example.reusemarket.databinding.FragmentUserBinding
import com.example.reusemarket.model.UserItem
import com.example.reusemarket.views.data.AddItemFragment


class UserFragment : Fragment() {


    lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user, container, false)

        binding = FragmentUserBinding.inflate(inflater, container, false)

// create list of RecyclerViewData
        var recyclerViewData = listOf<UserItem>()
        recyclerViewData = recyclerViewData + UserItem("Hello")
        recyclerViewData = recyclerViewData + UserItem("Hello")
        recyclerViewData = recyclerViewData + UserItem("Hello")
        recyclerViewData = recyclerViewData + UserItem("Hello")
        recyclerViewData = recyclerViewData + UserItem("Hello")
        recyclerViewData = recyclerViewData + UserItem("Hello")

        // create a vertical layout manager
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // create instance of MyRecyclerViewAdapter
        val myRecyclerViewAdapter = UserItemAdapter(recyclerViewData)

        // attach the adapter to the recycler view
        binding.rvUserItem.adapter = myRecyclerViewAdapter

        // also attach the layout manager
        binding.rvUserItem.layoutManager = layoutManager

        // make the adapter the data set changed for the recycler view
        myRecyclerViewAdapter.notifyDataSetChanged()


        /* val grade = listOf("A", "B", "C", "D", "E", "D", "F")

         val adapter = UserItemAdapter(grade)
         binding.rvUserItem.layoutManager = LinearLayoutManager(requireContext())
         binding.rvUserItem.setHasFixedSize(true)
         binding.rvUserItem.adapter = adapter*/




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAdd.setOnClickListener {
            Log.d("MyFragment", "onViewCreated() called")

            navigateToAddItemFragment()


        }


    }

    private fun navigateToAddItemFragment() {
        val destinationFragment = AddItemFragment()
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.container, destinationFragment)
        transaction.addToBackStack(null) // Optional: Add the transaction to the back stack for back navigation
        transaction.commit()
    }


}