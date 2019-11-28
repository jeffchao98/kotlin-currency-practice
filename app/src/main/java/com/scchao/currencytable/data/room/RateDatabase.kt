package com.scchao.currencytable.data.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.scchao.currencytable.data.model.CurrencyInfo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

fun dbSetupModule() = module {
    single {
        Room.databaseBuilder(androidContext(), RateDatabase::class.java, "CurrencyTable.db")
            .build()
    }
}


@Database(entities = arrayOf(CurrencyInfo::class), version = 1)
abstract class RateDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao
}