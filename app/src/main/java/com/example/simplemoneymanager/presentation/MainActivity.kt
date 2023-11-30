package com.example.simplemoneymanager.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.addTransactionFragment){
                binding.bottomNavigation.visibility = View.GONE
                binding.fabAddTransaction.visibility = View.GONE
            } else {
                binding.bottomNavigation.visibility = View.VISIBLE
                binding.fabAddTransaction.visibility = View.VISIBLE
            }
        }

        binding.fabAddTransaction.setOnClickListener {
            navController.navigate(R.id.addTransactionFragment)
        }

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.home -> {
                    navController.navigate(R.id.historyFragment)
                    true
                }
                R.id.accounts -> {
                    navController.navigate(R.id.accountListFragment)
                    true
                }
                else -> false
            }
        }
    }
}