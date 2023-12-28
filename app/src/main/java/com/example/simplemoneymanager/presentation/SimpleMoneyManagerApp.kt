package com.example.simplemoneymanager.presentation

import android.app.Application
import com.example.simplemoneymanager.di.DaggerAppComponent

class SimpleMoneyManagerApp: Application() {

    val component by lazy {
        DaggerAppComponent.factory().create(this)
    }
}