package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAccountBottomSheetBinding
import com.example.simplemoneymanager.domain.account.Account
import com.example.simplemoneymanager.presentation.recyclerViews.chooseAccount.ChooseAccountListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountBottomSheetViewModel
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AccountBottomSheetDialogFragment :
    BottomSheetDialogFragment(), ChooseAccountListAdapter.AccountPopupMenuItemClickListener {

    private var dataPassListener: DataPassListener? = null

    private val adapter = ChooseAccountListAdapter(this)

    private val viewModel: AccountBottomSheetViewModel by viewModels()

    private var _binding: FragmentAccountBottomSheetBinding? = null
    private val binding: FragmentAccountBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBottomSheetBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddNewAccount.setOnClickListener {
            showAddAccountDialogFragment()
        }

        val flexboxLayoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvAccounts.layoutManager = flexboxLayoutManager

        viewModel.getAccountList()
            .observe(viewLifecycleOwner) { accountList ->
                adapter.submitList(accountList)
                binding.rvAccounts.adapter = adapter
                adapter.onItemClickListener = {
                    passDataBack(it)
                    dismiss()
                }
            }

    }

    private fun showAddAccountDialogFragment() {
        val addAccountDialogFragment = AddAccountDialogFragment()
        addAccountDialogFragment.show(childFragmentManager, "TEST")
    }

    fun setDataPassListener(listener: DataPassListener) {
        dataPassListener = listener
    }

    private fun passDataBack(account: Account) {
        dataPassListener?.onAccountPassed(account)
    }

    interface DataPassListener {
        fun onAccountPassed(account: Account)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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