package com.example.reusemarket.views.login

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.ListFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.R
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.databinding.ActivityMainBinding
import com.example.reusemarket.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AuthViewModel
    private var oneTapClient: SignInClient? = null
    private var signInRequest: BeginSignInRequest? = null

    private val myActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    val credential = oneTapClient!!.getSignInCredentialFromIntent(result.data)
                    viewModel.signInWithGoogle(credential)
                    // navigateToListFragment()
                    //navigateToAddItemFragment()
                } catch (e: ApiException) {
                    TODO("Exception handling")
                }
            } else {
                // Result was not successful, handle the failure
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.vm = viewModel
        binding.lifecycleOwner = this
        checkAlreadyLoginUser()
        initUi()

        googleSignIn()


    }

    private fun checkAlreadyLoginUser() {
        if (viewModel.isAlreadyLoggedIn()) {



        }
    }


    private fun initUi() {
        binding.btnLogin.setOnClickListener {
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
            // Initiates the sign-up process
            viewModel.signUpResponse(email.toString(), password.toString())

            viewModel.signUp.observe(this) {
                when (it) {
                    is UIState.Loading -> {

                    }
                    is UIState.Success<*> -> {

                        val homeIntent = Intent(this@MainActivity, HomeActivity::class.java)
                        startActivity(homeIntent)
                        finish()

                    }

                    is UIState.Failure -> {


                    }
                }
            }

        }

        binding.btnSignupGoogle.setOnClickListener {

            oneTapClient!!.beginSignIn(signInRequest!!)
                .addOnSuccessListener { result ->
                    try {
                        val intent =
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                .build()
                        myActivityResultLauncher.launch(intent)


                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(ContentValues.TAG, "Couldn't start One Tap UI: " + e.localizedMessage)
                    }
                }


        }
    }

    private fun googleSignIn() {
        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(
                BeginSignInRequest.PasswordRequestOptions.builder()
                    .setSupported(true)
                    .build()
            )
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(getString(R.string.web_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()
    }


}




