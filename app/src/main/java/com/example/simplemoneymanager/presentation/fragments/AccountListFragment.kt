package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.databinding.FragmentAccountListBinding
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.accountList.AccountListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountListViewModel

class AccountListFragment : Fragment() {

    private val viewModel: AccountListViewModel by viewModels()

    private val adapter = AccountListAdapter()

    private var _binding: FragmentAccountListBinding? = null
    private val binding: FragmentAccountListBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAccountWithTransactions()
            .observe(viewLifecycleOwner) { accountWithTransactionsList ->
                adapter.accountWithTransactions = accountWithTransactionsList
                binding.rvAccountDetails.adapter = adapter
                adapter.onItemClickListener = {
                    findNavController().navigate(
                        AccountListFragmentDirections.actionAccountListFragmentToAccountDetailsFragment(
                            it.accountId
                        )
                    )
                }
            }

        viewModel.getOverallBalance().observe(viewLifecycleOwner) {
            binding.tvTotalAmount.text = Transaction.formatCurrencyWithoutSign(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}