package com.scchao.currencytable.api

import com.scchao.currencytable.BuildConfig.CURRENCY_TOKEN
import com.scchao.currencytable.data.model.CurrencyRates
import com.scchao.currencytable.data.model.CurrencyTypes
import retrofit2.http.GET

interface CurrencyApi {
    @GET("/api/list?access_key=${CURRENCY_TOKEN}&format=1")
    suspend fun getCurrencyTypes(): CurrencyTypes

    @GET("/api/live?access_key=${CURRENCY_TOKEN}&format=1")
    suspend fun getCurrencyRates(): CurrencyRates
}