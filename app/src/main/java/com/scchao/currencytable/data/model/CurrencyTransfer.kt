package com.scchao.currencytable.data.model

class CurrencyTransfer(
    var data: MutableList<CurrencyInfo> = mutableListOf()
) {
    fun getFullNameKeys(): MutableList<String> {
        var rtKeys: MutableList<String> = mutableListOf()
        for (info in data) {
            val nameKey = "(${info.code})${info.name}"
            rtKeys.add(nameKey)
        }
        return rtKeys
    }
}