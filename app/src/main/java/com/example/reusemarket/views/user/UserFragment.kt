package com.example.reusemarket.views.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R
import com.example.reusemarket.adapters.UserItemAdapter
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.constants.showAlertYeNo
import com.example.reusemarket.databinding.FragmentUserBinding
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.views.data.AddItemFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class UserFragment : Fragment(), UserItemAdapter.OnDeleteClicked {

    private lateinit var viewModel: UserViewModel
    lateinit var binding: FragmentUserBinding
    private var isBottomNavigationVisible = true
    private lateinit var fabButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user, container, false)

        // Inflate the layout for this fragment
        viewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentUserBinding.inflate(inflater, container, false)

        fabButton = binding.btnAdd



        setupScrollListener()
        showOrHideFabButton()

        // create a vertical layout manager
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        binding.rvUserItem.layoutManager = layoutManager

        return binding.root
    }

    private fun showOrHideFabButton() {
        binding.rvUserItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && fabButton.isShown) {
                    fabButton.hide()
                } else if (dy < 0 && !fabButton.isShown) {
                    fabButton.show()
                }
            }
        })
    }


    private fun setupScrollListener() {
        binding.rvUserItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    showOrHideBottomNavigation(true)
                    fabButton.show()

                } else {
                    showOrHideBottomNavigation(false)
                }
            }
        })
    }

    private fun showOrHideBottomNavigation(show: Boolean) {
        val bottomNav: BottomNavigationView =
            requireActivity().findViewById(R.id.bottomNavigationView)
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


        viewModel.dateListState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Failure -> {

                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }

                UIState.Loading -> {

                }

                is UIState.Success<*> -> {

                    Toast.makeText(
                        requireContext(),
                        "Number of documents retrieved: ${viewModel.marketItemList.value?.size}",
                        Toast.LENGTH_LONG
                    ).show()
                    val myRecyclerViewAdapter =
                        UserItemAdapter(
                            (viewModel.marketItemList.value ?: emptyList()) as ArrayList<AllItem>
                        )
                    myRecyclerViewAdapter.onDeleteClicked = this
                    binding.rvUserItem.adapter = myRecyclerViewAdapter
                }
            }
        }

        viewModel.fetchPostedItems()


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

    override fun onDeleteItemClicked(allItem: AllItem) {
        requireContext().showAlertYeNo("Confirm", "Are sure you want to delete this item?",
            {
                viewModel.deleteItem(allItem)
            })


    }


}