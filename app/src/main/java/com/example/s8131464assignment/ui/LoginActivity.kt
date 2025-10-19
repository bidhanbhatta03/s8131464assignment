package com.example.s8131464assignment.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.s8131464assignment.R
import com.example.s8131464assignment.datastore.KeypassStore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    @Inject lateinit var keypassStore: KeypassStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val username = findViewById<EditText>(R.id.etUsername)
        val password = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val spinner = findViewById<Spinner>(R.id.spLocation)
        val progress = findViewById<ProgressBar>(R.id.progress)

        val locations = listOf("footscray", "sydney", "br")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)

        // Prefill credentials for convenience
        username.setText("bidhan")
        password.setText("8131464")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                    if (!state.errorMessage.isNullOrBlank()) {
                        Toast.makeText(this@LoginActivity, state.errorMessage, Toast.LENGTH_LONG).show()
                    }
                    val key = state.keypass
                    if (!key.isNullOrBlank()) {
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("keypass", key)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        btnLogin.setOnClickListener {
            val u = username.text.toString().trim()
            val p = password.text.toString().trim()
            val location = spinner.selectedItem?.toString()?.trim().orEmpty()
            
            when {
                u.isEmpty() && p.isEmpty() -> {
                    Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                u.isEmpty() -> {
                    Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show()
                    username.requestFocus()
                    return@setOnClickListener
                }
                p.isEmpty() -> {
                    Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                    password.requestFocus()
                    return@setOnClickListener
                }
                location.isEmpty() -> {
                    Toast.makeText(this, "Please select a location", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            
            viewModel.login(location, u, p)
        }
    }
}


