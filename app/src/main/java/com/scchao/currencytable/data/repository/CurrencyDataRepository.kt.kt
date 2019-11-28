package com.scchao.currencytable.data.repository

import com.scchao.currencytable.data.model.CurrencyInfo
import com.scchao.currencytable.data.room.RateDao
import com.scchao.currencytable.data.room.RateDatabase
import org.koin.dsl.module

val currencyDataRepoModule = module {
    factory { CurrencyDataRepository(get()) }
    factory { get<RateDatabase>().rateDao() }
}

class CurrencyDataRepository(private val rateDao: RateDao) {
    suspend fun loadRateList(): List<CurrencyInfo> = rateDao.getAllList()
    suspend fun updateRate(saveRate: CurrencyInfo) = rateDao.updateRate(saveRate)
    suspend fun findRate(code: String) = rateDao.findRate(code)
    suspend fun updateAllRate(saveList: List<CurrencyInfo>) = rateDao.updateAllRate(saveList)
}