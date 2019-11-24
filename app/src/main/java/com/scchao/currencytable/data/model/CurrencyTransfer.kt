package com.scchao.currencytable.data.model

class CurrencyTransfer (
    var source: CurrencyInfo = CurrencyInfo(),
    var datas: MutableList<CurrencyInfo> = mutableListOf(),
    var keys: MutableList<String> = mutableListOf()
)