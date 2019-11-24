package com.scchao.currencytable.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.scchao.currencytable.R
import com.scchao.currencytable.data.model.CurrencyInfo
import java.text.DecimalFormat

class CurrencyCardView @JvmOverloads constructor(
    context: Context,
    val data: CurrencyInfo,
    val selectRate: Double = 1.0,
    val targetPrice: Double = 1.0,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        initView(context)
    }

    private var code: TextView? = null
    private var currency: TextView? = null

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.currency_card, this)
        code = view.findViewById(R.id.code)
        code?.text = data.code
        currency = view.findViewById(R.id.currency)
        val dec = DecimalFormat("#,###.##")
        currency?.text = "$ ${dec.format(calculatePrice())}"
    }

    private fun calculatePrice(): Double {
        return data.rate * targetPrice / selectRate
    }
}
