package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.databinding.FragmentAccountBottomSheetBinding
import com.example.simplemoneymanager.domain.account.AccountEntity
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.recyclerViews.chooseAccount.ChooseAccountListAdapter
import com.example.simplemoneymanager.presentation.viewModels.AccountBottomSheetViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import javax.inject.Inject

class AccountBottomSheetDialogFragment :
    BottomSheetDialogFragment(), ChooseAccountListAdapter.AccountPopupMenuItemClickListener {

    private var dataPassListener: DataPassListener? = null

    private val adapter = ChooseAccountListAdapter(this)

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AccountBottomSheetViewModel>{
        viewModelFactory
    }

    private var _binding: FragmentAccountBottomSheetBinding? = null
    private val binding: FragmentAccountBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentHistoryBinding is null")

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

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

        setupRecyclerView()

        binding.buttonAddNewAccount.setOnClickListener {
            showAddAccountDialogFragment()
        }

        viewModel.getAccountList()
            .observe(viewLifecycleOwner) { accountList ->
                passDataToRecyclerView(accountList)
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun passDataToRecyclerView(accountList: List<AccountEntity>){
        adapter.submitList(accountList)
        binding.rvAccounts.adapter = adapter
        adapter.onItemClickListener = {
            passDataBack(it)
            dismiss()
        }
    }

    private fun setupRecyclerView(){
        val flexboxLayoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvAccounts.layoutManager = flexboxLayoutManager
    }

    private fun showAddAccountDialogFragment() {
        val addAccountDialogFragment = AddAccountDialogFragment()
        addAccountDialogFragment.show(childFragmentManager, "TEST")
    }

    override fun onMenuItemClick(itemId: Int, position: Int, account: AccountEntity) {
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

    private fun createDeleteAccountDialogAlert(account: AccountEntity) {
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
                Toast.makeText(requireContext(), "Account removed", Toast.LENGTH_LONG).show()
            }
            .show()
    }

    fun setDataPassListener(listener: DataPassListener) {
        dataPassListener = listener
    }

    private fun passDataBack(account: AccountEntity) {
        dataPassListener?.onAccountPassed(account)
    }

    interface DataPassListener {
        fun onAccountPassed(account: AccountEntity)
    }
}