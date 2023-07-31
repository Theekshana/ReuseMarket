package com.example.reusemarket.views.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.R
import com.example.reusemarket.databinding.FragmentAddItemBinding
import com.example.reusemarket.databinding.FragmentListBinding
import com.example.reusemarket.views.data.AddItemViewModel
import com.example.reusemarket.views.login.MainActivity


class ListFragment : Fragment() {

    lateinit var binding: FragmentListBinding
    private lateinit var viewModel: ListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_list, container, false)

        binding = FragmentListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button.setOnClickListener {
            viewModel.signOut()

        }


    }




}