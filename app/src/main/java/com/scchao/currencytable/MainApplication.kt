package com.scchao.currencytable

import android.app.Application
import com.scchao.currencytable.api.networkModule
import com.scchao.currencytable.data.preferencesModule
import com.scchao.currencytable.data.repository.currencyDataRepoModule
import com.scchao.currencytable.data.repository.currencyListRepoModule
import com.scchao.currencytable.data.room.dbSetupModule
import com.scchao.currencytable.ui.model.mainViewModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(
                listOf(
                    currencyListRepoModule,
                    networkModule,
                    mainViewModule,
                    dbSetupModule(),
                    currencyDataRepoModule,
                    preferencesModule
                )
            )
        }
    }
}