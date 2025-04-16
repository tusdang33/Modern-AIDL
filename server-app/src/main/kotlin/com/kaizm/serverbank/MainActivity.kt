package com.kaizm.serverbank

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kaizm.serverbank.databinding.ActivityMainBinding
import com.kaizm.serverbank.service.ServiceStatus
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ServiceStatus.onStatusUpdate = { msg ->
            viewModel.updateStatus(msg)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.clientStatus.collect { msg ->
                        binding.status.text = msg
                    }
                }
                launch {
                    viewModel.timer.collect { seconds ->
                        val minutes = seconds / 60
                        val sec = seconds % 60
                        binding.timer.text = getString(
                            R.string.connect_time,
                            String.format(Locale.getDefault(), "%02d:%02d", minutes, sec)
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ServiceStatus.onStatusUpdate = null
    }

}