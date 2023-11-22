package com.example.simplemoneymanager.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemoneymanager.databinding.ActivityMainBinding
import com.example.simplemoneymanager.presentation.fragments.AddTransactionFragment

class MainActivity : AppCompatActivity(), AddTransactionFragment.OnAddingFinishListener {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onAddingFinished() {
        Toast.makeText(this@MainActivity, "Success", Toast.LENGTH_SHORT).show()
        supportFragmentManager.popBackStack()
    }
}