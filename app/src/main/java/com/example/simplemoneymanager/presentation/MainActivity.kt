package com.example.simplemoneymanager.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemoneymanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}