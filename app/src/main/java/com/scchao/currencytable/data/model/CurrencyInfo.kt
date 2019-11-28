package com.scchao.currencytable.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "collect_table")
data class CurrencyInfo (
    @PrimaryKey
    @ColumnInfo(name = "code")
    var code: String = "",
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "rate")
    var rate: Double = 1.0
)