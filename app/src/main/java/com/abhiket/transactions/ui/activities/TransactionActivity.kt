package com.abhiket.transactions.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abhiket.transactions.R
import com.abhiket.transactions.databinding.ActivityTransactionBinding
import com.abhiket.transactions.ui.AuthViewModel
import com.abhiket.transactions.ui.TransitionsViewModel
import com.abhiket.transactions.ui.UiState
import com.abhiket.transactions.ui.view.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTransactionBinding
    private val dialog by lazy {
        LoadingDialog(
            context = this,
            isCancelable = false,
            loadingText = R.string.progress_message_transaction
        )
    }
    private val adapter: TransitionAdapter by lazy { TransitionAdapter() }
    private val viewModel by viewModels<TransitionsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.transitionsToolbar)

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.hasFixedSize()
        recyclerView.adapter = adapter

        viewModel.transitions.observe(this) { state ->
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
                    adapter.transactions = state.data
                }
            }
        }

        viewModel.isLogout.observe(this) { isLogout ->
            if (isLogout) {
                startActivity(Intent(this@TransactionActivity, AuthActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.error_failed_logout),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_transactions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out -> {
                viewModel.logOut()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}