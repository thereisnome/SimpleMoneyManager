package com.example.simplemoneymanager.presentation.fragments

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.ColorList
import com.example.simplemoneymanager.databinding.FragmentAddAccountDialogBinding
import com.example.simplemoneymanager.presentation.SimpleMoneyManagerApp
import com.example.simplemoneymanager.presentation.viewModels.AddAccountDialogViewModel
import com.example.simplemoneymanager.presentation.viewModels.ViewModelFactory
import com.google.android.material.chip.Chip
import javax.inject.Inject

class AddAccountDialogFragment : DialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by viewModels<AddAccountDialogViewModel>{
        viewModelFactory
    }

    private var _binding: FragmentAddAccountDialogBinding? = null
    private val binding: FragmentAddAccountDialogBinding
        get() = _binding ?: throw RuntimeException("FragmentAddAccountDialogBinding is null")

    private lateinit var color: String

    private val component by lazy { (requireActivity().application as SimpleMoneyManagerApp).component }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAccountDialogBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createChipGroup()

        with(binding) {
            accountAddToolbar.setNavigationOnClickListener { dismiss() }
            accountAddToolbar.inflateMenu(R.menu.add_account_toolbar_menu)
            accountAddToolbar.setOnMenuItemClickListener {
                if (etAccountName.text.toString() != "") {
                    val accountName = etAccountName.text.toString()
                    val accountBalance =
                        if (etAccountBalance.text.toString() == "") 0.0 else etAccountBalance.text.toString()
                            .toDouble()
                    viewModel.addAccount(accountName, accountBalance, color)
                    Toast.makeText(requireContext(), "Account added successfully", Toast.LENGTH_LONG)
                        .show()
                    dismiss()
                } else tilAccountName.error = requireContext().getString(R.string.input_error)
                true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createChipGroup() {
        val colorList = ColorList.entries
        colorList.forEach {color->
            val chip = Chip(requireContext())
            chip.chipStrokeWidth = 0f
            chip.chipBackgroundColor = ColorStateList.valueOf(color.hex.toColorInt())
            binding.chipGroupColor.addView(chip)
            chip.setOnClickListener {
                this.color = color.hex
            }
        }
    }
}