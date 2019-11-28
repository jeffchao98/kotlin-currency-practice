package com.scchao.currencytable.ui.model

import android.util.Log
import androidx.lifecycle.*
import com.scchao.currencytable.data.model.CurrencyInfo
import com.scchao.currencytable.data.model.CurrencyRates
import com.scchao.currencytable.data.model.CurrencyTransfer
import com.scchao.currencytable.data.model.CurrencyTypes
import com.scchao.currencytable.data.repository.CurrencyDataRepository
import com.scchao.currencytable.data.repository.CurrencyListRepository
import org.koin.dsl.module

val mainViewModule = module {
    factory { MainViewModel(get(), get()) }
}

class MainViewModel(
    private val currencyListRepository: CurrencyListRepository,
    private val currencyDataRepository: CurrencyDataRepository
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val trigger = MutableLiveData<Boolean>()
    private val checkPrice = MutableLiveData<Double>()
    private val targetRate = MutableLiveData<Double>()

    private val currencyTypesRow = trigger.switchMap { ti ->
        liveData {
            var returnData: CurrencyTransfer = CurrencyTransfer()
            try {
                val loadData = currencyDataRepository.loadRateList()
                val currencyTypes = currencyListRepository.getCurrencyTypes()
                val rateData = currencyListRepository.getCurrencyRates()
                returnData = getCurrencyData(currencyTypes, rateData)

            } catch (exception: Throwable) {
                Log.i("error", exception.message)
            }
            emit(returnData)
        }
    }

    init {
        fetchData()
    }

    fun readyData(): LiveData<CurrencyTransfer> = currencyTypesRow
    fun getTargetPrice(): LiveData<Double> = checkPrice
    fun getStandRate(): LiveData<Double> = targetRate

    fun fetchData() {
        trigger.value = true
    }

    fun updatePrice(price: Double) {
        checkPrice.value = price
    }

    fun updateRate(rate: Double) {
        targetRate.value = rate
    }

    /* Memo about getCurrencyData method
     * In case something goes wrong when fetch the currency data
     * The following rules are prepared for the exceptions like internet error
     * 1. If the response data from the currency rate api shows null,
     *    we should return empty CurrencyTransfer because we have no idea about
     *    the most important value rate
     *
     * 2. If the response data from the currency type ( code v.s. full-name ) api
     *    is the only missing data, in this case, we still can parse and prepare
     *    the CurrencyTransfer for return, however the full name in drop-down labels
     *    will be replaced by the short code
     */

    private suspend fun getCurrencyData(
        currencyTypes: CurrencyTypes,
        rateData: CurrencyRates
    ): CurrencyTransfer {
        var returnData: CurrencyTransfer = CurrencyTransfer()

        val rates = rateData.quotes
        val currencies = currencyTypes.currencies
        if (rates != null) {
            val typeKeys: MutableList<String> = mutableListOf()
            val rateSource = rateData.source
            if (currencies != null) {
                typeKeys.addAll(currencies.keys.toList())
            } else {
                for (key in rates.keys.toList()) {
                    typeKeys.add(key.replaceFirst(rateSource, ""))
                }
            }
            val sources = rateData.source
            for (typeKey in typeKeys) {
                // full key = source type code + target type code
                val fullKey = "${sources}${typeKey}"
                // Before create and add any CurrencyInfo object,
                // will check if we can find the key in the map of the rates
                if (rates.containsKey(fullKey)) {
                    var fullName = typeKey
                    if (currencies != null) {
                        fullName = currencies.get(typeKey) ?: ""
                    }
                    // Add the CurrencyInfo object in the data list for the grid list
                    val currencyInfo = CurrencyInfo(
                        code = typeKey,
                        name = fullName,
                        rate = rates.get(fullKey) ?: 1.0
                    )
                    currencyDataRepository.updateRate(currencyInfo)
                    returnData.data.add(currencyInfo)
                }
            }
        }
        return returnData
    }

}
