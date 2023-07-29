package com.example.reusemarket.views.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.reusemarket.R
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
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
                    navigateToAddItemFragment()
                } catch (e: ApiException) {
                    TODO("Exception handling")
                }
            } else {
                // Result was not successful, handle the failure
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity())[AuthViewModel::class.java]

        /*binding.vm = viewModel
        binding.lifecycleOwner = this*/
        googleSignIn()

        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkAlreadyLoginUser()
        initUi()

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

            viewModel.signUp.observe(viewLifecycleOwner) {
                when (it) {
                    is UIState.Loading -> {

                    }

                    is UIState.Success<*> -> {

                        //navigateToListFragment()
                        navigateToAddItemFragment()

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
                        Log.e(TAG, "Couldn't start One Tap UI: " + e.localizedMessage)
                    }
                }


        }
    }

    private fun googleSignIn() {
        oneTapClient = Identity.getSignInClient(requireContext())
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


    /**
     * Navigates to the List Fragment.
     */
    private fun navigateToListFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_listFragment)
    }

    private fun navigateToAddItemFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_addItemFragment)
    }

    private fun checkAlreadyLoginUser() {
        if (viewModel.isAlreadyLoggedIn()) {
            //navigateToListFragment()
        navigateToAddItemFragment()
        }

    }
}


