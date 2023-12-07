package com.example.simplemoneymanager.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import com.example.simplemoneymanager.R
import com.example.simplemoneymanager.common.DateFilter
import com.example.simplemoneymanager.databinding.FragmentFilterBottomSheetBinding
import com.example.simplemoneymanager.presentation.viewModels.FilterBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth
import java.util.Locale


class FilterBottomSheetDialogFragment(private var dateFilter: DateFilter) :
    BottomSheetDialogFragment() {

    private var dataPassListener: DataPassListener? = null

    private val viewModel: FilterBottomSheetViewModel by viewModels()

    private var _binding: FragmentFilterBottomSheetBinding? = null

    private val binding: FragmentFilterBottomSheetBinding
        get() = _binding ?: throw RuntimeException("FragmentFilterBottomSheetBinding is null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDateList()
        setupRangeButton()
        setupSaveButton()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRangeButton() {
        binding.buttonSelectRange.setOnClickListener {
            createRangeDatePicker().show(childFragmentManager, "TAG")
        }
    }

    private fun setupSaveButton() {
        binding.buttonSaveFilter.setOnClickListener {
            when (true) {
                (binding.chipGroupMonth.checkedChipId != View.NO_ID) -> {
                    passDataBack(dateFilter)
                    dismiss()
                }

                (dateFilter.isRangeSet) -> {
                    passDataBack(dateFilter)
                    dismiss()
                }

                else -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.select_date_to_show_statistics),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun initDateList() {
        viewModel.getTransactionList().observe(viewLifecycleOwner) { transactionList ->
            createChipGroup(viewModel.createSetOfMonths(transactionList))
            restoreState()
        }
    }

    private fun restoreState() {
        if (dateFilter.isRangeSet) {
            binding.buttonSelectRange.text = dateFilter.toString()
        } else {
            binding.chipGroupMonth.check(dateFilter.startDate.toString().replace("-", "").toInt())
        }
    }

    private fun createRangeDatePicker(): MaterialDatePicker<Pair<Long, Long>> {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText(
            requireContext().getString(
                R.string.select_range
            )
        ).setSelection(
            Pair(
                MaterialDatePicker.thisMonthInUtcMilliseconds(),
                MaterialDatePicker.todayInUtcMilliseconds()
            )
        ).build()

        dateRangePicker.addOnPositiveButtonClickListener {
            dateFilter = viewModel.convertMillisToDate(it)
            binding.buttonSelectRange.text = dateFilter.toString()
            binding.chipGroupMonth.clearCheck()
        }

        dateRangePicker.addOnDismissListener {
            binding.chipGroupMonth.check(View.NO_ID)
        }
        return dateRangePicker
    }

    private fun createChipGroup(dateList: Set<LocalDate>) {

        dateList.forEach { date ->
            val chip = Chip(requireContext())
            chip.id = date.toString().replace("-", "").toInt()
            chip.text = date.month.toString().lowercase()
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            chip.textSize = 18F
            binding.chipGroupMonth.addView(chip)
            chip.setOnClickListener {
                this.dateFilter =
                    DateFilter(date.with(firstDayOfMonth()), date.with(lastDayOfMonth()), false)
            }
        }
        binding.chipOverall.setOnClickListener {
            this.dateFilter = DateFilter(LocalDate.ofEpochDay(0), LocalDate.MAX, false)
        }
    }

    fun setDataPassListener(listener: DataPassListener) {
        dataPassListener = listener
    }

    private fun passDataBack(dateFilter: DateFilter) {
        dataPassListener?.onDateFilterPassed(dateFilter)
    }

    interface DataPassListener {
        fun onDateFilterPassed(dateFilter: DateFilter)
    }
}