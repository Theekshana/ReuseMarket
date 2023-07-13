package com.example.reusemarket.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reusemarket.R
import com.example.reusemarket.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        binding.vm = viewModel
        binding.lifecycleOwner = this

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {

            // Retrieve the email and password values from the EditText
            val strEmail = binding.etEmail.text.toString()
            val strPassword = binding.etPassword.text.toString()

            // Initiates the sign-up process
            viewModel.signUpInfo(strEmail, strPassword)
            //viewModel.login(strEmail,strPassword)
            //navigate to the list fragment
            navigateToListFragment()
            Toast.makeText(context, "account added", Toast.LENGTH_LONG).show()

        }


    }

    /**
     * Navigates to the List Fragment.
     */
    private fun navigateToListFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_listFragment)
    }


}