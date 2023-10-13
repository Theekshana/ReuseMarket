package com.example.reusemarket.views.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reusemarket.R
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.constants.hide
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.FragmentSearchBinding
import com.example.reusemarket.model.AllItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A Fragment for searching items based on user input.
 */
class SearchFragment : Fragment(), SearchItemAdapter.OnItemClickedListener {

    private val debouncePeriod = 500L // 500 milliseconds debounce time
    private val scope = CoroutineScope(Dispatchers.Main)
    lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[SearchViewModel::class.java]

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            // Cancel any existing debounce job
            scope.coroutineContext.cancelChildren()
            // Launch a new debounce job
            scope.launch {
                delay(debouncePeriod)
                // Perform search here
                performSearch(s.toString())
            }
        }
    }

    private fun performSearch(searchText: String) {
        viewModel.searchItems(searchText)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchInputText.addTextChangedListener(textWatcher)
        registerForListState()
    }

    private fun registerForListState() {
        viewModel.dateListState.observe(viewLifecycleOwner) {
            when (it) {
                is UIState.Failure -> {
                    binding.progressBar.hide()
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }

                UIState.Loading -> {
                    binding.progressBar.show()
                }

                is UIState.Success<*> -> {
                    binding.progressBar.hide()
                    val myRecyclerViewAdapter =
                        SearchItemAdapter(viewModel.marketItemList.value ?: emptyList())
                    myRecyclerViewAdapter.onItemClickedListener = this
                    binding.searchList.adapter = myRecyclerViewAdapter
                }
            }
        }
    }

    override fun onItemClicked(item: AllItem) {
        findNavController().navigate(
            R.id.action_searchFragment_to_viewItemFragment,
            args = bundleOf("item" to item)
        )
    }

}