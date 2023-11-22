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
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.TransactionListAdapter
import com.example.simplemoneymanager.presentation.viewModels.HistoryViewModel

class HistoryFragment : Fragment(), TransactionListAdapter.PopupMenuItemClickListener {

    private val adapter = TransactionListAdapter(this)

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
        setupRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            adapter.transactionList = transactionList.sortedByDescending { it.transactionId }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchAddTransactionFragment() {
        findNavController().navigate(R.id.action_historyFragment_to_addTransactionFragment)
    }

    override fun onMenuItemClick(itemId: Int, position: Int, transaction: Transaction) {
        when (itemId) {
            R.id.menu_button_delete -> viewModel.removeTransaction(transaction.transactionId)
        }
    }
}