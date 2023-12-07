package com.example.simplemoneymanager.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters.firstDayOfMonth
import java.time.temporal.TemporalAdjusters.lastDayOfMonth

@Parcelize
data class DateFilter(val startDate: LocalDate, val endDate: LocalDate, val isRangeSet: Boolean): Parcelable {

    override fun toString(): String {
        return "$startDate - $endDate"
    }

    companion object{

        fun getDefaultDateFilter(): DateFilter{
            return DateFilter(LocalDate.now().with(firstDayOfMonth()), LocalDate.now().with(
                lastDayOfMonth()), false)
        }
    }
}