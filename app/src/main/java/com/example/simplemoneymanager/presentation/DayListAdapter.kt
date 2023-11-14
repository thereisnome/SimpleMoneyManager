package com.example.simplemoneymanager.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplemoneymanager.databinding.DayItemBinding
import com.example.simplemoneymanager.domain.day.Day
import java.time.format.DateTimeFormatter
import java.util.Locale

class DayListAdapter(private val dayList: List<Day>) : RecyclerView.Adapter<DayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val binding =
            DayItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = dayList[position]
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        val formattedDate = day.date.format(formatter)

        holder.binding.tvDate.text = formattedDate

        val adapter = TransactionListAdapter(day.transactions)
        holder.binding.rvTransactions.adapter = adapter
    }

    override fun getItemCount(): Int {
        return dayList.size
    }
}