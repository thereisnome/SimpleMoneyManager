package com.example.simplemoneymanager.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.simplemoneymanager.databinding.ActivityMainBinding
import com.example.simplemoneymanager.presentation.fragments.AddTransactionFragment

class MainActivity : AppCompatActivity(), AddTransactionFragment.OnEditingFinishListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onEditingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}