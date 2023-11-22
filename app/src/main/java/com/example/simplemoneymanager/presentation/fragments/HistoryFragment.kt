package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentHistoryBinding
import com.example.simplemoneymanager.presentation.recyclerViews.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.HistoryViewModel

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
        registerForContextMenu(binding.rvTransactions)
        setupRecyclerView()
        binding.fabAddTransaction.setOnClickListener {
            launchAddTransactionFragment()
        }
        binding.fabAddTransaction.setOnLongClickListener {
            viewModel.removeAllTransactions()
            true
        }
    }

    private fun setupRecyclerView() {
        binding.rvTransactions.adapter = adapter
        viewModel.getTransactionList().observe(viewLifecycleOwner) { transactionList ->
            adapter.submitList(transactionList.sortedByDescending { it.transactionId }) {
                val position = binding.rvTransactions.layoutManager?.let {
                    val viewAtPosition = it.findViewByPosition(0)
                    viewAtPosition?.let { view -> binding.rvTransactions.getChildAdapterPosition(view) }
                }
                if (position != null) {
                    adapter.notifyItemChanged(position)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchAddTransactionFragment() {
        findNavController().navigate(R.id.action_historyFragment_to_addTransactionFragment)

    }
}