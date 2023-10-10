package com.example.reusemarket.views.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.constants.gone
import com.example.reusemarket.constants.hide
import com.example.reusemarket.constants.show
import com.example.reusemarket.databinding.ActivitySignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: SignUpViewModel
    lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]
        binding.lifecycleOwner = this
        initUi()
    }

    private fun initUi() {
        binding.btnSignUp.setOnClickListener {
            // Retrieve the email and password values from the EditText
            val email = binding.etEmail.text
            val password = binding.etPassword.text

            if (email.isNullOrEmpty()) {
                binding.etEmail.error = "Please enter email"
                return@setOnClickListener
            }

            if (password.isNullOrEmpty()) {
                binding.etPassword.error = "Enter password"
                return@setOnClickListener
            }
            viewModel.signUp.observe(this) {
                when (it) {
                    is UIState.Loading -> {
                        binding.txtError.gone()
                        binding.progressBar.show()
                    }

                    is UIState.Success<*> -> {
                        binding.txtError.gone()
                        binding.progressBar.hide()
                        navigateToHomeActivity()
                    }

                    is UIState.Failure -> {
                        binding.progressBar.hide()
                        binding.txtError.show()
                        binding.txtError.text = it.error
                    }
                }
            }
            // Initiates the sign-up process
            viewModel.signUp(email.toString(), password.toString())
        }
    }

    private fun navigateToHomeActivity() {
        val homeIntent = Intent(this@SignUpActivity, HomeActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

}