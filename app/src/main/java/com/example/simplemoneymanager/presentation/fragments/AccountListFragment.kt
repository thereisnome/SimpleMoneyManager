package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAccountListBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.domain.transaction.Transaction
import com.example.simplemoneymanager.presentation.recyclerViews.accountList.AccountListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AccountListFragment : Fragment(), AccountListAdapter.AccountPopupMenuItemClickListener {

    private val viewModel: AccountListViewModel by viewModels()

    private val adapter = AccountListAdapter(this)

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

        val fab = requireActivity().findViewById<FloatingActionButton>(R.id.fab)
        fab.foreground = ContextCompat.getDrawable(requireContext(), R.drawable.background_fab_add_account)
        fab.setOnClickListener {
            showAddAccountDialogFragment()
        }

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

    private fun showAddAccountDialogFragment() {
        val addAccountDialogFragment = AddAccountDialogFragment()
        addAccountDialogFragment.show(childFragmentManager, "TEST")
    }

    override fun onMenuItemClick(itemId: Int, position: Int, account: Account) {
        when (itemId) {
            R.id.account_menu_button_delete -> {
                if (account.accountId == 0L) {
                    Toast.makeText(
                        requireContext(),
                        "You cannot delete Main account",
                        Toast.LENGTH_LONG
                    ).show()
                } else createDeleteAccountDialogAlert(account)
            }
        }
    }

    private fun createDeleteAccountDialogAlert(account: Account) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(
                R.string.delete_account_title,
                account.accountName
            ))
            .setMessage(resources.getString(R.string.delete_account_dialog_description))
            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(resources.getString(R.string.delete)) { _, _ ->
                viewModel.removeAccount(account.accountId)
            }
            .show()
    }
}