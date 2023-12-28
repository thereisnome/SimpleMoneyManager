package com.example.simplemoneymanager.common

import java.text.NumberFormat
import java.util.Locale

class Format {

    fun formatCurrency(value: Double): String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return if (value>=0){
            "+$formattedAmount".replace(" руб.", "₽")
        } else formattedAmount.replace(" руб.", "₽")
    }

    fun formatCurrencyWithoutSign(value: Double) : String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return formattedAmount.replace(" руб.", "₽")
    }

    fun formatCurrencyWithoutCurrencySigh(value: Double) : String {
        val formattedAmount = NumberFormat.getCurrencyInstance(Locale("ru", "RU")).format(value)
        return formattedAmount.replace(" руб.", "")
    }
}