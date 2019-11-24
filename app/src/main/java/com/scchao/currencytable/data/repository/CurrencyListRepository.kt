package com.scchao.currencytable.data.repository

import com.scchao.currencytable.api.CurrencyApi
import org.koin.dsl.module

val currencyListRepoModule = module {
    factory { CurrencyListRepository(get()) }
}

class CurrencyListRepository (private val currencyApi: CurrencyApi) {
    suspend fun getCurrencyTypes() = currencyApi.getCurrencyTypes()
    suspend fun getCurrenctRates() = currencyApi.getCurrenctRates()
}