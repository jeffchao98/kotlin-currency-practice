package com.scchao.currencytable.api

import com.scchao.currencytable.data.model.CurrencyRates
import com.scchao.currencytable.data.model.CurrencyTypes
import retrofit2.http.GET

const val access_token = "2779a0016237dd47bf7e6adafb3c2199"

interface CurrencyApi {
    @GET("/api/list?access_key=${access_token}&format=1")
    suspend fun getCurrencyTypes(): CurrencyTypes

    @GET("/api/live?access_key=${access_token}&format=1")
    suspend fun getCurrencyRates(): CurrencyRates
}