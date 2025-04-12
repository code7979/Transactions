package com.abhiket.transactions.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.abhiket.transactions.R
import com.abhiket.transactions.domain.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        checkForToken(tokenManager)
    }

    private fun checkForToken(tokenManager: TokenManager) = lifecycleScope.launch {
        val asyncToken = async(Dispatchers.IO) { tokenManager.retrieve(TokenManager.KEY) }
        val token = asyncToken.await()
        val intent = if (token != null) {
            Intent(this@SplashActivity, BiometricActivity::class.java)
        } else {
            Intent(this@SplashActivity, AuthActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}