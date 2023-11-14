package com.example.simplemoneymanager.domain.day

import com.example.simplemoneymanager.domain.transaction.Transaction
import java.time.LocalDate

data class Day(
    val id: Int,
    val date: LocalDate,
    val transactions: List<Transaction>
)