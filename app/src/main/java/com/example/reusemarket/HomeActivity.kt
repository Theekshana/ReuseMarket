package com.example.reusemarket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.reusemarket.databinding.ActivityHomeBinding
import com.example.reusemarket.databinding.FragmentListBinding
import com.example.reusemarket.views.home.ListFragment
import com.example.reusemarket.views.home.ListViewModel
import com.example.reusemarket.views.search.SearchFragment
import com.example.reusemarket.views.user.UserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {




    lateinit var binding: ActivityHomeBinding
//    private lateinit var viewModel: Home

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Inflate the layout for this fragment
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home)
//        viewModel = ViewModelProvider(requireActivity())[ListViewModel::class.java]
        loadFragment(ListFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.miHome -> {
                    loadFragment(ListFragment())
                    true
                }
                R.id.miSearch -> {
                    loadFragment(SearchFragment())
                    true
                }
                R.id.miUser -> {
                    loadFragment(UserFragment())
                    true
                }

                else -> {true}
            }
        }

    }

    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}




