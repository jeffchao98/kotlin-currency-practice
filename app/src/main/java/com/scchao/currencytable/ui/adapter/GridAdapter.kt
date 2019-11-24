package com.scchao.currencytable.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.scchao.currencytable.data.model.CurrencyInfo
import com.scchao.currencytable.ui.view.CurrencyCardView

class GridAdapter @JvmOverloads constructor(
    var context: Context,
    var rateList: List<CurrencyInfo>,
    var targetPrice: Double,
    var selectedRate: Double
) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rateItem = rateList[position]
        return CurrencyCardView(context, rateItem, selectedRate, targetPrice)
    }

    override fun getItem(position: Int): Any {
        return rateList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return rateList.size
    }

    fun updateSelectRate(newRate: Double) {
        this.selectedRate = newRate
        notifyDataSetChanged()
    }

    fun updateTargetPrice(newPrice: Double) {
        this.targetPrice = newPrice
        notifyDataSetChanged()
    }
}