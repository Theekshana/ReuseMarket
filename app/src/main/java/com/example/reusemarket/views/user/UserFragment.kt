package com.example.reusemarket.views.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reusemarket.adapters.UserItemAdapter
import com.example.reusemarket.databinding.FragmentUserBinding


class UserFragment : Fragment() {


    lateinit var binding: FragmentUserBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user, container, false)

        binding = FragmentUserBinding.inflate(inflater, container, false)





        val grade = listOf("A", "B", "C", "D", "E", "D", "F")

        val adapter = UserItemAdapter(grade)
        binding.rvUserItem.layoutManager = LinearLayoutManager(requireContext())
        binding.rvUserItem.setHasFixedSize(true)
        binding.rvUserItem.adapter = adapter




        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




    }


}