package com.scchao.currencytable.ui.model

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.*
import com.scchao.currencytable.data.model.CurrencyInfo
import com.scchao.currencytable.data.model.CurrencyRates
import com.scchao.currencytable.data.model.CurrencyTransfer
import com.scchao.currencytable.data.model.CurrencyTypes
import com.scchao.currencytable.data.repository.CurrencyDataRepository
import com.scchao.currencytable.data.repository.CurrencyListRepository
import org.koin.dsl.module
import java.util.*

val mainViewModule = module {
    factory { MainViewModel(get(), get(), get()) }
}

class MainViewModel(
    private val currencyListRepository: CurrencyListRepository,
    private val currencyDataRepository: CurrencyDataRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    // TODO: Implement the ViewModel

    private val trigger = MutableLiveData<Boolean>()
    private val checkPrice = MutableLiveData<Double>()
    private val targetRate = MutableLiveData<Double>()

    private val currencyTypesRow = trigger.switchMap { ti ->
        liveData {
            var returnData: CurrencyTransfer = CurrencyTransfer()
            try {
                val lastSave = sharedPreferences.getLong("logTime", -1)
                Log.i("MainViewModel", "Last fetch time: ${lastSave}")
                val period = 30 * 60 * 1000
                val logTime = Calendar.getInstance().time.time
                Log.i("MainViewModel", "was ${logTime - lastSave} millisecond before")

                if ((lastSave + period) > logTime) {
                    // If the last time successfully fetch the data was less than 30-minutes ago,
                    // get the data from Room
                    Log.i("MainViewModel", "reload data")
                    returnData.data.addAll(currencyDataRepository.loadRateList())
                } else {
                    // If the last time successfully fetch the data was more than 30-minutes ago,
                    // fetch the data from api
                    Log.i("MainViewModel", "fetch data")
                    val currencyTypes = currencyListRepository.getCurrencyTypes()
                    val rateData = currencyListRepository.getCurrencyRates()
                    returnData = assembleCurrencyData(currencyTypes, rateData)
                }


            } catch (exception: Throwable) {
                Log.i("error", exception.message)
            }
            emit(returnData)
        }
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

    /* Memo about assembleCurrencyData method
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

    private suspend fun assembleCurrencyData(
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
                    returnData.data.add(currencyInfo)
                }
            }
            //Save all of the assembled data in the Room DB
            currencyDataRepository.updateAllRate(returnData.data)

            // Log the time in SharedPreferences
            val logTime = Calendar.getInstance().time.time
            sharedPreferences.edit { putLong("logTime", logTime) }
            Log.i("MainViewModule", "Fetched data saved at ${logTime}")
        }

        return returnData
    }

}
