package com.example.reusemarket.views.user

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.reusemarket.R
import com.example.reusemarket.adapters.UserItemAdapter
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.constants.showAlertYeNo
import com.example.reusemarket.databinding.FragmentUserBinding
import com.example.reusemarket.model.AllItem
import com.example.reusemarket.views.login.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class UserFragment : Fragment(), UserItemAdapter.OnDeleteClicked, UserItemAdapter.OnEditClicked {

    private lateinit var viewModel: UserViewModel
    lateinit var binding: FragmentUserBinding
    private var isBottomNavigationVisible = true
    private lateinit var fabButton: FloatingActionButton


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.user_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.action_logout -> {
                        logout()
                        true
                    }

                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)


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
                    myRecyclerViewAdapter.onEditClicked = this
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

    private fun logout() {
        requireContext().showAlertYeNo("Logout", "Are you sure you want to logout", {
            viewModel.logout()
            val homeIntent = Intent(requireContext(), MainActivity::class.java)
            startActivity(homeIntent)
            requireActivity().finish()
        })
    }

    private fun navigateToAddItemFragment() {
        findNavController().navigate(R.id.addItemFragment)
    }

    override fun onDeleteItemClicked(allItem: AllItem) {
        requireContext().showAlertYeNo("Confirm", "Are you sure you want to delete this item?",
            {
                viewModel.deleteItem(allItem)
            })


    }

    override fun onEditItemClicked(allItem: AllItem) {
        findNavController().navigate(
            R.id.action_userFragment_to_addItemFragment,
            args = bundleOf("item" to allItem)
        )

    }


}