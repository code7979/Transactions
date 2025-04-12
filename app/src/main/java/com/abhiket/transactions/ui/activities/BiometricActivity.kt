package com.abhiket.transactions.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.core.content.ContextCompat
import com.abhiket.transactions.databinding.ActivityBiometricBinding
import com.abhiket.transactions.utils.checkExistence
import androidx.biometric.BiometricPrompt
import com.abhiket.transactions.R

class BiometricActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBiometricBinding
    private lateinit var biometricManager: BiometricManager

    private val executor by lazy { ContextCompat.getMainExecutor(applicationContext) }

    private val launcherForBiometricSetting = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            1 -> {
                Toast.makeText(applicationContext, "Enrollment done", Toast.LENGTH_SHORT).show()
            }

            2 -> {
                Toast.makeText(applicationContext, "Enrollment rejected", Toast.LENGTH_SHORT).show()
            }

            else -> {
                Toast.makeText(applicationContext, "Enrollment cancel", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityBiometricBinding.inflate(
            layoutInflater
        )
        setContentView(binding.root)
        super.onCreate(savedInstanceState)

        biometricManager = BiometricManager.from(this)
        biometricManager.checkExistence(
            onSuccess = { allowedAuthenticators ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val biometricPrompt = BiometricPrompt(this, executor, biometricPromptCallback)
                    val promptInfo = BiometricPrompt.PromptInfo.Builder()
                        .setTitle(getString(R.string.fingerprint_title))
                        .setSubtitle(getString(R.string.fingerprint_subtitle))
                        .setNegativeButtonText(getString(R.string.fingerprint_prompt_cancel))
                        .setAllowedAuthenticators(allowedAuthenticators)
                        .build()
                    biometricPrompt.authenticate(promptInfo)
                }
            },
            onError = {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            },
            openSettings = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val intent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            BiometricManager.Authenticators.BIOMETRIC_STRONG
                        )
                    }
                    launcherForBiometricSetting.launch(intent)
                } else {
                    val intent = Intent(Settings.ACTION_SECURITY_SETTINGS)
                    startActivity(intent)
                }
            })
    }

    private val biometricPromptCallback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Toast.makeText(applicationContext, "Succeeded", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@BiometricActivity, TransactionActivity::class.java))
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            finish()
        }

        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            finish()
        }
    }
}
