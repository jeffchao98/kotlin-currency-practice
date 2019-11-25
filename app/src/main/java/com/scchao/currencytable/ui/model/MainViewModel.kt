package com.scchao.currencytable.ui.model

import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import androidx.lifecycle.*
import com.scchao.currencytable.data.model.CurrencyInfo
import com.scchao.currencytable.data.model.CurrencyRates
import com.scchao.currencytable.data.model.CurrencyTransfer
import com.scchao.currencytable.data.model.CurrencyTypes
import com.scchao.currencytable.data.repository.CurrencyListRepository
import org.koin.dsl.module

val mainViewModule = module {
    factory { MainViewModel(get()) }
}

class MainViewModel(
    private val currencyListRepository: CurrencyListRepository
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val trigger = MutableLiveData<Boolean>()

    private val currencyTypesRow = trigger.switchMap { ti ->
        liveData {
            var returnData: CurrencyTransfer = CurrencyTransfer()
            try {
                val currencyTypes = currencyListRepository.getCurrencyTypes()
                val rateData = currencyListRepository.getCurrenctRates()
                returnData = getCurrencyData(currencyTypes, rateData)

            } catch (exception: Throwable) {
                Log.i("error", exception.message)
            }
            emit(returnData)
        }
    }

    init {
        doQueryData()
    }

    fun readyData(): LiveData<CurrencyTransfer> = currencyTypesRow

    fun doQueryData() {
        trigger.value = true
    }

    private fun getCurrencyData(
        currencyTypes: CurrencyTypes,
        rateData: CurrencyRates
    ): CurrencyTransfer {
        var returnData: CurrencyTransfer = CurrencyTransfer()

        val rates = rateData.quotes
        val currencies = currencyTypes.currencies
        val typeKeys = currencies.keys.toList()
        val sources = rateData.source

        returnData.source = CurrencyInfo(
            code = sources,
            name = currencies.get(sources) ?: "",
            rate = rates.get("${sources}${sources}") ?: 1.0
        )

        for (typeKey in typeKeys) {
            val fullName = currencies.get(typeKey) ?: ""
            returnData.datas.add(
                CurrencyInfo(
                    code = typeKey,
                    name = fullName,
                    rate = rates.get("${sources}${typeKey}") ?: 1.0
                )
            )
            returnData.keys.add(fullName)
        }
        return returnData
    }

}
