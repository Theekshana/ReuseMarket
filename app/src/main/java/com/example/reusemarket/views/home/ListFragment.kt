package com.example.reusemarket.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.R
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.constants.hide
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.FragmentListBinding
import com.example.reusemarket.model.AllItem
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * Fragment for displaying a list of items.
 *
 * This fragment displays a list of items and allows users to click on an item to view its details.
 *
 * @constructor Creates an instance of [ListFragment].
 */
class ListFragment : Fragment(), AllItemAdapter.OnItemClickedListener {

    lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    private var isBottomNavigationVisible = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentListBinding.inflate(inflater, container, false)

       viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]

        (requireActivity() as HomeActivity).findViewById<View>(R.id.bottomNavigationView)?.visibility =
            View.VISIBLE

        setupScrollListener()

        binding.rvAllItem.layoutManager = LinearLayoutManager(requireContext())

        return binding.root
    }


    private fun setupScrollListener() {
        binding.rvAllItem.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                val totalItemCount = layoutManager.itemCount

                if (totalItemCount > totalItemCount + 1) {
                    showOrHideBottomNavigation(totalItemCount - lastVisibleItemPosition > totalItemCount)
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
                    binding.lottieProgressBar.hide()
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }

                UIState.Loading -> {
                    val lottieProgressBar = binding.lottieProgressBar
                    lottieProgressBar.show()
                    lottieProgressBar.playAnimation()
                }

                is UIState.Success<*> -> {
                    binding.lottieProgressBar.hide()
                    val myRecyclerViewAdapter =
                        AllItemAdapter(
                            (viewModel.marketItemList.value ?: emptyList()) as ArrayList<AllItem>
                        )
                    myRecyclerViewAdapter.onItemClickedListener = this
                    binding.rvAllItem.adapter = myRecyclerViewAdapter
                }
            }
        }
        viewModel.fetchAllItems()

    }

    override fun onItemClicked(item: AllItem) {
        findNavController().navigate(
            R.id.action_listFragment_to_viewItemFragment,
            args = bundleOf("item" to item)
        )
    }


}