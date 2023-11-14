package com.example.simplemoneymanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.ActivityMainBinding
import com.example.simplemoneymanager.domain.day.Day
import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.LocalDate

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}