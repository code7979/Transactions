package com.abhiket.transactions.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.abhiket.transactions.R
import com.abhiket.transactions.databinding.ActivityAuthBinding
import com.abhiket.transactions.ui.AuthViewModel
import com.abhiket.transactions.ui.UiState
import com.abhiket.transactions.ui.view.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityAuthBinding
    private val dialog by lazy {
        LoadingDialog(
            context = this,
            isCancelable = false,
            loadingText = R.string.progress_message_login
        )
    }

    private var isUserAgreementTicked: Boolean = false


    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        binding.btnLogin.setOnClickListener(this)
        binding.userAgreementTick.setOnClickListener(this)

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is UiState.Failure -> {
                    dialog.dismiss()
                    val string = state.stringValue.asString(this)
                    Toast.makeText(applicationContext, string, Toast.LENGTH_SHORT).show()
                }

                is UiState.Loading -> {
                    dialog.show()
                }

                is UiState.Success -> {
                    dialog.dismiss()
                    val message = state.data
                    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, BiometricActivity::class.java))
                    finish()
                }
            }
        }

    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> {
                val username = binding.loginId.text.toString()
                val password = binding.password.text.toString()
                viewModel.loginWithUsername(username, password, isUserAgreementTicked)
            }

            R.id.user_agreement_tick -> {
                val imageView = binding.userAgreementTick
                if (isUserAgreementTicked) {
                    imageView.setImageResource(0)
                    isUserAgreementTicked = false
                } else {
                    imageView.setImageResource(R.drawable.ic_checkbox_circle_fill)
                    isUserAgreementTicked = true
                }
            }
        }
    }
}