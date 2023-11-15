package com.example.simplemoneymanager.presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAddTransactionBinding
import com.example.simplemoneymanager.databinding.FragmentHistoryBinding

class AddTransactionFragment : Fragment() {

    companion object {
        fun newInstance() = AddTransactionFragment()
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[AddTransactionViewModel::class.java]
    }

    private var _binding: FragmentAddTransactionBinding? = null
    private val binding: FragmentAddTransactionBinding
        get() = _binding ?: throw RuntimeException("FragmentAddTransactionBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }
}