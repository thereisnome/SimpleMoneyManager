package com.example.simplemoneymanager.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simplemoneymanager.databinding.FragmentHistoryBinding
import com.example.simplemoneymanager.presentation.recyclerView.TransactionListAdapter
import com.google.android.material.divider.MaterialDividerItemDecoration


class HistoryFragment : Fragment() {

    private val adapter = TransactionListAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    private var _binding: FragmentHistoryBinding? = null
    private val binding: FragmentHistoryBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTransactions.adapter = adapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}