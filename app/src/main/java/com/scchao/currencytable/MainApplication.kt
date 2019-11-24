package com.scchao.currencytable

import android.app.Application
import com.scchao.currencytable.api.networkModule
import com.scchao.currencytable.data.repository.currencyListRepoModule
import com.scchao.currencytable.ui.model.mainViewModule
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                listOf(
                    currencyListRepoModule,
                    networkModule,
                    mainViewModule
                )
            )
        }
    }
}