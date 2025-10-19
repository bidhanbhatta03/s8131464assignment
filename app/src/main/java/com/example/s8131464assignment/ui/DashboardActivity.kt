package com.example.s8131464assignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s8131464assignment.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val keypass = intent.getStringExtra("keypass")
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val progress = findViewById<ProgressBar>(R.id.progress)
        val empty = findViewById<View>(R.id.empty)

        recycler.layoutManager = LinearLayoutManager(this)
        val adapter = DashboardAdapter { item ->
            val i = Intent(this, DetailsActivity::class.java)
            i.putExtra("item", item)
            startActivity(i)
        }
        recycler.adapter = adapter

        if (keypass.isNullOrBlank()) {
            Toast.makeText(this, "Missing keypass", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    if (state.errorMessage != null) {
                        Toast.makeText(this@DashboardActivity, state.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    adapter.submitList(state.items)
                    empty.visibility = if (state.items.isEmpty() && !state.isLoading) View.VISIBLE else View.GONE
                }
            }
        }

        // Trigger load
        viewModel.load(keypass)
    }
}


