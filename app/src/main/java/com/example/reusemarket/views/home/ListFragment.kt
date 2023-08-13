package com.example.reusemarket.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R
import com.example.reusemarket.adapters.AllItemAdapter
import com.example.reusemarket.databinding.FragmentListBinding
import com.example.reusemarket.model.AllItem
import com.google.android.material.bottomnavigation.BottomNavigationView


class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private var isBottomNavigationVisible = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_list, container, false)

        binding = FragmentListBinding.inflate(inflater, container, false)


        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]

        setupScrollListener()

        // create list of RecyclerViewData
        var recyclerViewData = listOf<AllItem>()
        recyclerViewData = recyclerViewData + AllItem("Hello", "Tokyo")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Kyoto")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Tokyo")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Kyoto")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Tokyo")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Kyoto")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Tokyo")
        recyclerViewData = recyclerViewData + AllItem("Hello", "Kyoto")


        // create a vertical layout manager
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // create instance of MyRecyclerViewAdapter
        val myRecyclerViewAdapter = AllItemAdapter(recyclerViewData)

        // attach the adapter to the recycler view
        binding.rvAllItem.adapter = myRecyclerViewAdapter

        // also attach the layout manager
        binding.rvAllItem.layoutManager = layoutManager

        // make the adapter the data set changed for the recycler view
        myRecyclerViewAdapter.notifyDataSetChanged()

        return binding.root
    }

    private fun setupScrollListener() {
        binding.rvAllItem.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    showOrHideBottomNavigation(true)
                }else{
                    showOrHideBottomNavigation(false)
                }
            }
        })
    }

    private fun showOrHideBottomNavigation(show: Boolean) {
        val bottomNav: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        if (show && !isBottomNavigationVisible) {
            // Show the bottom navigation if it's not visible
            bottomNav.visibility = View.VISIBLE
            isBottomNavigationVisible = true
        } else if (!show && isBottomNavigationVisible) {
            // Hide the bottom navigation if it's visible
            bottomNav.visibility = View.GONE
            isBottomNavigationVisible = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

               /*binding.button.setOnClickListener {
            viewModel.signOut()


        }*/


    }




}