package com.example.reusemarket.views.login

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: AuthViewModel
    private var oneTapClient: SignInClient? = null
    private var signInRequest: BeginSignInRequest? = null
    private lateinit var myActivityResultLauncher: ActivityResultLauncher<IntentSenderRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        binding.lifecycleOwner = this

        checkAlreadyLoginUser()

        setupUI()
        googleSignIn()
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

            viewModel.signIn(email, password)

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
                }
            }
        }

        binding.btnSignupGoogle.setOnClickListener {
            initiateGoogleSignIn()
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
                }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
