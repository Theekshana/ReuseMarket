package com.example.reusemarket.views.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.reusemarket.HomeActivity
import com.example.reusemarket.R
import com.example.reusemarket.constants.UIState
import com.example.reusemarket.databinding.ActivityMainBinding
import com.example.reusemarket.views.signup.SignUpActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main activity responsible for user authentication and sign-in operations.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AuthViewModel
    private var oneTapClient: SignInClient? = null
    private var signInRequest: BeginSignInRequest? = null

    private val myActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleGoogleSignInResult(result)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.lifecycleOwner = this

        binding.etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Hide the keyboard
                hideKeyboard()
                return@setOnEditorActionListener true
            }
            false
        }

        binding.root.setOnClickListener {
            hideKeyboard()
        }

        checkAlreadyLoginUser()

        setupUI()
        googleSignIn()
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun checkAlreadyLoginUser() {
        if (viewModel.isAlreadyLoggedIn()) {
            navigateToHomeActivity()
        }
    }

    private fun navigateToHomeActivity() {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
        finish()
    }

    private fun navigateToSignUpActivity() {
        val signUpIntent = Intent(this, SignUpActivity::class.java)
        startActivity(signUpIntent)
    }

    private fun setupUI() {
        binding.btnSignupWithEmail.setOnClickListener {
            navigateToSignUpActivity()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            binding.etEmail.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    binding.etPassword.requestFocus() // Move focus to the next EditText
                    true
                } else {
                    false
                }
            }

            viewModel.signIn(email, password)
            observeSignInState()
        }

        binding.btnSignupGoogle.setOnClickListener {
            initiateGoogleSignIn()
            observeSignInState()

        }
    }

    private fun observeSignInState() {
        viewModel.signIn.observe(this) {
            when (it) {
                is UIState.Loading -> {
                    showProgress(true)
                }

                is UIState.Success<*> -> {
                    showProgress(false)
                    navigateToHomeActivity()
                }

                is UIState.Failure -> {
                    showProgress(false)
                    showError(it.error)
                }

                else -> {

                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    private fun showError(error: String) {
        binding.txtError.visibility = android.view.View.VISIBLE
        binding.txtError.text = error
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
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    private fun initiateGoogleSignIn() {
        if (oneTapClient != null && signInRequest != null) {
            oneTapClient!!.beginSignIn(signInRequest!!)
                .addOnSuccessListener { result ->
                    try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender)
                                .build()
                        myActivityResultLauncher.launch(intentSenderRequest)
                    } catch (e: IntentSender.SendIntentException) {
                        Log.e(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener { e ->

                    Log.e(TAG, "Google sign-in failed: ${e.localizedMessage}")
                    showError("Google sign-in failed: ${e.localizedMessage}")

                }
        }
    }

    private fun handleGoogleSignInResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val credential = oneTapClient!!.getSignInCredentialFromIntent(result.data)
                viewModel.signInWithGoogle(credential)
            } catch (e: ApiException) {
                showError("Google sign-in failed: ${e.message}")
            }
        } else {
            showError("Google sign-in was canceled or failed. Please try again.")

        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
